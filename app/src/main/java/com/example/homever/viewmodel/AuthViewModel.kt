package com.example.homever.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homever.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthStateStatus {
    IDLE, LOADING, OTP_REQUIRED_SIGN_UP, OTP_REQUIRED_SIGN_IN, AUTHENTICATED, ERROR
}

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _emailOrPhone = MutableStateFlow("")
    val emailOrPhone = _emailOrPhone.asStateFlow()

    private val _status = MutableStateFlow(AuthStateStatus.IDLE)
    val status = _status.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun updateEmailOrPhone(value: String) {
        _emailOrPhone.value = value
    }

    fun clearError() {
        _errorMessage.value = null
        if (_status.value == AuthStateStatus.ERROR) {
            _status.value = AuthStateStatus.IDLE
        }
    }
    
    fun setAuthenticated() {
        _status.value = AuthStateStatus.AUTHENTICATED
    }

    fun checkAccountAndSendOtp(selectedCountryCode: String = "") {
        if (_emailOrPhone.value.isBlank()) return
        
        // E.164 Formatting
        val rawInput = _emailOrPhone.value.trim()
        val isPhoneNumber = rawInput.all { it.isDigit() || it == '+' || it == ' ' } && rawInput.isNotBlank()
        
        val formattedUsername = if (isPhoneNumber) {
            val cleanedNumber = rawInput.replace(" ", "")
            if (cleanedNumber.startsWith("0")) {
                selectedCountryCode + cleanedNumber.substring(1)
            } else if (!cleanedNumber.startsWith("+")) {
                selectedCountryCode + cleanedNumber
            } else {
                cleanedNumber
            }
        } else {
            rawInput
        }
        
        _status.value = AuthStateStatus.LOADING
        _errorMessage.value = null
        
        viewModelScope.launch {
            // First try to SignIn
            val signInResult = authRepository.signIn(formattedUsername)
            
            signInResult.fold(
                onSuccess = { requiresOtp ->
                    if (requiresOtp) {
                        _status.value = AuthStateStatus.OTP_REQUIRED_SIGN_IN
                    } else {
                        _status.value = AuthStateStatus.AUTHENTICATED
                    }
                },
                onFailure = { signInException ->
                    val errorMsg = signInException.message ?: ""
                    val isUserNotFound = signInException.javaClass.simpleName.contains("UserNotFound", ignoreCase = true) 
                            || errorMsg.contains("UserNotFound", ignoreCase = true)
                            || errorMsg.contains("User does not exist", ignoreCase = true)

                    // If user does not exist, fall back to SignUp
                    if (isUserNotFound) {
                        val signUpResult = authRepository.signUp(formattedUsername)
                        signUpResult.fold(
                            onSuccess = {
                                _status.value = AuthStateStatus.OTP_REQUIRED_SIGN_UP
                            },
                            onFailure = { signUpEx ->
                                _errorMessage.value = "Đăng ký thất bại: \${signUpEx.localizedMessage}"
                                _status.value = AuthStateStatus.ERROR
                            }
                        )
                    } else {
                        _errorMessage.value = "Đăng nhập thất bại: \${signInException.localizedMessage}"
                        _status.value = AuthStateStatus.ERROR
                    }
                }
            )
        }
    }

    fun verifyOtp(otp: String) {
        if (otp.length < 6) return
        
        val currentStatus = _status.value
        _status.value = AuthStateStatus.LOADING
        _errorMessage.value = null
        
        viewModelScope.launch {
            if (currentStatus == AuthStateStatus.OTP_REQUIRED_SIGN_UP) {
                val result = authRepository.confirmSignUp(_emailOrPhone.value, otp)
                result.fold(
                    onSuccess = {
                        // After confirm SignUp, user needs to SignIn to get tokens.
                        // Ideally we auto-login here.
                        checkAccountAndSendOtp() 
                    },
                    onFailure = { ex ->
                        _errorMessage.value = "Xác thực thất bại: \${ex.localizedMessage}"
                        _status.value = AuthStateStatus.OTP_REQUIRED_SIGN_UP
                    }
                )
            } else if (currentStatus == AuthStateStatus.OTP_REQUIRED_SIGN_IN) {
                val result = authRepository.confirmSignIn(otp)
                result.fold(
                    onSuccess = {
                        _status.value = AuthStateStatus.AUTHENTICATED
                    },
                    onFailure = { ex ->
                        _errorMessage.value = "Xác thực thất bại: \${ex.localizedMessage}"
                        _status.value = AuthStateStatus.OTP_REQUIRED_SIGN_IN
                    }
                )
            } else {
                _status.value = AuthStateStatus.ERROR
                _errorMessage.value = "Trạng thái không hợp lệ."
            }
        }
    }
}