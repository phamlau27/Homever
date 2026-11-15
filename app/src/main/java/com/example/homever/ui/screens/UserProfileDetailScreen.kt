package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

private val PreviewCheckIcon: ImageVector
    get() = ImageVector.Builder(name = "Check", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF2563EB))) {
            moveTo(9f, 16.17f); lineTo(4.83f, 12f); lineTo(3.41f, 13.41f); lineTo(9f, 19f); lineTo(21f, 7f); lineTo(19.59f, 5.59f); close()
        }
    }.build()

@Composable
fun UserProfileDetailScreen(
    modifier: Modifier = Modifier,
    name: String = "Phạm Hữu Lầu",
    age: Int = 20,
    locationAndBudget: String = "Thủ Đức • 3–5 triệu/tháng",
    onBack: () -> Unit = {},
    onStartChat: () -> Unit = {},
    onReportUser: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(44.dp))

        // Top Bar: "Xem trước hồ sơ" + Circle check button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.size(36.dp))

            Text(
                text = "Xem trước hồ sơ",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    .background(Color.White, CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PreviewCheckIcon,
                    contentDescription = "Confirm",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF2563EB)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Main Profile Card (Cover image + overlay at bottom)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            // Simulated user avatar / portrait illustration
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFE0E7FF), Color(0xFFCBD5E1))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .background(Color(0xFF94A3B8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ảnh đại diện", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Top indicator (Pill + dots)
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(5.dp)
                        .background(Color(0xFF2563EB), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                )
            }

            // Dark gradient overlay at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text = "$name, $age",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = locationAndBudget,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 1 of Tags
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PreviewTagPill("Cú đêm")
                        PreviewTagPill("Gọn gàng")
                        PreviewTagPill("Không hút thuốc")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 2 of Tags
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PreviewTagPill("Thích nấu ăn")
                        PreviewTagPill("Cần riêng tư")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PreviewTagPill(text: String) {
    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF1E293B),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileDetailScreenPreview() {
    HomEverTheme {
        UserProfileDetailScreen()
    }
}
