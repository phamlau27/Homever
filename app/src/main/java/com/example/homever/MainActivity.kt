package com.example.homever

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.homever.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homever.auth.CognitoAuthManager
import com.example.homever.data.auth.AuthRepositoryImpl
import com.example.homever.data.network.NetworkModule
import com.example.homever.data.network.UserProfile
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
import com.example.homever.viewmodel.DiscoveryViewModel

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
                    ViewModelProvider(this, factory)[DiscoveryViewModel::class.java].reset()
                    ViewModelProvider(this, factory)[ChatViewModel::class.java].reset()
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

    private var localNetworkPermissionResult: ((Boolean) -> Unit)? = null

    private val localNetworkPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val callback = localNetworkPermissionResult
            localNetworkPermissionResult = null
            callback?.invoke(granted)
            if (!granted && !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_LOCAL_NETWORK)) {
                AlertDialog.Builder(this)
                    .setMessage("Hãy cho phép truy cập mạng nội bộ trong phần Quyền của ứng dụng để kết nối máy chủ.")
                    .setPositiveButton("Mở cài đặt") { _, _ ->
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)))
                    }
                    .setNegativeButton("Đóng", null)
                    .show()
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
        val profileRepository: ProfileRepository = ProfileRepositoryImpl(homeverApi, applicationContext)

        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
                modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                    AuthViewModel(authRepository) as T
                modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(profileRepository) as T
                modelClass.isAssignableFrom(DiscoveryViewModel::class.java) ->
                    DiscoveryViewModel(homeverApi) as T
                modelClass.isAssignableFrom(ChatViewModel::class.java) ->
                    ChatViewModel(homeverApi) as T
                else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
            }
        }

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced = false
        setContent {
            HomEverTheme {
                val authViewModel: AuthViewModel =
                    androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
                val profileViewModel: ProfileViewModel =
                    androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

                val discoveryViewModel: DiscoveryViewModel =
                    androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
                val discoveryState by discoveryViewModel.state.collectAsState()
                val chatViewModel: ChatViewModel =
                    androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
                val chatState by chatViewModel.state.collectAsState()
                val lifecycleOwner = LocalLifecycleOwner.current

                val authStatus by authViewModel.status.collectAsState()
                val authErrorMessage by authViewModel.errorMessage.collectAsState()

                val profileStatus by profileViewModel.status.collectAsState()
                val profileErrorMessage by profileViewModel.errorMessage.collectAsState()
                val profileAvatarUri by profileViewModel.avatarUri.collectAsState()
                val currentProfile by profileViewModel.currentProfile.collectAsState()

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
                val tempTraits by profileViewModel.tempTraits.collectAsState()
                val tempPhotos by profileViewModel.tempPhotos.collectAsState()

                val isAuthReady by authManager.isReady.collectAsState()

                CompositionLocalProvider(LocalChatUnreadCount provides chatState.conversations.sumOf { it.unreadCount }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val navigateTab: (String) -> Unit = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    val openChat: (String) -> Unit = { id ->
                        if (!navController.popBackStack("direct_chat/$id", inclusive = false)) {
                            navController.navigate("direct_chat/$id") { launchSingleTop = true }
                        }
                    }
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.destination?.route
                    var selectedUserCandidate by remember { mutableStateOf<RoommateCandidate?>(null) }

                    LaunchedEffect(currentProfile?.id, authRepository.isLoggedIn(), lifecycleOwner) {
                        if (currentProfile?.id.isNullOrBlank() || !authRepository.isLoggedIn()) return@LaunchedEffect
                        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            while (isActive) {
                                chatViewModel.refreshConversations()
                                discoveryViewModel.refreshInterests()
                                delay(3500)
                            }
                        }
                    }
                    LaunchedEffect(discoveryState.matchedConversation?.id) {
                        discoveryState.matchedConversation?.let { connection ->
                            chatViewModel.acceptConnection(connection, currentProfile?.id.orEmpty())
                            discoveryViewModel.consumeMatch()
                        }
                    }
                    LaunchedEffect(chatState.conversations.map { it.profile.id }.toSet()) {
                        discoveryViewModel.applyConnections(chatState.conversations.map { it.profile.id }.toSet())
                    }
                    LaunchedEffect(chatState.pendingMatch?.id, currentRoute) {
                        val connection = chatState.pendingMatch
                        if (connection != null && currentRoute in setOf("home", "invites", "messages", "room", "personal_profile", "user_profile_detail")) {
                            navController.navigate("match/${connection.id}") { launchSingleTop = true }
                        }
                    }

                        val isLoggedIn = authRepository.isLoggedIn()
                        val isProfileCompleted = sessionManager.isProfileCompleted()

                        LaunchedEffect(Unit) {
                            if (authRepository.isLoggedIn()) {
                                withLocalNetworkAccess(profileViewModel::onLocalNetworkPermissionDenied) {
                                    profileViewModel.loadProfile { profileExists ->
                                        if (profileExists) {
                                            sessionManager.saveProfileCompleted(true)
                                        }
                                    }
                                }
                            }
                        }

                        NavHost(
                            navController = navController,
                            startDestination = "splash"
                        ) {

                            // ── Splash Screen (01-splash) ───────────────────────────
                            composable("splash") {
                                SplashScreen(
                                    onSplashFinished = {
                                        val dest = when {
                                            isLoggedIn && isProfileCompleted -> "home"
                                            isLoggedIn && !isProfileCompleted -> "profile"
                                            else -> "onboarding"
                                        }
                                        navController.navigate(dest) {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            // ── Onboarding Screen (02-onboarding) ───────────────────
                            composable("onboarding") {
                                OnboardingScreen(
                                    onStartClick = {
                                        navController.navigate("login")
                                    }
                                )
                            }

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
                                        withLocalNetworkAccess(profileViewModel::onLocalNetworkPermissionDenied) {
                                            profileViewModel.checkProfileExists { exists, isUnauthorized ->
                                                when {
                                                    isUnauthorized -> {
                                                        discoveryViewModel.reset()
                                                        chatViewModel.reset()
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
                                    onRetry = {
                                        withLocalNetworkAccess(profileViewModel::onLocalNetworkPermissionDenied) {
                                            profileViewModel.loadProfile { profileExists ->
                                                if (profileExists) {
                                                    sessionManager.saveProfileCompleted(true)
                                                    navController.navigate("home") {
                                                        popUpTo("profile") { inclusive = true }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    onBack = {
                                        // Do NOT clear Cognito session tokens. Allow user to navigate back safely.
                                        if (!navController.popBackStack()) {
                                            finish()
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
                                    avatarUri = profileAvatarUri,
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
                                LaunchedEffect(Unit) {
                                    discoveryViewModel.loadCandidates()
                                    discoveryViewModel.loadInterests()
                                }
                                DiscoverScreen(
                                    state = discoveryState,
                                    onRefresh = { discoveryViewModel.loadCandidates(force = true) },
                                    onPrevious = discoveryViewModel::previous,
                                    onSkip = discoveryViewModel::skip,
                                    onInterest = discoveryViewModel::sendInterest,
                                    onPageChange = discoveryViewModel::selectPage,
                                    onNavigateToProfile = {
                                        navigateTab("personal_profile")
                                    },
                                    onNavigateToRoom = {
                                        navigateTab("room")
                                    },
                                    onNavigateToInvites = {
                                        navigateTab("invites")
                                    },
                                    onNavigateToMessages = {
                                        navigateTab("messages")
                                    },
                                    onNavigateToUserDetail = { candidate ->
                                        selectedUserCandidate = candidate
                                        navController.navigate("user_profile_detail")
                                    }
                                )
                            }

                            // ── User Profile Detail (View other candidate or preview) ─
                            composable("user_profile_detail") {
                                val isOwnPreview = selectedUserCandidate == null
                                val candidate = selectedUserCandidate ?: UserProfile(
                                    displayName = tempDisplayName.ifBlank { "Người dùng" },
                                    age = tempAge.toIntOrNull() ?: 0,
                                    occupation = tempOccupation,
                                    location = tempLocation,
                                    budget = tempBudget,
                                    bio = tempBio,
                                    lifestyle = tempLifestyle,
                                    traits = tempTraits,
                                    avatarUrl = currentProfile?.avatarUrl ?: profileAvatarUri?.toString()
                                ).asRoommateCandidate()
                                val initialPage = if (!isOwnPreview &&
                                    candidate.id == discoveryState.candidates.getOrNull(discoveryState.currentIndex)?.profile?.id
                                ) discoveryState.cardPage else 0
                                UserProfileDetailScreen(
                                    name = candidate.name,
                                    age = candidate.age,
                                    occupation = candidate.occupation,
                                    location = candidate.location,
                                    budget = candidate.budget,
                                    bio = candidate.bio,
                                    lifestyle = candidate.lifestyle.orEmpty(),
                                    traits = candidate.traits.orEmpty(),
                                    tags = candidate.tags,
                                    avatarUrl = candidate.avatarUrl,
                                    photos = candidate.photos,
                                    matchPercentage = candidate.matchPercentage,
                                    isOwnPreview = isOwnPreview,
                                    initialPage = initialPage,
                                    onBack = { navController.popBackStack() },
                                    onStartChat = {
                                        val connection = chatState.conversations.firstOrNull { it.profile.id == candidate.id }
                                        if (connection != null) {
                                            openChat(connection.id)
                                        } else {
                                            Toast.makeText(this@MainActivity, "Hai bạn cần cùng quan tâm để bắt đầu nhắn tin.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            }

                            // ── Invites (Quan tâm) ─────────────────────────────────
                            composable("invites") {
                                LaunchedEffect(Unit) { discoveryViewModel.loadInterests() }
                                InvitesScreen(
                                    state = discoveryState,
                                    onRetry = discoveryViewModel::loadInterests,
                                    onInterest = discoveryViewModel::sendInterest,
                                    onNavigateToDiscover = { navigateTab("home") },
                                    onNavigateToRoom = { navigateTab("room") },
                                    onNavigateToMessages = { navigateTab("messages") },
                                    onNavigateToProfile = { navigateTab("personal_profile") },
                                    onViewProfile = { profile ->
                                        selectedUserCandidate = profile.asRoommateCandidate()
                                        navController.navigate("user_profile_detail")
                                    }
                                )
                            }

                            // ── Room Browsing ───────────────────────────────────────
                            composable("room") {
                                RoomBrowsingScreen(
                                    onNavigateToInvites = { navigateTab("invites") },
                                    onNavigateToDiscover = {
                                        navigateTab("home")
                                    },
                                    onNavigateToMessages = {
                                        navigateTab("messages")
                                    },
                                    onNavigateToProfile = {
                                        navigateTab("personal_profile")
                                    }
                                )
                            }

                            // ── Messages ────────────────────────────────────────────
                            composable("messages") {
                                MessagesScreen(
                                    state = chatState,
                                    onRetry = chatViewModel::refresh,
                                    onOpenConversation = openChat,
                                    onNavigateToInvites = { navigateTab("invites") },
                                    onNavigateToDiscover = { navigateTab("home") },
                                    onNavigateToRoom = { navigateTab("room") },
                                    onNavigateToProfile = { navigateTab("personal_profile") }
                                )
                            }

                            composable("match/{conversationId}") { entry ->
                                val id = entry.arguments?.getString("conversationId") ?: return@composable
                                val connection = chatState.conversations.firstOrNull { it.id == id }
                                val dismissMatch: () -> Unit = {
                                    chatViewModel.acknowledgeMatch(id)
                                    navController.popBackStack()
                                    navigateTab("home")
                                }
                                BackHandler(onBack = dismissMatch)
                                if (connection != null) {
                                    MatchScreen(
                                        myName = tempDisplayName,
                                        myAvatarUrl = currentProfile?.avatarUrl ?: profileAvatarUri?.toString(),
                                        otherName = connection.profile.displayName,
                                        otherAvatarUrl = connection.profile.avatarUrl,
                                        onContinueDiscover = dismissMatch,
                                        onStartChat = {
                                            chatViewModel.acknowledgeMatch(id)
                                            navController.popBackStack()
                                            openChat(id)
                                        }
                                    )
                                } else {
                                    Column(Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp)) {
                                        Text("Đang tải kết nối…")
                                        TextButton(onClick = dismissMatch) { Text("Quay lại") }
                                    }
                                }
                            }

                            composable("direct_chat/{conversationId}") { entry ->
                                val id = entry.arguments?.getString("conversationId") ?: return@composable
                                val connection = chatState.conversations.firstOrNull { it.id == id }
                                LaunchedEffect(id, lifecycleOwner) {
                                    chatViewModel.openConversation(id)
                                    try {
                                        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                            while (isActive) {
                                                chatViewModel.refreshMessages(id)
                                                delay(2500)
                                            }
                                        }
                                    } finally {
                                        chatViewModel.closeConversation(id)
                                    }
                                }
                                val threadState = if (chatState.activeConversationId == id) chatState else chatState.copy(
                                    messages = emptyList(), draft = "", loadingMessages = true
                                )
                                DirectChatScreen(
                                    userName = connection?.profile?.displayName ?: "Cuộc trò chuyện",
                                    avatarUrl = connection?.profile?.avatarUrl,
                                    state = threadState,
                                    onBack = { navController.popBackStack() },
                                    onDraftChange = chatViewModel::updateDraft,
                                    onSend = chatViewModel::sendMessage,
                                    onLoadOlder = chatViewModel::loadOlderMessages,
                                    onRetry = chatViewModel::retryMessages,
                                    onViewProfile = {
                                        connection?.profile?.let { profile ->
                                            selectedUserCandidate = profile.asRoommateCandidate()
                                            navController.navigate("user_profile_detail")
                                        }
                                    }
                                )
                            }

                            // ── Personal Profile ────────────────────────────────────
                            composable("personal_profile") {
                                PersonalProfileScreen(
                                    onNavigateToInvites = { navigateTab("invites") },
                                    displayName = tempDisplayName.ifBlank { "Người dùng" },
                                    gender = tempGender,
                                    age = tempAge,
                                    occupation = tempOccupation,
                                    needType = tempNeedType,
                                    lifestyle = tempLifestyle,
                                    budget = tempBudget,
                                    location = tempLocation,
                                    bio = tempBio,
                                    traits = tempTraits,
                                    avatarUrl = currentProfile?.avatarUrl ?: profileAvatarUri?.toString(),
                                    isPhotoUploading = profileStatus == ProfileStatus.LOADING,
                                    onAddPhoto = { uri ->
                                        profileViewModel.uploadAndAddPhoto(uri) { success, _ ->
                                            if (!success) {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    profileErrorMessage ?: "Tải ảnh thất bại",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Đã thêm ảnh thành công!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                    onSaveNotes = { bioVal, lifestyleVal, traitsVal ->
                                        profileViewModel.saveIntroNotes(bioVal, lifestyleVal, traitsVal)
                                    },
                                    onPreviewClick = {
                                        selectedUserCandidate = null
                                        navController.navigate("user_profile_detail")
                                    },
                                    onEditProfileClick = {
                                        navController.navigate("profile")
                                    },
                                    onSettingsClick = {
                                        navController.navigate("account_settings")
                                    },
                                    onNavigateToDiscover = {
                                        navigateTab("home")
                                    },
                                    onNavigateToRoom = {
                                        navigateTab("room")
                                    },
                                    onNavigateToMessages = {
                                        navigateTab("messages")
                                    }
                                )
                            }

                            // ── Account Settings / Logout ───────────────────────────
                            composable("account_settings") {
                                AccountSettingsScreen(
                                    onBackClick = { navController.popBackStack() },
                                    onLogoutClick = {
                                        discoveryViewModel.reset()
                                        chatViewModel.reset()
                                        authViewModel.signOut()
                                        sessionManager.clearSession()
                                        NetworkModule.reset()

                                        authManager.startLogout(
                                            launcher = logoutLauncher,
                                            onLocalFallback = {
                                                navController.navigate("onboarding") {
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

    private fun withLocalNetworkAccess(onDenied: () -> Unit, onGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT < 37 ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCAL_NETWORK) == PackageManager.PERMISSION_GRANTED
        ) {
            onGranted()
            return
        }

        localNetworkPermissionResult = { granted ->
            if (granted) onGranted() else onDenied()
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_LOCAL_NETWORK)) {
            AlertDialog.Builder(this)
                .setMessage("Ứng dụng cần truy cập mạng nội bộ để kết nối backend trên máy tính của bạn.")
                .setPositiveButton("Tiếp tục") { _, _ ->
                    localNetworkPermissionLauncher.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
                }
                .setNegativeButton("Hủy") { _, _ ->
                    localNetworkPermissionResult = null
                    onDenied()
                }
                .setOnCancelListener {
                    localNetworkPermissionResult = null
                    onDenied()
                }
                .show()
        } else {
            localNetworkPermissionLauncher.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
        }
    }

    private fun restartToLogin() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finish()
    }
}
