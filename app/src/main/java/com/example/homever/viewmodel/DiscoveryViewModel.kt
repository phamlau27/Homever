package com.example.homever.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homever.data.network.ConversationSummary
import com.example.homever.data.network.CandidateResponse
import com.example.homever.data.network.HomeverApi
import com.example.homever.data.network.InterestRequest
import com.example.homever.data.network.InterestsResponse
import com.example.homever.data.network.MatchingProfileError
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DiscoveryUiState(
    val candidates: List<CandidateResponse> = emptyList(),
    val currentIndex: Int = 0,
    val cardPage: Int = 0,
    val isLoading: Boolean = false,
    val candidatesLoaded: Boolean = false,
    val profileIncomplete: Boolean = false,
    val errorMessage: String? = null,
    val interests: InterestsResponse = InterestsResponse(),
    val interestsLoading: Boolean = false,
    val interestsError: String? = null,
    val sendingProfileId: String? = null,
    val matchedConversation: ConversationSummary? = null
)

class DiscoveryViewModel(private val api: HomeverApi) : ViewModel() {
    private val mutableState = MutableStateFlow(DiscoveryUiState())
    val state = mutableState.asStateFlow()

    private var candidatesJob: Job? = null

    fun invalidateCandidates() {
        candidatesJob?.cancel()
        mutableState.update {
            it.copy(candidates = emptyList(), currentIndex = 0, cardPage = 0,
                isLoading = false, candidatesLoaded = false, profileIncomplete = false, errorMessage = null)
        }
    }

