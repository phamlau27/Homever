package com.example.homever.ui.screens

import com.example.homever.domain.model.LifestyleCriteria

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.homever.ui.theme.*

private val ProfEditIcon: ImageVector
    get() = ImageVector.Builder(name = "Edit", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(HomeverTextGray)) {
            moveTo(3f, 17.25f); lineTo(3f, 21f); lineTo(6.75f, 21f); lineTo(17.81f, 9.94f); lineTo(14.06f, 6.19f); lineTo(3f, 17.25f); close()
            moveTo(20.71f, 7.04f); arcTo(0.996f, 0.996f, 0f, false, false, 20.71f, 5.63f); lineTo(18.37f, 3.29f); arcTo(0.996f, 0.996f, 0f, false, false, 16.96f, 3.29f); lineTo(15.13f, 5.12f); lineTo(18.88f, 8.87f); lineTo(20.71f, 7.04f); close()
        }
    }.build()

private val ProfSettingsIcon: ImageVector
    get() = ImageVector.Builder(name = "Settings", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(HomeverTextGray)) {
            moveTo(19.14f, 12.94f); curveTo(19.19f, 12.63f, 19.22f, 12.32f, 19.22f, 12f)
            curveTo(19.22f, 11.68f, 19.19f, 11.37f, 19.14f, 11.06f); lineTo(21.16f, 9.48f)
            curveTo(21.34f, 9.34f, 21.39f, 9.08f, 21.28f, 8.87f); lineTo(19.36f, 5.55f)
            curveTo(19.25f, 5.34f, 18.99f, 5.25f, 18.78f, 5.34f); lineTo(16.4f, 6.29f)
            curveTo(15.91f, 5.92f, 15.37f, 5.6f, 14.79f, 5.36f); lineTo(14.43f, 2.82f)
            curveTo(14.4f, 2.59f, 14.2f, 2.42f, 13.96f, 2.42f); lineTo(10.04f, 2.42f)
            curveTo(9.8f, 2.42f, 9.6f, 2.59f, 9.57f, 2.82f); lineTo(9.21f, 5.36f)
            curveTo(8.63f, 5.6f, 8.09f, 5.92f, 7.6f, 6.29f); lineTo(5.22f, 5.34f)
            curveTo(5.01f, 5.25f, 4.75f, 5.34f, 4.64f, 5.55f); lineTo(2.72f, 8.87f)
            curveTo(2.61f, 9.08f, 2.66f, 9.34f, 2.84f, 9.48f); lineTo(4.86f, 11.06f)
            curveTo(4.81f, 11.37f, 4.78f, 11.68f, 4.78f, 12f); curveTo(4.78f, 12.32f, 4.81f, 12.63f, 4.86f, 12.94f)
            lineTo(2.84f, 14.52f); curveTo(2.66f, 14.66f, 2.61f, 14.92f, 2.72f, 15.13f)
            lineTo(4.64f, 18.45f); curveTo(4.75f, 18.66f, 5.01f, 18.75f, 5.22f, 18.66f)
            lineTo(7.6f, 17.71f); curveTo(8.09f, 18.08f, 8.63f, 18.4f, 9.21f, 18.64f)
            lineTo(9.57f, 21.18f); curveTo(9.6f, 21.41f, 9.8f, 21.58f, 10.04f, 21.58f)
            lineTo(13.96f, 21.58f); curveTo(14.2f, 21.58f, 14.4f, 21.41f, 14.43f, 21.18f)
            lineTo(14.79f, 18.64f); curveTo(15.37f, 18.4f, 15.91f, 18.08f, 16.4f, 17.71f)
            lineTo(18.78f, 18.66f); curveTo(18.99f, 18.75f, 19.25f, 18.66f, 19.36f, 18.45f)
            lineTo(21.28f, 15.13f); curveTo(21.39f, 14.92f, 21.34f, 14.66f, 21.16f, 14.52f)
            lineTo(19.14f, 12.94f); close()
            moveTo(12f, 15.5f); curveTo(10.07f, 15.5f, 8.5f, 13.93f, 8.5f, 12f)
            curveTo(8.5f, 10.07f, 10.07f, 8.5f, 12f, 8.5f); curveTo(13.93f, 8.5f, 15.5f, 10.07f, 15.5f, 12f)
            curveTo(15.5f, 13.93f, 13.93f, 15.5f, 12f, 15.5f); close()
        }
    }.build()

