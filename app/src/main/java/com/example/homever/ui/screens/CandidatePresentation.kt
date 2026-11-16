package com.example.homever.ui.screens

import androidx.compose.ui.graphics.Color
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.model.LifestyleCriteria
import com.example.homever.domain.model.MatchingPreferenceCatalog

fun UserProfile.asRoommateCandidate(matchPercentage: Int = 0): RoommateCandidate =
    RoommateCandidate(
        id = id,
        name = displayName.ifBlank { "Người dùng" },
        age = age,
        occupation = occupation,
        location = location.orEmpty(),
        budget = MatchingPreferenceCatalog.budgetLabel(budget.orEmpty()),
        tags = LifestyleCriteria.profileLabels(lifestyle, gender),
        bio = bio.orEmpty(),
        matchPercentage = matchPercentage,
        gradientColors = listOf(Color(0xFF334155), Color(0xFF1E293B)),
        avatarUrl = avatarUrl,
        lifestyle = lifestyle,
        traits = traits,
        needType = needType,
        gender = gender
    )
