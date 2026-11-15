package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

val LocalChatUnreadCount = compositionLocalOf { 0 }

@Composable
fun ChatAvatar(name: String, avatarUrl: String?, modifier: Modifier = Modifier) {
    val photo = avatarUrl.orEmpty().split(",").firstOrNull { it.isNotBlank() }?.trim()
    Box(modifier.clip(CircleShape).background(Color(0xFFE3EAFF)), contentAlignment = Alignment.Center) {
        Text(name.trim().take(1).uppercase().ifBlank { "H" }, color = Color(0xFF2962FF))
        if (photo != null) {
            AsyncImage(
                model = photo,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun chatTime(value: String?): String {
    if (value.isNullOrBlank()) return ""
    return runCatching {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        parser.timeZone = TimeZone.getTimeZone("UTC")
        parser.isLenient = false
        val date = parser.parse(value) ?: return ""
        DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }.getOrDefault("")
}
