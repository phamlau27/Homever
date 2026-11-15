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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homever.auth.CognitoAuthManager
import com.example.homever.data.auth.AuthRepositoryImpl
import com.example.homever.data.network.NetworkModule
import com.example.homever.data.profile.ProfileRepositoryImpl
import com.example.homever.data.session.SessionManager
import com.example.homever.domain.repository.AuthRepository
import com.example.homever.domain.repository.ProfileRepository
import com.example.homever.ui.screens.*
import com.example.homever.ui.theme.HomEverTheme
import com.example.homever.viewmodel.AuthStateStatus
import com.example.homever.viewmodel.AuthViewModel
import com.example.homever.viewmodel.ProfileStatus
import com.example.homever.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {

    private lateinit var authManager: CognitoAuthManager
    private lateinit var sessionManager: SessionManager

    // ── AppAuth activity launchers ─────────────────────────────────────────
    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data ?: return@registerForActivityResult
            val authVm = ViewModelProvider(this, factory)[AuthViewModel::class.java]
            authVm.setLoading()
            authManager.handleLoginResult(data) { success, isCancelled ->
                if (success) {
                    val accessToken = authManager.getAccessToken()
                    val idToken = authManager.getIdToken() ?: ""
                    val refreshToken = authManager.getRefreshToken()
                    if (accessToken != null) {
                        sessionManager.saveTokens(accessToken, idToken, refreshToken)
                        Log.d("MainActivity", "Tokens saved after login")
                    }
                    authVm.setAuthenticated()
                } else if (isCancelled) {
                    authVm.clearError()
                } else {
                    authVm.setError("Đăng nhập thất bại. Vui lòng thử lại.")
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val logoutLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (data != null) {
                authManager.handleLogoutResult(data) { _ ->
                    restartToLogin()
                }
            } else {
                authManager.clearAuthState()
                restartToLogin()
            }
        }

    private lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── Infrastructure ──────────────────────────────────────────────
        authManager = CognitoAuthManager(this)
        sessionManager = SessionManager(this)

        val authRepository: AuthRepository = AuthRepositoryImpl(sessionManager)

        val homeverApi = NetworkModule.initialize(sessionManager, authManager)
        val profileRepository: ProfileRepository = ProfileRepositoryImpl(homeverApi)

        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
                modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                    AuthViewModel(authRepository) as T
                modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(profileRepository) as T
                else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
            }
        }

        enableEdgeToEdge()
        setContent {
            HomEverTheme {
                val authViewModel: AuthViewModel =
                    androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
                val profileViewModel: ProfileViewModel =
                    androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

                val authStatus by authViewModel.status.collectAsState()
                val authErrorMessage by authViewModel.errorMessage.collectAsState()

                val profileStatus by profileViewModel.status.collectAsState()
                val profileErrorMessage by profileViewModel.errorMessage.collectAsState()
                val profileAvatarUri by profileViewModel.avatarUri.collectAsState()

                // Collect StateFlow onboarding values
                val tempDisplayName by profileViewModel.tempDisplayName.collectAsState()
                val tempGender by profileViewModel.tempGender.collectAsState()
                val tempAge by profileViewModel.tempAge.collectAsState()
                val tempOccupation by profileViewModel.tempOccupation.collectAsState()
                val tempNeedType by profileViewModel.tempNeedType.collectAsState()
                val tempLifestyle by profileViewModel.tempLifestyle.collectAsState()
                val tempBudget by profileViewModel.tempBudget.collectAsState()
                val tempLocation by profileViewModel.tempLocation.collectAsState()
                val tempBio by profileViewModel.tempBio.collectAsState()

                val isAuthReady by authManager.isReady.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()

                        val startDest = if (authRepository.isLoggedIn() &&
                            sessionManager.isProfileCompleted()
                        ) "home" else "login"

                        NavHost(
                            navController = navController,
                            startDestination = startDest
                        ) {

                            // ── Login ───────────────────────────────────────────────
                            composable("login") {
                                val isCheckingProfile = authStatus == AuthStateStatus.AUTHENTICATED && profileStatus != ProfileStatus.ERROR && profileStatus != ProfileStatus.UNAUTHORIZED
                                val isLoginLoading = authStatus == AuthStateStatus.LOADING || isCheckingProfile || profileStatus == ProfileStatus.LOADING

                                LoginScreen(
                                    isLoading = isLoginLoading,
                                    isAuthReady = isAuthReady,
                                    errorMessage = authErrorMessage ?: profileErrorMessage,
                                    onContinueClick = {
                                        authManager.startLogin(loginLauncher)
                                    },
                                    onGoogleLoginClick = {
                                        authManager.startLogin(
                                            loginLauncher,
                                            identityProvider = "Google"
                                        )
                                    }
                                )

                                LaunchedEffect(authStatus) {
                                    if (authStatus == AuthStateStatus.AUTHENTICATED) {
                                        profileViewModel.checkProfileExists { exists, isUnauthorized ->
                                            when {
                                                isUnauthorized -> {
                                                    sessionManager.clearSession()
                                                    authManager.clearAuthState()
                                                    authViewModel.signOut()
                                                    Toast.makeText(
                                                        this@MainActivity,
                                                        "Phiên đăng nhập đã hết hạn",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                exists == true -> {
                                                    sessionManager.saveProfileCompleted(true)
                                                    navController.navigate("home") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                }
                                                exists == false -> {
                                                    sessionManager.saveProfileCompleted(false)
                                                    navController.navigate("profile") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                }
                                                else -> {
                                                    Toast.makeText(
                                                        this@MainActivity,
                                                        "Không thể kết nối máy chủ. Vui lòng thử lại.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ── Onboarding: Step 1 — Basic Profile ─────────────────
                            composable("profile") {
                                ProfileScreen(
                                    isLoading = profileStatus == ProfileStatus.LOADING,
                                    errorMessage = profileErrorMessage,
                                    avatarUri = profileAvatarUri,
                                    initialDisplayName = tempDisplayName,
                                    initialGender = tempGender,
                                    initialAge = tempAge,
                                    initialOccupation = tempOccupation,
                                    onAvatarSelected = { uri ->
                                        profileViewModel.updateAvatarUri(uri)
                                    },
                                    onBack = {
                                        authViewModel.signOut()
                                        sessionManager.clearSession()
                                        authManager.clearAuthState()
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    },
                                    onComplete = { name, gender, age, occ ->
                                        profileViewModel.saveBasicProfileLocally(
                                            name, gender, age, occ
                                        )
                                        navController.navigate("needs")
                                    }
                                )
                            }

                            // ── Onboarding: Step 2 — Needs ─────────────────────────
                            composable("needs") {
                                NeedsScreen(
                                    initialSelection = tempNeedType,
                                    onBack = { navController.popBackStack() },
                                    onNext = { needType ->
                                        profileViewModel.saveNeedLocally(needType)
                                        navController.navigate("lifestyle")
                                    }
                                )
                            }

                            // ── Onboarding: Step 3 — Lifestyle ─────────────────────
                            composable("lifestyle") {
                                LifestyleScreen(
                                    onBack = { navController.popBackStack() },
                                    onNext = { lifestyle ->
                                        profileViewModel.saveLifestyleLocally(lifestyle)
                                        navController.navigate("budget")
                                    }
                                )
                            }

                            // ── Onboarding: Step 4a — Budget ───────────────────────
                            composable("budget") {
                                BudgetScreen(
                                    initialBudget = tempBudget,
                                    onBack = { navController.popBackStack() },
                                    onNext = { budget ->
                                        profileViewModel.saveBudgetAndLocationLocally(
                                            budget = budget,
                                            location = tempLocation
                                        )
                                        navController.navigate("location")
                                    }
                                )
                            }

                            // ── Onboarding: Step 4b — Location ─────────────────────
                            composable("location") {
                                LocationSearchScreen(
                                    initialLocation = tempLocation,
                                    onBack = { navController.popBackStack() },
                                    onNext = { location ->
                                        profileViewModel.saveBudgetAndLocationLocally(
                                            budget = tempBudget,
                                            location = location
                                        )
                                        navController.navigate("review")
                                    }
                                )
                            }

                            // ── Onboarding: Step 5 — Review & Submit ───────────────
                            composable("review") {
                                ReviewProfileScreen(
                                    displayName = tempDisplayName,
                                    gender = tempGender,
                                    age = tempAge,
                                    occupation = tempOccupation,
                                    needType = tempNeedType,
                                    lifestyle = tempLifestyle,
                                    budget = tempBudget,
                                    location = tempLocation,
                                    bio = tempBio,
                                    isLoading = profileStatus == ProfileStatus.LOADING,
                                    errorMessage = profileErrorMessage,
                                    onBack = { navController.popBackStack() },
                                    onNext = {
                                        profileViewModel.completeProfile {
                                            sessionManager.saveProfileCompleted(true)
                                            navController.navigate("home") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    }
                                )
                            }

                            // ── Home (Discover) ─────────────────────────────────────
                            composable("home") {
                                DiscoverScreen(
                                    onNavigateToProfile = {
                                        navController.navigate("personal_profile")
                                    },
                                    onNavigateToRoom = {
                                        navController.navigate("room")
                                    },
                                    onNavigateToMessages = {
                                        navController.navigate("messages")
                                    }
                                )
                            }

                            // ── Room Browsing ───────────────────────────────────────
                            composable("room") {
                                RoomBrowsingScreen(
                                    onNavigateToDiscover = {
                                        navController.navigate("home")
                                    },
                                    onNavigateToMessages = {
                                        navController.navigate("messages")
                                    },
                                    onNavigateToProfile = {
                                        navController.navigate("personal_profile")
                                    }
                                )
                            }

                            // ── Messages ────────────────────────────────────────────
                            composable("messages") {
                                MessagesScreen(
                                    onNavigateToDiscover = {
                                        navController.navigate("home")
                                    },
                                    onNavigateToRoom = {
                                        navController.navigate("room")
                                    },
                                    onNavigateToProfile = {
                                        navController.navigate("personal_profile")
                                    }
                                )
                            }

                            // ── Personal Profile ────────────────────────────────────
                            composable("personal_profile") {
                                PersonalProfileScreen(
                                    displayName = tempDisplayName.ifBlank { "Người dùng" },
                                    age = tempAge,
                                    occupation = tempOccupation,
                                    bio = tempBio,
                                    onSettingsClick = {
                                        navController.navigate("account_settings")
                                    },
                                    onNavigateToDiscover = {
                                        navController.navigate("home")
                                    },
                                    onNavigateToRoom = {
                                        navController.navigate("room")
                                    },
                                    onNavigateToMessages = {
                                        navController.navigate("messages")
                                    }
                                )
                            }

                            // ── Account Settings / Logout ───────────────────────────
                            composable("account_settings") {
                                AccountSettingsScreen(
                                    onBackClick = { navController.popBackStack() },
                                    onLogoutClick = {
                                        authViewModel.signOut()
                                        sessionManager.clearSession()
                                        NetworkModule.reset()

                                        authManager.startLogout(
                                            launcher = logoutLauncher,
                                            onLocalFallback = {
                                                navController.navigate("login") {
                                                    popUpTo(0) { inclusive = true }
                                                }
                                            }
                                        )
                                    }
                                )
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

    private fun restartToLogin() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finish()
    }
}
