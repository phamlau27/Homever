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
import com.example.homever.ui.theme.HomEverTheme

private val DetailBlue = Color(0xFF2563EB)
private val DetailBgGray = Color(0xFFF8FAFC)
private val DetailTextGray = Color(0xFF64748B)

private val BackArrowIcon: ImageVector
    get() = ImageVector.Builder(name = "BackArrow", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f); lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f); lineTo(20f, 13f); close()
        }
    }.build()

private val ShareIcon: ImageVector
    get() = ImageVector.Builder(name = "Share", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(18f, 16.08f); curveTo(17.24f, 16.08f, 16.56f, 16.38f, 16.04f, 16.85f); lineTo(8.91f, 12.7f); curveTo(8.96f, 12.47f, 9f, 12.24f, 9f, 12f); curveTo(9f, 11.76f, 8.96f, 11.53f, 8.91f, 11.3f); lineTo(15.96f, 7.19f); curveTo(16.5f, 7.69f, 17.21f, 8f, 18f, 8f); curveTo(19.66f, 8f, 21f, 6.66f, 21f, 5f); curveTo(21f, 3.34f, 19.66f, 2f, 18f, 2f); curveTo(16.34f, 2f, 15f, 3.34f, 15f, 5f); curveTo(15f, 5.24f, 15.04f, 5.47f, 15.09f, 5.7f); lineTo(8.04f, 9.81f); curveTo(7.5f, 9.31f, 6.79f, 9f, 6f, 9f); curveTo(4.34f, 9f, 3f, 10.34f, 3f, 12f); curveTo(3f, 13.66f, 4.34f, 15f, 6f, 15f); curveTo(6.79f, 15f, 7.5f, 14.69f, 8.04f, 14.19f); lineTo(15.16f, 18.35f); curveTo(15.11f, 18.56f, 15.08f, 18.78f, 15.08f, 19f); curveTo(15.08f, 20.61f, 16.39f, 21.92f, 18f, 21.92f); curveTo(19.61f, 21.92f, 20.92f, 20.61f, 20.92f, 19f); curveTo(20.92f, 17.39f, 19.61f, 16.08f, 18f, 16.08f); close()
        }
    }.build()

@Composable
fun RoomDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onChatWithOwner: () -> Unit = {}
) {
    var isFavorited by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            // Thanh bar liên hệ cố định dưới đáy
            Surface(
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Giá thuê", fontSize = 12.sp, color = DetailTextGray)
                        Text("3.500.000đ/tháng", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DetailBlue)
                    }
                    Button(
                        onClick = onChatWithOwner,
                        colors = ButtonDefaults.buttonColors(containerColor = DetailBlue),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Nhắn tin chủ phòng", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DetailBgGray)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color(0xFFE2E8F0))
            ) {
                Text(
                    text = "Hình ảnh phòng (Slide ảnh)",
                    color = Color(0xFF94A3B8),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Top Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.9f), CircleShape)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = BackArrowIcon, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                                .clickable { /* Share */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = ShareIcon, contentDescription = "Share", modifier = Modifier.size(18.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                                .clickable { isFavorited = !isFavorited },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (isFavorited) "❤️" else "🤍", fontSize = 18.sp)
                        }
                    }
                }
            }

            // Info Body
            Column(modifier = Modifier.padding(20.dp)) {
                // Title and Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFDCFCE7), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Còn trống", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF16A34A))
                    }
                    Text("Đăng 2 ngày trước", fontSize = 12.sp, color = DetailTextGray)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Phòng studio view sông, đầy đủ tiện nghi tại Thủ Đức",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "📍 Đường số 10, Hiệp Bình Chánh, TP. Thủ Đức",
                    fontSize = 14.sp,
                    color = DetailTextGray
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stats Row (Diện tích, Tối đa, Giờ giấc)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem(title = "Diện tích", value = "25 m²")
                    StatItem(title = "Tối đa", value = "2 người")
                    StatItem(title = "Giờ giấc", value = "Tự do")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tiện ích
                Text("Tiện ích phòng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(12.dp))

                val amenities = listOf("Máy lạnh", "Tủ lạnh", "Máy giặt chung", "Gác lửng", "Bảo vệ 24/7", "Không chung chủ", "Ban công")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        amenities.take(4).forEach { AmenityChip(it) }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        amenities.drop(4).forEach { AmenityChip(it) }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mô tả
                Text("Mô tả chi tiết", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Phòng mới xây sạch sẽ thoáng mát, có cửa sổ lớn đón gió sông. Khu dân cư an ninh, yên tĩnh, gần các trường đại học Hutech, Ngoại Thương, Kinh tế - Luật. Điện 3.5k/kWh, nước 100k/người. Tìm bạn ở ghép sạch sẽ, văn minh.",
                    fontSize = 14.sp,
                    color = DetailTextGray,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Chủ phòng Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(50.dp).background(Color(0xFFCBD5E1), CircleShape))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Trần Văn Hưng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text("Chủ nhà • Phản hồi trong 15 phút", fontSize = 12.sp, color = DetailTextGray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DetailBlue)
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 12.sp, color = DetailTextGray)
    }
}

@Composable
fun AmenityChip(name: String) {
    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text("✓ $name", fontSize = 13.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
fun RoomDetailScreenPreview() {
    HomEverTheme {
        RoomDetailScreen()
    }
}
