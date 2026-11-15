package com.example.homever.ui.screens

import androidx.compose.ui.graphics.Color
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.model.LifestyleCriteria

fun UserProfile.asRoommateCandidate(matchPercentage: Int = 0): RoommateCandidate =
    RoommateCandidate(
        id = id,
        name = displayName.ifBlank { "Người dùng" },
        age = age,
        occupation = occupation,
        location = location.orEmpty(),
        budget = budget.orEmpty(),
        tags = LifestyleCriteria.criterionLabels(lifestyle),
        bio = bio.orEmpty(),
        matchPercentage = matchPercentage,
        gradientColors = listOf(Color(0xFF334155), Color(0xFF1E293B)),
        avatarUrl = avatarUrl,
        lifestyle = lifestyle,
        traits = traits,
        needType = needType
    )
