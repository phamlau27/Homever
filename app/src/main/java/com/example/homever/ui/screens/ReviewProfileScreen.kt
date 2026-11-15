package com.example.homever.ui.screens

import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.homever.ui.theme.*

private val ReviewArrowBack: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f)
            lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f)
            lineTo(20f, 13f); close()
        }
    }.build()

private val ReviewEditIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Edit", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(HomeverTextGray)) {
            moveTo(3f, 17.25f); lineTo(3f, 21f); lineTo(6.75f, 21f); lineTo(17.81f, 9.94f)
            lineTo(14.06f, 6.19f); lineTo(3f, 17.25f); close()
            moveTo(20.71f, 7.04f)
            arcTo(0.996f, 0.996f, 0f, false, false, 20.71f, 5.63f); lineTo(18.37f, 3.29f)
            arcTo(0.996f, 0.996f, 0f, false, false, 16.96f, 3.29f); lineTo(15.13f, 5.12f)
            lineTo(18.88f, 8.87f); lineTo(20.71f, 7.04f); close()
        }
    }.build()

@Composable
fun ReviewProfileScreen(
    modifier: Modifier = Modifier,
    displayName: String = "",
    gender: String = "",
    age: String = "",
    occupation: String = "",
    needType: String = "roommate",
    lifestyle: String = "",
    budget: String = "",
    location: String = "",
    bio: String = "",
    avatarUri: Uri? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onBack: () -> Unit = {},
    onBioChanged: (String) -> Unit = {},
    onNext: () -> Unit = {}
) {
    val displayNeed = if (needType == "room") "Tôi cần tìm phòng" else "Tôi tìm bạn cùng phòng"
    val subtitleText = listOf(
        if (age.isNotBlank()) "$age tuổi" else null,
        gender.ifBlank { null },
        occupation.ifBlank { null }
    ).filterNotNull().joinToString(" • ")

    var isEditingBio by remember { mutableStateOf(false) }
    var editableBio by remember { mutableStateOf(bio) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp, vertical = 48.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Title ─────────────────────────────────────────────────────────
        Text(
            text = "Kiểm tra lại thông tin",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Xem lại hồ sơ trước khi hoàn tất.",
            fontSize = 13.sp,
            color = TextGray
        )

        Spacer(Modifier.height(24.dp))

        // ── Card 1: Profile Info ──────────────────────────────────────────
        ReviewCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar initial / image
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(BluePrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUri != null) {
                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = displayName.take(1).uppercase().ifBlank { "U" },
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = displayName.ifBlank { "Tên hiển thị" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TitleBlue
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = subtitleText.ifBlank { "Chưa cập nhật" },
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = HomeverGrayBorder)
            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isEditingBio = !isEditingBio }
            ) {
                Text(
                    "GIỚI THIỆU BẢN THÂN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGray,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = ReviewEditIcon,
                    contentDescription = "Chỉnh sửa",
                    modifier = Modifier.size(14.dp),
                    tint = if (isEditingBio) BluePrimary else HomeverTextGray
                )
            }
            Spacer(Modifier.height(8.dp))
            if (isEditingBio) {
                OutlinedTextField(
                    value = editableBio,
                    onValueChange = {
                        editableBio = it
                        onBioChanged(it)
                    },
                    placeholder = { Text("Viết vài dòng giới thiệu về bản thân...", color = Color(0xFFA0AAB0), fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = HomeverGrayBorder,
                        focusedBorderColor = BluePrimary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = HomeverTextDark, lineHeight = 20.sp),
                    maxLines = 5
                )
            } else {
                Text(
                    text = editableBio.ifBlank { "Chưa nhập giới thiệu bản thân. Nhấn để viết." },
                    fontSize = 13.sp,
                    color = if (editableBio.isBlank()) TextGray.copy(alpha = 0.6f) else TextGray,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Card 2: Needs, Budget & Location ─────────────────────────────
        ReviewCard {
            ReviewRow(label = "Nhu cầu chính", value = displayNeed)
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = HomeverGrayBorder)
            Spacer(Modifier.height(12.dp))
            ReviewRow(
                label = "Khoảng ngân sách",
                value = budget.ifBlank { "Chưa chọn ngân sách" }
            )
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = HomeverGrayBorder)
            Spacer(Modifier.height(12.dp))
            ReviewRow(
                label = "Khu vực mong muốn",
                value = location.ifBlank { "Chưa chọn khu vực" }
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Card 3: Lifestyle ─────────────────────────────────────────────
        ReviewCard {
            Text(
                "Thói quen & Lối sống",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = HomeverTextDark
            )
            Spacer(Modifier.height(14.dp))

            val traitsList = lifestyle.split(",").map { it.trim() }.filter { it.isNotBlank() }
            if (traitsList.isNotEmpty()) {
                traitsList.chunked(3).forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowItems.forEach { trait ->
                            Box(
                                modifier = Modifier
                                    .background(BgGray, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(trait, fontSize = 12.sp, color = TextGray)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            } else {
                Text("Chưa chọn lối sống", fontSize = 12.sp, color = TextGray)
            }
        }

        // ── Error ─────────────────────────────────────────────────────────
        if (errorMessage != null) {
            Spacer(Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = HomeverRedLight,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = HomeverRed,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        // ── Back link ─────────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {
            Icon(
                imageVector = ReviewArrowBack,
                contentDescription = "Quay lại",
                modifier = Modifier.size(16.dp),
                tint = BluePrimary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Quay lại chỉnh sửa",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BluePrimary
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Submit button ─────────────────────────────────────────────────
        Button(
            onClick = { onNext() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(14.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    "Hoàn tất hồ sơ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ReviewCard(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, HomeverGrayBorder, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column(content = content)
    }
}

@Composable
private fun ReviewRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = TextGray)
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = HomeverTextDark
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewProfileScreenPreview() {
    HomEverTheme {
        ReviewProfileScreen(
            displayName = "Nguyễn Minh Khánh",
            gender = "Nữ",
            age = "24",
            occupation = "Designer",
            needType = "roommate",
            lifestyle = "Cú đêm, Khá yên tĩnh, Rất gọn gàng",
            budget = "3 - 5 triệu/tháng",
            location = "TP. Hồ Chí Minh",
            bio = "Mình sống khá yên tĩnh, gọn gàng. Mong tìm được bạn roommate tử tế."
        )
    }
}
