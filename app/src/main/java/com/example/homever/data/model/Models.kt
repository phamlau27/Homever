package com.example.homever.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("id")
    val id: String = "",
    @SerialName("email")
    val email: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("full_name")
    val fullName: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("gender")
    val gender: String = "",
    @SerialName("age")
    val age: Int? = null,
    @SerialName("occupation")
    val occupation: String = "", // Nghề nghiệp hoặc trường học
    @SerialName("bio")
    val bio: String = "",
    @SerialName("need_type")
    val needType: String = "", // "ROOMMATE" (tìm bạn cùng phòng) hoặc "ROOM" (tìm phòng)
    @SerialName("min_budget")
    val minBudget: Double = 0.0,
    @SerialName("max_budget")
    val maxBudget: Double = 0.0,
    @SerialName("target_locations")
    val targetLocations: List<String> = emptyList(),
    @SerialName("lifestyle_tags")
    val lifestyleTags: List<String> = emptyList()
)

@Serializable
data class RoomPost(
    @SerialName("id")
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("price")
    val price: Double = 0.0,
    @SerialName("location")
    val location: String = "",
    @SerialName("area_sqm")
    val areaSqm: Double = 0.0,
    @SerialName("max_occupants")
    val maxOccupants: Int = 2,
    @SerialName("image_urls")
    val imageUrls: List<String> = emptyList(),
    @SerialName("amenities")
    val amenities: List<String> = emptyList(),
    @SerialName("is_available")
    val isAvailable: Boolean = true
)

@Serializable
data class UserInteraction(
    @SerialName("id")
    val id: String = "",
    @SerialName("from_user_id")
    val fromUserId: String = "",
    @SerialName("to_user_id")
    val toUserId: String = "",
    @SerialName("action")
    val action: String = "LIKE" // "LIKE" hoặc "DISLIKE"
)

@Serializable
data class UserMatch(
    @SerialName("id")
    val id: String = "",
    @SerialName("user_id_1")
    val userId1: String = "",
    @SerialName("user_id_2")
    val userId2: String = "",
    @SerialName("is_matched")
    val isMatched: Boolean = true
)
