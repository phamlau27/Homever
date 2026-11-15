package com.example.homever.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homever.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthStateStatus {
    IDLE, LOADING, AUTHENTICATED, ERROR
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

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _status.value = AuthStateStatus.IDLE
            _emailOrPhone.value = ""
            _errorMessage.value = null
        }
    }
    
    fun setAuthenticated() {
        _status.value = AuthStateStatus.AUTHENTICATED
    }

    fun setLoading() {
        _status.value = AuthStateStatus.LOADING
    }

    fun setError(message: String) {
        _errorMessage.value = message
        _status.value = AuthStateStatus.ERROR
    }

    // Native sign in / sign up logic has been removed.
    // Auth state is now managed directly by observing MainActivity's AppAuth result callbacks.
}