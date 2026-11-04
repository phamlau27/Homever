package com.example.homever.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ProfileStatus {
    IDLE, LOADING, SUCCESS, ERROR
}

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _status = MutableStateFlow(ProfileStatus.IDLE)
    val status = _status.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _avatarUri = MutableStateFlow<Uri?>(null)
    val avatarUri = _avatarUri.asStateFlow()

    fun updateAvatarUri(uri: Uri?) {
        _avatarUri.value = uri
    }

    fun checkProfileExists(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = profileRepository.getMyProfile()
            result.fold(
                onSuccess = { profile ->
                    // Assuming empty displayName means incomplete
                    if (profile.displayName.isNotBlank()) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                },
                onFailure = {
                    onResult(false)
                }
            )
        }
    }

    fun completeProfile(displayName: String, gender: String, age: String, occupation: String, onSuccess: () -> Unit) {
        if (displayName.isBlank() || gender.isBlank() || age.isBlank() || occupation.isBlank()) {
            _errorMessage.value = "Vui lòng điền đầy đủ thông tin"
            _status.value = ProfileStatus.ERROR
            return
        }

        val ageInt = age.toIntOrNull()
        if (ageInt == null || ageInt < 16 || ageInt > 100) {
            _errorMessage.value = "Tuổi không hợp lệ"
            _status.value = ProfileStatus.ERROR
            return
        }

        _status.value = ProfileStatus.LOADING
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                var avatarUrl: String? = null
                val uri = _avatarUri.value
                
                // 1. Upload Avatar if selected
                if (uri != null) {
                    val uploadResult = profileRepository.uploadAvatar(uri, "image/jpeg") // simplification
                    uploadResult.fold(
                        onSuccess = { urlOrKey -> avatarUrl = urlOrKey },
                        onFailure = { 
                            _errorMessage.value = "Tải ảnh thất bại: \${it.localizedMessage}"
                            _status.value = ProfileStatus.ERROR
                            return@launch
                        }
                    )
                }

                // 2. Save Profile
                val profile = UserProfile(
                    displayName = displayName,
                    gender = gender,
                    age = ageInt,
                    occupation = occupation,
                    avatarUrl = avatarUrl
                )
                
                val updateResult = profileRepository.updateMyProfile(profile)
                
                updateResult.fold(
                    onSuccess = {
                        _status.value = ProfileStatus.SUCCESS
                        onSuccess()
                    },
                    onFailure = {
                        _errorMessage.value = "Lưu hồ sơ thất bại: \${it.localizedMessage}"
                        _status.value = ProfileStatus.ERROR
                    }
                )

            } catch (e: Exception) {
                _errorMessage.value = "Đã xảy ra lỗi hệ thống."
                _status.value = ProfileStatus.ERROR
            }
        }
    }
}