package com.example.homever.viewmodel

import android.net.Uri
import com.example.homever.domain.model.LifestyleCriteria
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ProfileStatus {
    IDLE, LOADING, SUCCESS, ERROR, UNAUTHORIZED
}

/**
 * Holds all onboarding state as [StateFlow] so it survives recomposition and is observable
 * from any composable. Temporary data is stored here during the multi-step onboarding flow
 * and only submitted to the backend at the final confirmation step.
 */
class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // ── Status / Error ─────────────────────────────────────────────────────
    private val _status = MutableStateFlow(ProfileStatus.IDLE)
    val status: StateFlow<ProfileStatus> = _status.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ── Current User Profile ───────────────────────────────────────────────
    private val _currentProfile = MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile.asStateFlow()

    // ── Avatar (optional — S3 upload deferred) ─────────────────────────────
    private val _avatarUri = MutableStateFlow<Uri?>(null)
    val avatarUri: StateFlow<Uri?> = _avatarUri.asStateFlow()

    // ── Photos for Carousel / Profile ──────────────────────────────────────
    private val _tempPhotos = MutableStateFlow<List<Uri>>(emptyList())
    val tempPhotos: StateFlow<List<Uri>> = _tempPhotos.asStateFlow()

    // ── Onboarding temp data (StateFlow so recomposition doesn't lose values) ──
    private val _tempDisplayName = MutableStateFlow("")
    val tempDisplayName: StateFlow<String> = _tempDisplayName.asStateFlow()

    private val _tempGender = MutableStateFlow("")
    val tempGender: StateFlow<String> = _tempGender.asStateFlow()

    private val _tempAge = MutableStateFlow("")
    val tempAge: StateFlow<String> = _tempAge.asStateFlow()

    private val _tempOccupation = MutableStateFlow("")
    val tempOccupation: StateFlow<String> = _tempOccupation.asStateFlow()

    private val _tempNeedType = MutableStateFlow("roommate")
    val tempNeedType: StateFlow<String> = _tempNeedType.asStateFlow()

    private val _tempLifestyle = MutableStateFlow("")
    val tempLifestyle: StateFlow<String> = _tempLifestyle.asStateFlow()

    private val _tempBudget = MutableStateFlow("")
    val tempBudget: StateFlow<String> = _tempBudget.asStateFlow()

    private val _tempLocation = MutableStateFlow("")
    val tempLocation: StateFlow<String> = _tempLocation.asStateFlow()

    private val _tempBio = MutableStateFlow("")
    val tempBio: StateFlow<String> = _tempBio.asStateFlow()

    private val _tempTraits = MutableStateFlow("")
    val tempTraits: StateFlow<String> = _tempTraits.asStateFlow()

    // Guard against duplicate submission
    private var isSubmitting = false

    // ── Setters ───────────────────────────────────────────────────────────
    fun saveBasicProfileLocally(displayName: String, gender: String, age: String, occupation: String) {
        _tempDisplayName.value = displayName
        _tempGender.value = gender
        _tempAge.value = age
        _tempOccupation.value = occupation
    }

    fun saveNeedLocally(needType: String) {
        _tempNeedType.value = needType
    }

    fun saveLifestyleLocally(lifestyle: String) {
        _tempLifestyle.value = lifestyle
    }

    fun saveBudgetAndLocationLocally(budget: String, location: String) {
        _tempBudget.value = budget
        _tempLocation.value = location
    }

    fun saveBioLocally(bio: String) {
        _tempBio.value = bio
    }

    fun saveTraitsLocally(traits: String) {
        _tempTraits.value = traits
    }

    fun updateAvatarUri(uri: Uri?) {
        _avatarUri.value = uri
    }

    /**
     * Updates and immediately saves introduction notes (bio, lifestyle, traits) to Backend.
     */
    fun saveIntroNotes(bio: String, lifestyle: String, traits: String, onComplete: ((Boolean) -> Unit)? = null) {
        val updatedLifestyle = LifestyleCriteria.withNote(_tempLifestyle.value, lifestyle)
        _tempBio.value = bio
        _tempLifestyle.value = updatedLifestyle
        _tempTraits.value = traits

        viewModelScope.launch {
            val current = _currentProfile.value ?: UserProfile(
                displayName = _tempDisplayName.value,
                gender = _tempGender.value,
                age = _tempAge.value.toIntOrNull() ?: 20,
                occupation = _tempOccupation.value
            )
            val updated = current.copy(
                bio = bio,
                lifestyle = updatedLifestyle,
                traits = traits
            )
            val result = profileRepository.updateMyProfile(updated)
            result.fold(
                onSuccess = { savedProfile ->
                    populateFromProfile(savedProfile)
                    onComplete?.invoke(true)
                },
                onFailure = {
                    onComplete?.invoke(false)
                }
            )
        }
    }

    /**
     * Uploads an additional photo to Supabase Storage and appends its URL to avatarUrl in PostgreSQL.
     */
    fun uploadAndAddPhoto(uri: Uri, onComplete: ((Boolean, String?) -> Unit)? = null) {
        viewModelScope.launch {
            _status.value = ProfileStatus.LOADING
            val uploadResult = profileRepository.uploadAvatar(uri)
            uploadResult.fold(
                onSuccess = { uploadedUrl ->
                    val current = _currentProfile.value ?: UserProfile(
                        displayName = _tempDisplayName.value,
                        gender = _tempGender.value,
                        age = _tempAge.value.toIntOrNull() ?: 20,
                        occupation = _tempOccupation.value
                    )
                    val rawAvatar = current.avatarUrl ?: _avatarUri.value?.toString() ?: ""
                    val existingPhotos = rawAvatar.split(",")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                    val updatedPhotos = (existingPhotos + uploadedUrl).distinct()
                    val newAvatarUrl = updatedPhotos.joinToString(",")

                    val updatedProfile = current.copy(avatarUrl = newAvatarUrl)
                    val updateResult = profileRepository.updateMyProfile(updatedProfile)
                    updateResult.fold(
                        onSuccess = { saved ->
                            populateFromProfile(saved)
                            _status.value = ProfileStatus.SUCCESS
                            onComplete?.invoke(true, uploadedUrl)
                        },
                        onFailure = { error ->
                            _status.value = ProfileStatus.ERROR
                            _errorMessage.value = "Lưu ảnh thất bại: ${error.message}"
                            onComplete?.invoke(false, null)
                        }
                    )
                },
                onFailure = { error ->
                    _status.value = ProfileStatus.ERROR
                    _errorMessage.value = "Tải ảnh thất bại: ${error.message}"
                    onComplete?.invoke(false, null)
                }
            )
        }
    }

    fun savePhotosLocally(photos: List<Uri>) {
        _tempPhotos.value = photos
        if (_avatarUri.value == null && photos.isNotEmpty()) {
            _avatarUri.value = photos.first()
        }
    }

    private fun populateFromProfile(profile: UserProfile) {
        _currentProfile.value = profile
        _tempDisplayName.value = profile.displayName
        _tempGender.value = profile.gender
        _tempAge.value = if (profile.age > 0) profile.age.toString() else ""
        _tempOccupation.value = profile.occupation
        _tempNeedType.value = profile.needType ?: "roommate"
        _tempLifestyle.value = profile.lifestyle ?: ""
        _tempBudget.value = profile.budget ?: ""
        _tempLocation.value = profile.location ?: ""
        _tempBio.value = profile.bio ?: ""
        _tempTraits.value = profile.traits ?: ""
        if (!profile.avatarUrl.isNullOrBlank()) {
            val firstPhoto = profile.avatarUrl.split(",").firstOrNull { it.isNotBlank() }
            if (firstPhoto != null) {
                _avatarUri.value = Uri.parse(firstPhoto)
            }
        }
    }

    fun onLocalNetworkPermissionDenied() {
        _status.value = ProfileStatus.ERROR
        _errorMessage.value = "Cần quyền truy cập mạng nội bộ để kết nối máy chủ. Hãy cấp quyền rồi nhấn Thử lại."
    }

    fun clearError() {
        _errorMessage.value = null
        if (_status.value == ProfileStatus.ERROR) {
            _status.value = ProfileStatus.IDLE
        }
    }

    // ── Profile existence check ────────────────────────────────────────────
    /**
     * Calls GET /v1/users/me to determine whether a Homever profile exists for the current user.
     *
     * - HTTP 200 with a non-blank displayName → profile found → [onResult](true, false)
     * - HTTP 404 / PROFILE_NOT_FOUND → no profile → [onResult](false, false)
     * - HTTP 401 → token genuinely invalid → [onResult](null, true)
     * - Network / server error → [onResult](null, false) — do NOT clear session
     */
    fun checkProfileExists(onResult: (exists: Boolean?, isUnauthorized: Boolean) -> Unit) {
        viewModelScope.launch {
            _status.value = ProfileStatus.LOADING
            _errorMessage.value = null
            val result = profileRepository.getMyProfile()
            result.fold(
                onSuccess = { profile ->
                    // A profile is considered "existing" only when displayName has been set.
                    if (profile.displayName.isNotBlank()) {
                        populateFromProfile(profile)
                        _status.value = ProfileStatus.SUCCESS
                        onResult(true, false)
                    } else {
                        _currentProfile.value = profile
                        _status.value = ProfileStatus.IDLE
                        onResult(false, false)
                    }
                },
                onFailure = { error ->
                    when (error.message) {
                        "PROFILE_NOT_FOUND" -> {
                            _status.value = ProfileStatus.IDLE
                            onResult(false, false)
                        }
                        "UNAUTHORIZED" -> {
                            _status.value = ProfileStatus.UNAUTHORIZED
                            _errorMessage.value = "Phiên đăng nhập đã hết hạn"
                            onResult(null, true)
                        }
                        else -> {
                            // Network / server error — preserve session, let caller decide.
                            Log.e("ProfileViewModel", "checkProfileExists failed: ${error.message}", error)
                            _status.value = ProfileStatus.ERROR
                            _errorMessage.value = "Lỗi kết nối máy chủ khi kiểm tra hồ sơ: ${error.message}"
                            onResult(null, false)
                        }
                    }
                }
            )
        }
    }

    /**
     * Fetches current user profile from GET /v1/users/me and populates all StateFlow fields.
     */
    fun loadProfile(onComplete: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            _status.value = ProfileStatus.LOADING
            _errorMessage.value = null
            val result = profileRepository.getMyProfile()
            result.fold(
                onSuccess = { profile ->
                    if (profile.displayName.isNotBlank()) {
                        populateFromProfile(profile)
                        _status.value = ProfileStatus.SUCCESS
                        onComplete?.invoke(true)
                    } else {
                        _currentProfile.value = profile
                        _status.value = ProfileStatus.IDLE
                        onComplete?.invoke(false)
                    }
                },
                onFailure = { error ->
                    Log.e("ProfileViewModel", "loadProfile failed: ${error.message}", error)
                    when (error.message) {
                        "PROFILE_NOT_FOUND" -> {
                            _currentProfile.value = null
                            _status.value = ProfileStatus.IDLE
                        }
                        "UNAUTHORIZED" -> {
                            _status.value = ProfileStatus.UNAUTHORIZED
                            _errorMessage.value = "Phiên đăng nhập đã hết hạn"
                        }
                        else -> {
                            _status.value = ProfileStatus.ERROR
                            _errorMessage.value = if (error.message?.startsWith("NETWORK_ERROR") == true) {
                                "Không thể kết nối máy chủ. Hãy kiểm tra backend đang chạy và nhấn Thử lại."
                            } else {
                                error.localizedMessage
                            }
                        }
                    }
                    onComplete?.invoke(false)
                }
            )
        }
    }

    // ── Profile submission ─────────────────────────────────────────────────
    fun completeProfile(onSuccess: () -> Unit) {
        if (isSubmitting) return
        if (_tempDisplayName.value.isBlank() || _tempGender.value.isBlank() ||
            _tempAge.value.isBlank() || _tempOccupation.value.isBlank()
        ) {
            _errorMessage.value = "Vui lòng điền đầy đủ thông tin"
            _status.value = ProfileStatus.ERROR
            return
        }

        val ageInt = _tempAge.value.toIntOrNull()
        if (ageInt == null || ageInt < 16 || ageInt > 100) {
            _errorMessage.value = "Tuổi không hợp lệ (16–100)"
            _status.value = ProfileStatus.ERROR
            return
        }

        isSubmitting = true
        _status.value = ProfileStatus.LOADING
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                var finalAvatarUrl: String? = _currentProfile.value?.avatarUrl

                // If user selected a new local image URI, upload it to Supabase Storage first
                val selectedAvatarUri = _avatarUri.value
                if (selectedAvatarUri != null && !selectedAvatarUri.toString().startsWith("http")) {
                    val uploadResult = profileRepository.uploadAvatar(selectedAvatarUri)
                    uploadResult.fold(
                        onSuccess = { uploadedUrl ->
                            finalAvatarUrl = uploadedUrl
                            Log.d("ProfileViewModel", "Avatar uploaded successfully: $uploadedUrl")
                        },
                        onFailure = { uploadError ->
                            Log.e("ProfileViewModel", "Avatar upload failed: ${uploadError.message}", uploadError)
                            // Allow graceful fallback without crashing, or notify in log
                        }
                    )
                }

                val profile = UserProfile(
                    displayName = _tempDisplayName.value,
                    gender = _tempGender.value,
                    age = ageInt,
                    occupation = _tempOccupation.value,
                    avatarUrl = finalAvatarUrl,
                    needType = _tempNeedType.value,
                    lifestyle = _tempLifestyle.value,
                    budget = _tempBudget.value,
                    location = _tempLocation.value,
                    bio = _tempBio.value,
                    traits = _tempTraits.value
                )

                val updateResult = profileRepository.updateMyProfile(profile)

                updateResult.fold(
                    onSuccess = { updatedProfile ->
                        populateFromProfile(updatedProfile)
                        _status.value = ProfileStatus.SUCCESS
                        isSubmitting = false
                        onSuccess()
                    },
                    onFailure = { error ->
                        isSubmitting = false
                        when (error.message) {
                            "UNAUTHORIZED" -> {
                                _status.value = ProfileStatus.UNAUTHORIZED
                                _errorMessage.value = "Phiên đăng nhập đã hết hạn"
                            }
                            else -> {
                                _errorMessage.value = "Lưu hồ sơ thất bại: ${error.localizedMessage}"
                                _status.value = ProfileStatus.ERROR
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                isSubmitting = false
                _errorMessage.value = "Đã xảy ra lỗi hệ thống."
                _status.value = ProfileStatus.ERROR
            }
        }
    }
}