    fun loadCandidates(force: Boolean = false) {
        if (mutableState.value.isLoading || (!force && mutableState.value.candidatesLoaded)) return
        mutableState.update { it.copy(isLoading = true, errorMessage = null, profileIncomplete = false) }
        candidatesJob = viewModelScope.launch {
            try {
                val response = api.getMatchingCandidates(limit = 100)
                if (response.code() == 422) {
                    val detail = runCatching {
                        Gson().fromJson(response.errorBody()?.string(), MatchingProfileError::class.java)
                    }.getOrNull()
                    if (detail?.code == "MATCHING_PROFILE_INCOMPLETE") {
                        mutableState.update {
                            it.copy(candidates = emptyList(), currentIndex = 0, cardPage = 0,
                                isLoading = false, candidatesLoaded = true, profileIncomplete = true,
                                errorMessage = "Hãy hoàn tất lựa chọn tìm bạn cùng phòng để nhận gợi ý phù hợp.")
                        }
                        return@launch
                    }
                }
                if (!response.isSuccessful) error("HTTP ${response.code()}")
                val profiles = response.body().orEmpty().distinctBy { it.profile.id }
                mutableState.update { current ->
                    val currentId = current.candidates.getOrNull(current.currentIndex)?.profile?.id
                    val sentIds = (current.interests.sent.map { it.profile.id } + current.interests.connectedProfileIds).toSet()
                    val available = profiles.filterNot { it.profile.id in sentIds }
                    current.copy(
                        candidates = available,
                        currentIndex = available.indexOfFirst { it.profile.id == currentId }.coerceAtLeast(0),
                        cardPage = if (available.any { it.profile.id == currentId }) current.cardPage else 0,
                        isLoading = false,
                        candidatesLoaded = true
                    )
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                mutableState.update { it.copy(isLoading = false, errorMessage = "Không tải được danh sách. Hãy thử lại.") }
            }
        }
    }

    private var interestsRequestInFlight = false

    fun loadInterests() = fetchInterests(silent = false)

    fun refreshInterests() = fetchInterests(silent = true)

    private fun fetchInterests(silent: Boolean) {
        if (interestsRequestInFlight) return
        interestsRequestInFlight = true
        if (!silent) mutableState.update { it.copy(interestsLoading = true, interestsError = null) }
        viewModelScope.launch {
            try {
                val response = api.getInterests()
                if (!response.isSuccessful) error("HTTP ${response.code()}")
                val interests = response.body() ?: error("EMPTY_RESPONSE")
                mutableState.update { current ->
                    val currentId = current.candidates.getOrNull(current.currentIndex)?.profile?.id
                    val sentIds = (interests.sent.map { it.profile.id } + interests.connectedProfileIds).toSet()
                    val available = current.candidates.filterNot { it.profile.id in sentIds }
                    current.copy(
                        interests = interests,
                        interestsLoading = false,
                        interestsError = null,
                        candidates = available,
                        currentIndex = available.indexOfFirst { it.profile.id == currentId }
                            .takeIf { it >= 0 } ?: current.currentIndex.coerceAtMost(available.size),
                        cardPage = if (available.any { it.profile.id == currentId }) current.cardPage else 0
                    )
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                mutableState.update { it.copy(interestsLoading = false, interestsError = "Không tải được lượt quan tâm. Hãy thử lại.") }
            } finally {
                interestsRequestInFlight = false
            }
        }
    }

    fun sendInterest(profileId: String) {
        if (profileId.isBlank() || mutableState.value.sendingProfileId != null) return
        mutableState.update { it.copy(sendingProfileId = profileId, errorMessage = null, interestsError = null) }
        viewModelScope.launch {
            try {
                val response = api.sendInterest(InterestRequest(profileId))
                if (!response.isSuccessful) error("HTTP ${response.code()}")
                val interests = response.body() ?: error("EMPTY_RESPONSE")
                mutableState.update { current ->
                    val currentId = current.candidates.getOrNull(current.currentIndex)?.profile?.id
                    val available = current.candidates.filterNot { it.profile.id == profileId }
                    current.copy(
                        interests = interests,
                        sendingProfileId = null,
                        matchedConversation = interests.matchedConversation,
                        candidates = available,
                        currentIndex = available.indexOfFirst { it.profile.id == currentId }
                            .takeIf { it >= 0 } ?: current.currentIndex.coerceAtMost(available.size),
                        cardPage = if (currentId == profileId) 0 else current.cardPage
                    )
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                val message = "Chưa gửi được quan tâm. Vui lòng thử lại."
                mutableState.update { it.copy(sendingProfileId = null, errorMessage = message, interestsError = message) }
            }
        }
    }

    fun applyConnections(profileIds: Set<String>) {
        mutableState.update { current ->
            val currentId = current.candidates.getOrNull(current.currentIndex)?.profile?.id
            val available = current.candidates.filterNot { it.profile.id in profileIds }
            current.copy(
                candidates = available,
                currentIndex = available.indexOfFirst { it.profile.id == currentId }
                    .takeIf { it >= 0 } ?: current.currentIndex.coerceAtMost(available.size),
                cardPage = if (currentId in profileIds) 0 else current.cardPage,
                interests = current.interests.copy(
                    sent = current.interests.sent.filterNot { it.profile.id in profileIds },
                    received = current.interests.received.filterNot { it.profile.id in profileIds },
                    connectedProfileIds = (current.interests.connectedProfileIds + profileIds).distinct()
                )
            )
        }
    }

    fun consumeMatch() {
        mutableState.update { it.copy(matchedConversation = null) }
    }

    fun skip() {
        if (mutableState.value.sendingProfileId != null) return
        mutableState.update { it.copy(currentIndex = (it.currentIndex + 1).coerceAtMost(it.candidates.size), cardPage = 0, errorMessage = null) }
    }

    fun previous() {
        if (mutableState.value.sendingProfileId != null) return
        mutableState.update { it.copy(currentIndex = (it.currentIndex - 1).coerceAtLeast(0), cardPage = 0, errorMessage = null) }
    }

    fun selectPage(page: Int) {
        mutableState.update { it.copy(cardPage = page.coerceAtLeast(0)) }
    }

    fun reset() {
        viewModelScope.coroutineContext.cancelChildren()
        mutableState.value = DiscoveryUiState()
    }
}
