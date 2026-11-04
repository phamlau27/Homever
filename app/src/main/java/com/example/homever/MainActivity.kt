package com.example.homever

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.homever.auth.CognitoAuthManager
import com.example.homever.ui.screens.LoginScreen
import com.example.homever.ui.theme.HomEverTheme

import com.example.homever.ui.screens.ProfileScreen
import com.example.homever.ui.screens.OtpScreen
import androidx.navigation.compose.NavHost
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.NavHost
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homever.ui.screens.LoginScreen
import com.example.homever.ui.screens.OtpScreen
import com.example.homever.ui.screens.ProfileScreen
import com.example.homever.ui.screens.DiscoverScreen
import com.example.homever.viewmodel.ProfileViewModel
import com.example.homever.viewmodel.ProfileStatus
import com.example.homever.viewmodel.AuthStateStatus
import com.example.homever.viewmodel.AuthViewModel
import com.example.homever.domain.repository.AuthRepository
import com.example.homever.data.auth.AuthRepositoryImpl
import com.example.homever.data.session.SessionManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.example.homever.domain.repository.ProfileRepository
import com.example.homever.data.profile.ProfileRepositoryImpl
import com.example.homever.data.network.HomeverApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var authManager: CognitoAuthManager

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        if (data != null) {
            authManager.handleLoginResult(data) { success ->
                if (success) {
                    // Update auth state so UI will observe and trigger profile check
                    // We need a way to tell the AuthViewModel that auth succeeded from AppAuth.
                    // Instead of a direct Toast/Restart, let's signal AuthViewModel.
                    val authViewModel: AuthViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
                    authViewModel.setAuthenticated()
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        authManager = CognitoAuthManager(this)
        val sessionManager = SessionManager(this)
        val authRepository: AuthRepository = AuthRepositoryImpl(sessionManager)
        
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Replace with actual backend port if different
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val homeverApi = retrofit.create(HomeverApi::class.java)
        val profileRepository: ProfileRepository = ProfileRepositoryImpl(homeverApi, this)

        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    return AuthViewModel(authRepository) as T
                }
                if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                    return ProfileViewModel(profileRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        
        enableEdgeToEdge()
        setContent {
            HomEverTheme {
                val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
                val profileViewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
                
                val authStatus = authViewModel.status.collectAsState().value
                val authErrorMessage = authViewModel.errorMessage.collectAsState().value
                
                val profileStatus = profileViewModel.status.collectAsState().value
                val profileErrorMessage = profileViewModel.errorMessage.collectAsState().value
                val profileAvatarUri = profileViewModel.avatarUri.collectAsState().value
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()
                        
                        // Determine start destination
                        val startDest = if (authRepository.isLoggedIn()) {
                            if (sessionManager.isProfileCompleted()) "home" else "profile"
                        } else {
                            "login"
                        }
                        
                        NavHost(navController = navController, startDestination = startDest) {
                            composable("login") {
                                LoginScreen(
                                    isLoading = authStatus == AuthStateStatus.LOADING,
                                    errorMessage = authErrorMessage,
                                    onContinueClick = { email, countryCode ->
                                        authViewModel.updateEmailOrPhone(email)
                                        authViewModel.checkAccountAndSendOtp(countryCode)
                                    },
                                    onGoogleLoginClick = {
                                        // AppAuth will redirect to Google via Cognito
                                        authManager.startLogin(loginLauncher, identityProvider = "Google")
                                    }
                                )

                                LaunchedEffect(authStatus) {
                                    when (authStatus) {
                                        AuthStateStatus.OTP_REQUIRED_SIGN_UP,
                                        AuthStateStatus.OTP_REQUIRED_SIGN_IN -> {
                                            navController.navigate("otp") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                        AuthStateStatus.AUTHENTICATED -> {
                                            // Call backend to check if profile exists
                                            profileViewModel.checkProfileExists { exists ->
                                                if (exists) {
                                                    sessionManager.saveProfileCompleted(true)
                                                    navController.navigate("home") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                } else {
                                                    sessionManager.saveProfileCompleted(false)
                                                    navController.navigate("profile") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                }
                                            }
                                        }
                                        else -> {}
                                    }
                                }
                            }
                            composable("otp") {
                                OtpScreen(
                                    isLoading = authStatus == AuthStateStatus.LOADING,
                                    errorMessage = authErrorMessage,
                                    onBackClick = { 
                                        authViewModel.clearError()
                                        navController.navigate("login") {
                                            popUpTo("otp") { inclusive = true }
                                        }
                                    },
                                    onResendClick = {
                                        authViewModel.checkAccountAndSendOtp()
                                    },
                                    onConfirmClick = { otp ->
                                        authViewModel.verifyOtp(otp)
                                    }
                                )

                                LaunchedEffect(authStatus) {
                                    if (authStatus == AuthStateStatus.AUTHENTICATED) {
                                        profileViewModel.checkProfileExists { exists ->
                                            if (exists) {
                                                sessionManager.saveProfileCompleted(true)
                                                navController.navigate("home") {
                                                    popUpTo("otp") { inclusive = true }
                                                }
                                            } else {
                                                sessionManager.saveProfileCompleted(false)
                                                navController.navigate("profile") {
                                                    popUpTo("otp") { inclusive = true }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            composable("profile") {
                                ProfileScreen(
                                    isLoading = profileStatus == ProfileStatus.LOADING,
                                    errorMessage = profileErrorMessage,
                                    avatarUri = profileAvatarUri,
                                    onAvatarSelected = { uri ->
                                        profileViewModel.updateAvatarUri(uri)
                                    },
                                    onComplete = { name, gender, age, occ ->
                                        profileViewModel.completeProfile(name, gender, age, occ) {
                                            sessionManager.saveProfileCompleted(true)
                                            navController.navigate("home") {
                                                popUpTo("profile") { inclusive = true }
                                            }
                                        }
                                    }
                                )
                            }
                            composable("home") {
                                DiscoverScreen() // Main Home screen after login
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authManager.dispose()
    }
}