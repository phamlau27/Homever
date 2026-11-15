package com.example.homever.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homever.data.remote.SupabaseClient
import com.example.homever.data.repository.AuthRepository
import com.example.homever.ui.screens.*
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // --- SPLASH SCREEN ---
        composable(Screen.Splash.route) {
            val scope = rememberCoroutineScope()
            SplashScreen(
                onSplashFinished = {
                    scope.launch {
                        val authRepo = AuthRepository()
                        val isLoggedIn = authRepo.isUserLoggedIn()
                        val nextRoute = if (isLoggedIn) Screen.Discover.route else Screen.Onboarding.route
                        navController.navigate(nextRoute) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // --- AUTH & ONBOARDING FLOW ---
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onStartClick = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onContinueClick = {
                    navController.navigate(Screen.Needs.route)
                },
                onGoogleLoginClick = {
                    navController.navigate(Screen.Discover.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Otp.route) {
            OtpScreen(
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { _ ->
                    navController.navigate(Screen.Discover.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Needs.route) {
            NeedsScreen(
                onBack = { navController.popBackStack() },
                onNext = { _ -> navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onComplete = { _, _, _, _ -> navController.navigate(Screen.Budget.route) }
            )
        }

        composable(Screen.Budget.route) {
            BudgetScreen(
                onBack = { navController.popBackStack() },
                onNext = { _ -> navController.navigate(Screen.Lifestyle.route) }
            )
        }

        composable(Screen.Lifestyle.route) {
            LifestyleScreen(
                onBack = { navController.popBackStack() },
                onNext = { _ -> navController.navigate(Screen.LocationSearch.route) }
            )
        }

        composable(Screen.LocationSearch.route) {
            LocationSearchScreen(
                onBack = { navController.popBackStack() },
                onNext = { _ -> navController.navigate(Screen.ReviewProfile.route) }
            )
        }

        composable(Screen.ReviewProfile.route) {
            ReviewProfileScreen(
                onBack = { navController.popBackStack() },
                onNext = {
                    navController.navigate(Screen.Discover.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // --- MAIN APP TABS ---
        composable(Screen.Discover.route) {
            DiscoverScreen(
                onNavigateToProfile = { navController.navigate(Screen.PersonalProfile.route) },
                onNavigateToRoom = { navController.navigate(Screen.RoomBrowsing.route) },
                onNavigateToMessages = { navController.navigate(Screen.Messages.route) }
            )
        }

        composable(Screen.RoomBrowsing.route) {
            RoomBrowsingScreen(
                onNavigateToDiscover = { navController.navigate(Screen.Discover.route) },
                onNavigateToMessages = { navController.navigate(Screen.Messages.route) },
                onNavigateToProfile = { navController.navigate(Screen.PersonalProfile.route) }
            )
        }

        composable(Screen.Invites.route) {
            InvitesScreen()
        }

        composable(Screen.Messages.route) {
            MessagesScreen(
                onNavigateToDiscover = { navController.navigate(Screen.Discover.route) },
                onNavigateToRoom = { navController.navigate(Screen.RoomBrowsing.route) },
                onNavigateToProfile = { navController.navigate(Screen.PersonalProfile.route) }
            )
        }

        composable(Screen.PersonalProfile.route) {
            PersonalProfileScreen(
                onSettingsClick = { navController.navigate(Screen.AccountSettings.route) },
                onNavigateToDiscover = { navController.navigate(Screen.Discover.route) },
                onNavigateToRoom = { navController.navigate(Screen.RoomBrowsing.route) },
                onNavigateToMessages = { navController.navigate(Screen.Messages.route) }
            )
        }

        // --- SUB SCREENS ---
        composable(Screen.Match.route) {
            MatchScreen()
        }

        composable(Screen.AccountSettings.route) {
            AccountSettingsScreen(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 5 Màn hình bổ sung mới
        composable(Screen.RoomDetail.route) {
            RoomDetailScreen(
                onBack = { navController.popBackStack() },
                onChatWithOwner = { navController.navigate(Screen.DirectChat.route) }
            )
        }

        composable(Screen.RoomFilter.route) {
            RoomFilterScreen(
                onClose = { navController.popBackStack() },
                onApplyFilter = { navController.popBackStack() }
            )
        }

        composable(Screen.DirectChat.route) {
            DirectChatScreen(
                onBack = { navController.popBackStack() },
                onViewProfile = { navController.navigate(Screen.UserProfileDetail.route) }
            )
        }

        composable(Screen.UserProfileDetail.route) {
            UserProfileDetailScreen(
                onBack = { navController.popBackStack() },
                onStartChat = { navController.navigate(Screen.DirectChat.route) },
                onReportUser = { navController.navigate(Screen.ReportBlock.route) }
            )
        }

        composable(Screen.ReportBlock.route) {
            ReportBlockScreen(
                onBack = { navController.popBackStack() },
                onSubmitSuccess = { navController.popBackStack() }
            )
        }
    }
}
