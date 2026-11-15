package com.example.homever.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.*

private val AccArrowBackIcon: ImageVector
    get() = ImageVector.Builder(name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f); lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f); lineTo(20f, 13f); close()
        }
    }.build()

private val AccChevronRightIcon: ImageVector
    get() = ImageVector.Builder(name = "ChevronRight", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(Color.Gray), strokeLineWidth = 2f) {
            moveTo(9f, 5f); lineTo(16f, 12f); lineTo(9f, 19f);
        }
    }.build()

@Composable
fun AccountSettingsScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp, vertical = 48.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBackClick() }
        ) {
            Icon(imageVector = AccArrowBackIcon, contentDescription = "Back")
            Spacer(modifier = Modifier.width(16.dp))
            Text("Cài đặt tài khoản", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BluePrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("CÁ NHÂN HÓA & BẢO MẬT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextGray, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(12.dp))
        
        SettingsSection(
            items = listOf(
                SettingsItem("Quyền riêng tư", "Quản lý hiển thị hồ sơ & thông tin", Color(0xFFE0E7FF), BluePrimary, null),
                SettingsItem("Thông báo", "Cài đặt đẩy và nhắc nhở ghép phòng", Color(0xFFE0E7FF), BluePrimary, "2"),
                SettingsItem("An toàn & Xác thực", "Xác minh căn cước công dân (eKYC)", Color(0xFFD1FAE5), HomeverGreen, null)
            )
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("HỖ TRỢ & CỘNG ĐỒNG", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextGray, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(12.dp))
        
        SettingsSection(
            items = listOf(
                SettingsItem("Danh sách chặn", "Quản lý những roommate đã chặn", Color(0xFFE0E7FF), BluePrimary, null),
                SettingsItem("Báo cáo & Phản hồi", "Gửi báo cáo lỗi hoặc đóng góp ý kiến", Color(0xFFE0E7FF), BluePrimary, null),
                SettingsItem("Trung tâm trợ giúp", "Câu hỏi thường gặp & Hướng dẫn sử dụng", Color(0xFFE0E7FF), BluePrimary, null)
            )
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, Color(0xFFF87171)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = HomeverRedLight)
        ) {
            Text("Đăng xuất", color = HomeverRed, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Phiên bản ứng dụng v2.4.0 (Build 1205)", 
            fontSize = 11.sp, 
            color = TextGray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

data class SettingsItem(val title: String, val subtitle: String, val iconBg: Color, val iconColor: Color, val badge: String?)

@Composable
fun SettingsSection(items: List<SettingsItem>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, HomeverGrayBorder, RoundedCornerShape(16.dp))
    ) {
        Column {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(36.dp).background(item.iconBg, CircleShape), contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.size(16.dp).background(item.iconColor, CircleShape))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = HomeverTextDark)
                        Text(item.subtitle, fontSize = 12.sp, color = TextGray)
                    }
                    
                    if (item.badge != null) {
                        Box(modifier = Modifier.background(BluePrimary, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text(item.badge, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    Icon(imageVector = AccChevronRightIcon, contentDescription = "Next", modifier = Modifier.size(16.dp))
                }
                
                if (index < items.size - 1) {
                    HorizontalDivider(color = HomeverGrayBorder, modifier = Modifier.padding(start = 64.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountSettingsScreenPreview() {
    HomEverTheme {
        AccountSettingsScreen()
    }
}
