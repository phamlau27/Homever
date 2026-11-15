package com.example.homever.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.homever.domain.model.LifestyleCriteria
import com.example.homever.domain.model.discoveryCardPages
import com.example.homever.ui.theme.BluePrimary

@Composable
fun ProfileDiscoveryCard(
    candidate: RoommateCandidate,
    page: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Homever",
    onBack: (() -> Unit)? = null,
    onOpenProfile: (() -> Unit)? = null,
    errorMessage: String? = null,
    actions: @Composable () -> Unit = {}
) {
    val candidatePhotos = (candidate.avatarUrl.orEmpty().split(",") + candidate.photos)
        .map(String::trim).filter(String::isNotBlank).distinct()
    val notes = listOf(
        "Giới thiệu" to candidate.bio,
        "Ở cùng mình" to LifestyleCriteria.note(candidate.lifestyle),
        "Roommate mong muốn" to candidate.traits.orEmpty()
    ).map { (title, text) -> title to LifestyleCriteria.displayText(text) }
    val pages = discoveryCardPages(candidatePhotos, candidate.tags, notes)
    val cardPage = page.coerceIn(pages.indices)
    val totalPages = pages.size
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val photoUrl = pages[cardPage].photo

            if (!photoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = candidate.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(candidate.gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = candidate.name.take(1).uppercase().ifBlank { "U" },
                                color = Color.White,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = candidate.name,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {
                        if (cardPage > 0) onPageChange(cardPage - 1)
                    }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {
                        if (cardPage < totalPages - 1) {
                            onPageChange(cardPage + 1)
                        }
                    }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .align(Alignment.TopStart)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(ProfArrowBackIcon, contentDescription = "Quay lại", tint = Color.White)
                    }
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }

            if (totalPages > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(totalPages) { idx ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (idx <= cardPage) Color.White else Color.White.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column {
                Text(
                    "${candidate.name}, ${candidate.age}",
                    modifier = if (onOpenProfile != null) Modifier.clickable(onClick = onOpenProfile) else Modifier,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    candidate.occupation,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Text(
                    "${candidate.location} · ${candidate.budget}",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                AnimatedContent(
                    targetState = cardPage,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "cardPageContent"
                ) { page ->
                    Column {
                        val content = pages[page.coerceIn(pages.indices)]
                        if (!content.note.isNullOrBlank()) {
                            Text(content.noteTitle.orEmpty(), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text(content.note, fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                            Spacer(Modifier.height(8.dp))
                        }
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            content.tags.forEach { tag -> TagPill(tag) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                errorMessage?.let { message ->
                    Text(message, color = Color.White, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                }

                actions()
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
