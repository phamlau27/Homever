package com.example.homever.navigation

sealed class Screen(val route: String) {
    // Splash screen
    object Splash : Screen("splash")

    // Auth & Onboarding flow
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object Otp : Screen("otp")
    object Needs : Screen("needs")
    object Profile : Screen("profile")
    object Budget : Screen("budget")
    object Lifestyle : Screen("lifestyle")
    object LocationSearch : Screen("location_search")
    object ReviewProfile : Screen("review_profile")

    // Main App Bottom Navigation tabs
    object Discover : Screen("discover")
    object RoomBrowsing : Screen("room_browsing")
    object Invites : Screen("invites")
    object Messages : Screen("messages")
    object PersonalProfile : Screen("personal_profile")

    // Sub-screens
    object Match : Screen("match")
    object AccountSettings : Screen("account_settings")
    object RoomDetail : Screen("room_detail")
    object RoomFilter : Screen("room_filter")
    object DirectChat : Screen("direct_chat")
    object UserProfileDetail : Screen("user_profile_detail")
    object ReportBlock : Screen("report_block")
}
