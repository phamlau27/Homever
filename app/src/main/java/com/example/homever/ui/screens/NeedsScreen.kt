package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.*

val CheckCircleIcon: ImageVector
    get() = ImageVector.Builder(
        name = "CheckCircle", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f); arcTo(10f, 10f, 0f, true, false, 12f, 22f)
            arcTo(10f, 10f, 0f, true, false, 12f, 2f); close()
            moveTo(10f, 17f); lineTo(5f, 12f); lineTo(6.41f, 10.59f)
            lineTo(10f, 14.17f); lineTo(17.59f, 6.58f); lineTo(19f, 8f); lineTo(10f, 17f); close()
        }
    }.build()

val PersonIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Person", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 12f); arcTo(4f, 4f, 0f, true, false, 12f, 4f)
            arcTo(4f, 4f, 0f, true, false, 12f, 12f); close()
            moveTo(12f, 14f); arcTo(9.02f, 9.02f, 0f, false, false, 3f, 20f)
            lineTo(21f, 20f); arcTo(9.02f, 9.02f, 0f, false, false, 12f, 14f); close()
        }
    }.build()

val HomeIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Home", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close()
            moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f)
            lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close()
        }
    }.build()

private val NeedsArrowBackIcon: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.42f, 5.41f); lineTo(12f, 4f)
            lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f)
            lineTo(20f, 13f); close()
        }
    }.build()

@Composable
fun NeedsScreen(
    modifier: Modifier = Modifier,
    initialSelection: String = "roommate",
    onBack: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf(initialSelection) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        // ── Top Bar ───────────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {
            Icon(
                imageVector = NeedsArrowBackIcon,
                contentDescription = "Quay lại",
                tint = HomeverTextDark
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Bạn cần tìm gì?",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }

        Spacer(Modifier.height(40.dp))

        Text(
            text = "Chọn nhu cầu của bạn",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Chọn một nhu cầu phù hợp nhất với bạn.",
            fontSize = 14.sp,
            color = TextGray
        )

        Spacer(Modifier.height(28.dp))

        // ── Option: Find a Roommate ────────────────────────────────────────
        NeedOptionCard(
            icon = PersonIcon,
            title = "Tôi tìm bạn cùng phòng",
            subtitle = "Tìm người phù hợp để ở cùng.",
            isSelected = selectedOption == "roommate",
            onClick = { selectedOption = "roommate" }
        )

        Spacer(Modifier.height(16.dp))

        // ── Option: Find a Room ────────────────────────────────────────────
        NeedOptionCard(
            icon = HomeIcon,
            title = "Tôi cần tìm phòng",
            subtitle = "Tìm phòng trọ phù hợp với nhu cầu của tôi.",
            isSelected = selectedOption == "room",
            onClick = { selectedOption = "room" }
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onNext(selectedOption) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Tiếp tục",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun NeedOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderWidth = if (isSelected) 2.dp else 1.dp
    val borderColor = if (isSelected) BluePrimary else HomeverGrayBorder
    val iconBg = if (isSelected) Color(0xFFD4F7EE) else BgGray
    val iconTint = if (isSelected) BluePrimary else TextGray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomeverTextDark)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, fontSize = 13.sp, color = TextGray)
            }
            if (isSelected) {
                Icon(CheckCircleIcon, contentDescription = null, tint = BluePrimary)
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(1.dp, Color(0xFFD1D5DB), CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NeedsScreenPreview() {
    com.example.homever.ui.theme.HomEverTheme {
        NeedsScreen()
    }
}