private val PromptPencilIcon: ImageVector
    get() = ImageVector.Builder(name = "PromptPencil", defaultWidth = 18.dp, defaultHeight = 18.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF94A3B8))) {
            moveTo(3f, 17.25f); lineTo(3f, 21f); lineTo(6.75f, 21f); lineTo(17.81f, 9.94f); lineTo(14.06f, 6.19f); lineTo(3f, 17.25f); close()
            moveTo(20.71f, 7.04f); arcTo(0.996f, 0.996f, 0f, false, false, 20.71f, 5.63f); lineTo(18.37f, 3.29f); arcTo(0.996f, 0.996f, 0f, false, false, 16.96f, 3.29f); lineTo(15.13f, 5.12f); lineTo(18.88f, 8.87f); lineTo(20.71f, 7.04f); close()
        }
    }.build()

private val SparkleIcon: ImageVector
    get() = ImageVector.Builder(name = "Sparkle", defaultWidth = 16.dp, defaultHeight = 16.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF2563EB))) {
            moveTo(12f, 2f); lineTo(14.5f, 9.5f); lineTo(22f, 12f); lineTo(14.5f, 14.5f); lineTo(12f, 22f); lineTo(9.5f, 14.5f); lineTo(2f, 12f); lineTo(9.5f, 9.5f); close()
        }
    }.build()

private val HouseIcon: ImageVector
    get() = ImageVector.Builder(name = "House", defaultWidth = 16.dp, defaultHeight = 16.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF059669))) {
            moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close()
        }
    }.build()

private val PeopleIcon: ImageVector
    get() = ImageVector.Builder(name = "People", defaultWidth = 16.dp, defaultHeight = 16.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFFD97706))) {
            moveTo(16f, 11f); curveTo(17.66f, 11f, 18.99f, 9.66f, 18.99f, 8f); curveTo(18.99f, 6.34f, 17.66f, 5f, 16f, 5f); curveTo(14.34f, 5f, 13f, 6.34f, 13f, 8f); curveTo(13f, 9.66f, 14.34f, 11f, 16f, 11f); close()
            moveTo(8f, 11f); curveTo(9.66f, 11f, 10.99f, 9.66f, 10.99f, 8f); curveTo(10.99f, 6.34f, 9.66f, 5f, 8f, 5f); curveTo(6.34f, 5f, 5f, 6.34f, 5f, 8f); curveTo(5f, 9.66f, 6.34f, 11f, 8f, 11f); close()
            moveTo(8f, 13f); curveTo(5.67f, 13f, 1f, 14.17f, 1f, 16.5f); lineTo(1f, 19f); lineTo(15f, 19f); lineTo(15f, 16.5f); curveTo(15f, 14.17f, 10.33f, 13f, 8f, 13f); close()
        }
    }.build()

private val PlusIcon: ImageVector
    get() = ImageVector.Builder(name = "Plus", defaultWidth = 18.dp, defaultHeight = 18.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF0284C7))) {
            moveTo(19f, 13f); lineTo(13f, 13f); lineTo(13f, 19f); lineTo(11f, 19f); lineTo(11f, 13f); lineTo(5f, 13f); lineTo(5f, 11f); lineTo(11f, 11f); lineTo(11f, 5f); lineTo(13f, 5f); lineTo(13f, 11f); lineTo(19f, 11f); close()
        }
    }.build()

