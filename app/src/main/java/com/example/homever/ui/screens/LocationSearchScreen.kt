package com.example.homever.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.*

private val LocationArrowBack: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f)
            lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f)
            lineTo(20f, 13f); close()
        }
    }.build()

private val LocationSearchIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Search",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(15.5f, 14f); lineTo(16.21f, 14f); lineTo(20.49f, 18.29f); lineTo(19.08f, 19.71f); lineTo(14.79f, 15.42f); lineTo(14.79f, 14.71f); lineTo(14.5f, 14.42f)
            arcTo(6.47f, 6.47f, 0f, false, true, 9.5f, 16f); arcTo(6.5f, 6.5f, 0f, true, true, 16f, 9.5f); arcTo(6.47f, 6.47f, 0f, false, true, 14.42f, 14.5f); lineTo(14.71f, 14.79f); close()
            moveTo(9.5f, 14f); arcTo(4.5f, 4.5f, 0f, true, false, 9.5f, 5f); arcTo(4.5f, 4.5f, 0f, false, false, 9.5f, 14f); close()
        }
    }.build()

private val LocationPinIcon: ImageVector
    get() = ImageVector.Builder(
        name = "LocationPin",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 2f); arcTo(7f, 7f, 0f, false, false, 5f, 9f); curveTo(5f, 14.25f, 12f, 22f, 12f, 22f); curveTo(12f, 22f, 19f, 14.25f, 19f, 9f); arcTo(7f, 7f, 0f, false, false, 12f, 2f); close()
            moveTo(12f, 11.5f); arcTo(2.5f, 2.5f, 0f, true, true, 12f, 6.5f); arcTo(2.5f, 2.5f, 0f, false, true, 12f, 11.5f); close()
        }
    }.build()

private val popularLocations = listOf(
    "Quận 1, TP. HCM",
    "Quận 3, TP. HCM",
    "Quận Bình Thạnh, TP. HCM",
    "TP. Thủ Đức, TP. HCM",
    "Quận 10, TP. HCM",
    "Quận Tân Bình, TP. HCM"
)

@Composable
fun LocationSearchScreen(
    modifier: Modifier = Modifier,
    initialLocation: String = "Quận 1, TP. HCM",
    onBack: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf(if (initialLocation.isNotBlank()) initialLocation else "Quận 1, TP. HCM") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {
            Icon(
                imageVector = LocationArrowBack,
                contentDescription = "Quay lại",
                tint = HomeverTextDark
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Khu vực mong muốn",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text("BƯỚC 4B/5", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Bạn muốn ở khu vực nào?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Chọn hoặc nhập quận/huyện bạn dự định tìm phòng hoặc bạn cùng phòng.",
            fontSize = 14.sp,
            color = TextGray,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.isNotBlank()) {
                    selectedLocation = it
                }
            },
            placeholder = { Text("Nhập quận, huyện hoặc địa chỉ...") },
            leadingIcon = {
                Icon(
                    imageVector = LocationSearchIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = BluePrimary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = HomeverGrayBorder,
                focusedBorderColor = BluePrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Gợi ý khu vực phổ biến tại TP. HCM",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomeverTextDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            popularLocations.forEach { loc ->
                val isSelected = selectedLocation == loc
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSelected) Color(0xFFDDF4FF) else Color.White, RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (isSelected) BluePrimary else HomeverGrayBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            selectedLocation = loc
                            searchQuery = loc
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = LocationPinIcon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = if (isSelected) BluePrimary else TextGray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = loc,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) BluePrimary else HomeverTextDark,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(BluePrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .border(1.dp, Color(0xFFD1D5DB), CircleShape)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = { onNext(selectedLocation) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(14.dp),
            enabled = selectedLocation.isNotBlank()
        ) {
            Text(
                text = "Tiếp tục",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSearchScreenPreview() {
    HomEverTheme {
        LocationSearchScreen()
    }
}
