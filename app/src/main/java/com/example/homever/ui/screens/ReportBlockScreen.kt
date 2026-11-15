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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

private val DangerRed = Color(0xFFDC2626)
private val DangerButtonColor = Color(0xFFD9534F)

private val BackArrowReportIcon: ImageVector
    get() = ImageVector.Builder(name = "BackArrow", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f); lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f); lineTo(20f, 13f); close()
        }
    }.build()

@Composable
fun ReportBlockScreen(
    modifier: Modifier = Modifier,
    targetUserName: String = "Lê Tuấn Kiệt",
    targetUserAge: Int = 23,
    targetUserDesc: String = "Đang tìm bạn ở ghép Quận 1",
    onBack: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    var selectedReason by remember { mutableStateOf("Ngôn từ quấy rối, thiếu lịch sự") }
    var descriptionText by remember { mutableStateOf("Bạn này liên tục gửi tin nhắn rác và những lời mời thô tục vô căn cứ làm phiền cuộc sống của tôi...") }
    var shouldBlockUser by remember { mutableStateOf(true) }

    val reportReasons = listOf(
        "Hồ sơ lừa đảo, giả mạo thông tin",
        "Ngôn từ quấy rối, thiếu lịch sự",
        "Hình ảnh đại diện không phù hợp",
        "Lý do khác"
    )

    Scaffold(
        bottomBar = {
            Surface(color = Color.White) {
                Button(
                    onClick = onSubmitSuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerButtonColor),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text("Gửi báo cáo của bạn", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Header: Back Arrow + "Báo cáo & Chặn"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Icon(
                    imageVector = BackArrowReportIcon,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onBack() },
                    tint = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Báo cáo & Chặn", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Target User Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCBD5E1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Avatar", color = Color.Gray, fontSize = 10.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text("$targetUserName, $targetUserAge", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(targetUserDesc, fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("LÝ DO BÁO CÁO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(10.dp))

            // Reason List (Individual white cards with radio circles)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                reportReasons.forEach { reason ->
                    val isSelected = selectedReason == reason
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(14.dp))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedReason = reason }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = reason,
                                fontSize = 14.sp,
                                color = Color(0xFF1E293B),
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )

                            // Custom Radio Dot
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.5.dp,
                                        color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFCBD5E1),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(Color(0xFF3B82F6), CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("MÔ TẢ CHI TIẾT (TÙY CHỌN)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(10.dp))

            // Textarea for description
            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Color(0xFF334155), lineHeight = 19.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Switch: "Đồng thời chặn Lê Tuấn Kiệt"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Đồng thời chặn $targetUserName", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text("Bạn sẽ không thấy hồ sơ và tin nhắn của người này nữa.", fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(
                        checked = shouldBlockUser,
                        onCheckedChange = { shouldBlockUser = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF3B6FE7)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportBlockScreenPreview() {
    HomEverTheme {
        ReportBlockScreen()
    }
}