@Composable
fun PersonalProfileScreen(
    displayName: String = "Tôi",
    gender: String = "",
    age: String = "",
    occupation: String = "",
    needType: String = "",
    lifestyle: String = "",
    budget: String = "",
    location: String = "",
    bio: String = "",
    traits: String = "",
    avatarUrl: String? = null,
    isPhotoUploading: Boolean = false,
    onAddPhoto: (Uri) -> Unit = {},
    onSaveNotes: (bio: String, lifestyle: String, traits: String) -> Unit = { _, _, _ -> },
    onPreviewClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToInvites: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    val displayHeader = listOfNotNull(
        displayName.ifBlank { "Người dùng" },
        if (age.isNotBlank()) age else null
    ).joinToString(", ")

    // Multiple photos parsing
    val photosList = remember(avatarUrl) {
        (avatarUrl ?: "").split(",").map { it.trim() }.filter { it.isNotBlank() }
    }
    val mainPhoto = photosList.getOrNull(0)
    val photo1 = photosList.getOrNull(1)
    val photo2 = photosList.getOrNull(2)
    val photo3 = photosList.getOrNull(3)
    val photo4 = photosList.getOrNull(4)
    val photo5 = photosList.getOrNull(5)

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onAddPhoto(uri)
            }
        }
    )

    // Editing Intro Dialog state
    var editingField by remember { mutableStateOf<String?>(null) } // "bio" | "lifestyle" | "traits"
    var editNoteInput by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "profile",
                onProfileClick = {},
                onDiscoverClick = onNavigateToDiscover,
                onRoomClick = onNavigateToRoom,
                onMessagesClick = onNavigateToMessages,
                onInvitesClick = onNavigateToInvites
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hồ sơ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A),
                    modifier = Modifier.weight(1f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Edit Profile Icon
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .background(Color.White, CircleShape)
                            .clickable { onEditProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ProfEditIcon,
                            contentDescription = "Chỉnh sửa hồ sơ",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF475569)
                        )
                    }

                    // Settings Icon
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .background(Color.White, CircleShape)
                            .clickable { onSettingsClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ProfSettingsIcon,
                            contentDescription = "Cài đặt tài khoản",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF475569)
                        )
                    }
                }
            }

            // User Avatar & Name Center Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(BluePrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (!mainPhoto.isNullOrBlank()) {
                        AsyncImage(
                            model = mainPhoto,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = displayName.take(1).uppercase().ifBlank { "U" },
                            color = Color.White,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = displayHeader,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // "Xem trước" Pill Button
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                        .clickable { onPreviewClick() }
                        .padding(horizontal = 18.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Xem trước",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 1: Ảnh của tôi
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ảnh của tôi (${photosList.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )

                            Text(
                                text = "+ Thêm ảnh",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BluePrimary,
                                modifier = Modifier.clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Photo Grid Row: Large photo on Left, 3 stacked photos on Right
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Main large photo
                            Box(
                                modifier = Modifier
                                    .weight(1.3f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (!mainPhoto.isNullOrBlank()) {
                                    AsyncImage(
                                        model = mainPhoto,
                                        contentDescription = "Ảnh chính",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(
                                        text = displayName.take(1).uppercase().ifBlank { "📷" },
                                        color = Color(0xFF94A3B8),
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Secondary photos column
                            Column(
                                modifier = Modifier
                                    .weight(0.9f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PhotoThumbnailSlot(
                                    imageUrl = photo1,
                                    isLoading = isPhotoUploading && photosList.size == 1,
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                )
                                PhotoThumbnailSlot(
                                    imageUrl = photo2,
                                    isLoading = isPhotoUploading && photosList.size == 2,
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                )
                                PhotoThumbnailSlot(
                                    imageUrl = photo3,
                                    isLoading = isPhotoUploading && photosList.size == 3,
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom upload slots with '+' button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isPhotoUploading && photosList.size == 4) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = BluePrimary)
                                } else if (!photo4.isNullOrBlank()) {
                                    AsyncImage(
                                        model = photo4,
                                        contentDescription = "Ảnh 5",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .background(Color(0xFFE0F2FE), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = PlusIcon, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isPhotoUploading && photosList.size == 5) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = BluePrimary)
                                } else if (!photo5.isNullOrBlank()) {
                                    AsyncImage(
                                        model = photo5,
                                        contentDescription = "Ảnh 6",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .background(Color(0xFFE0F2FE), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = PlusIcon, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Card 2: Giới thiệu của tôi
                Text(
                    text = "Giới thiệu của tôi",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Prompt 1: Giới thiệu bản thân (Blue card)
                PromptIntroCard(
                    title = "Giới thiệu bản thân",
                    subtitle = bio.ifBlank { "Thêm vài dòng để mọi người hiểu hơn về bạn." },
                    iconBg = Color(0xFFDBEAFE),
                    icon = SparkleIcon,
                    cardBg = Color(0xFFF0F7FF),
                    onEditClick = {
                        editingField = "bio"
                        editNoteInput = bio
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Prompt 2: Ở cùng mình sẽ như thế nào? (Green card)
                PromptIntroCard(
                    title = "Ở cùng mình sẽ như thế nào?",
                    subtitle = if (lifestyle.isNotBlank()) LifestyleCriteria.displayText(lifestyle) else "Chia sẻ điều thú vị khi trở thành bạn cùng nhà của bạn.",
                    iconBg = Color(0xFFDCFCE7),
                    icon = HouseIcon,
                    cardBg = Color(0xFFF0FDF4),
                    onEditClick = {
                        editingField = "lifestyle"
                        editNoteInput = LifestyleCriteria.note(lifestyle)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Prompt 3: Roommate mong muốn (Warm peach card)
                PromptIntroCard(
                    title = "Mình đang tìm một roommate như thế nào?",
                    subtitle = if (traits.isNotBlank()) LifestyleCriteria.displayText(traits) else "Kể về người bạn cùng nhà mà bạn mong muốn.",
                    iconBg = Color(0xFFFFEDD5),
                    icon = PeopleIcon,
                    cardBg = Color(0xFFFFF7ED),
                    onEditClick = {
                        editingField = "traits"
                        editNoteInput = traits
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Dialog for editing Intro Notes
        if (editingField != null) {
            val dialogTitle = when (editingField) {
                "bio" -> "Giới thiệu bản thân"
                "lifestyle" -> "Ở cùng mình sẽ như thế nào?"
                "traits" -> "Roommate mong muốn"
                else -> "Ghi chú"
            }

            val dialogHint = when (editingField) {
                "bio" -> "Thêm vài dòng để mọi người hiểu hơn về bạn..."
                "lifestyle" -> "Chia sẻ điều thú vị khi trở thành bạn cùng nhà của bạn (giờ giấc, nấu nướng, vệ sinh...)..."
                "traits" -> "Kể về người bạn cùng nhà mà bạn mong muốn (sạch sẽ, tôn trọng riêng tư, không hút thuốc...)..."
                else -> "Nhập nội dung..."
            }

            AlertDialog(
                onDismissRequest = { editingField = null },
                title = {
                    Text(
                        text = dialogTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = editNoteInput,
                            onValueChange = { editNoteInput = it },
                            placeholder = { Text(dialogHint, fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrectEnabled = true
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            when (editingField) {
                                "bio" -> onSaveNotes(editNoteInput, LifestyleCriteria.note(lifestyle), traits)
                                "lifestyle" -> onSaveNotes(bio, editNoteInput, traits)
                                "traits" -> onSaveNotes(bio, LifestyleCriteria.note(lifestyle), editNoteInput)
                            }
                            editingField = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Lưu", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingField = null }) {
                        Text("Hủy", color = Color(0xFF64748B))
                    }
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
private fun ColumnScope.PhotoThumbnailSlot(
    imageUrl: String?,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F5F9))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = BluePrimary)
        } else if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text("📷", fontSize = 16.sp)
        }
    }
}

@Composable
private fun PromptIntroCard(
    title: String,
    subtitle: String,
    iconBg: Color,
    icon: ImageVector,
    cardBg: Color,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .clickable { onEditClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(iconBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Icon(
                    imageVector = PromptPencilIcon,
                    contentDescription = "Chỉnh sửa",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalProfileScreenPreview() {
    HomEverTheme {
        PersonalProfileScreen(
            displayName = "Nguyễn Minh Trung",
            age = "22",
            bio = "Thêm vài dòng để mọi người hiểu hơn về bạn.",
            lifestyle = "Tôn trọng giờ giấc, giữ vệ sinh chung, văn minh và lịch sự.",
            needType = "roommate",
            budget = "3 - 5 triệu/tháng",
            location = "Quận 1, TP.HCM"
        )
    }
}
