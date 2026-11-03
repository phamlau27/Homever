package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

private val CustomArrowBack: ImageVector
    get() = ImageVector.Builder(name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f); lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f); lineTo(20f, 13f); close()
        }
    }.build()

private val CustomSearchIcon: ImageVector
    get() = ImageVector.Builder(name = "Search", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(15.5f, 14f); lineTo(16.21f, 14f); lineTo(20.49f, 18.29f); lineTo(19.08f, 19.71f); lineTo(14.79f, 15.42f); lineTo(14.79f, 14.71f); lineTo(14.5f, 14.42f)
            arcTo(6.47f, 6.47f, 0f, false, true, 9.5f, 16f); arcTo(6.5f, 6.5f, 0f, true, true, 16f, 9.5f); arcTo(6.47f, 6.47f, 0f, false, true, 14.42f, 14.5f); lineTo(14.71f, 14.79f); close()
            moveTo(9.5f, 14f); arcTo(4.5f, 4.5f, 0f, true, false, 9.5f, 5f); arcTo(4.5f, 4.5f, 0f, false, false, 9.5f, 14f); close()
        }
    }.build()

private val CustomLocationIcon: ImageVector
    get() = ImageVector.Builder(name = "Location", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 2f); arcTo(7f, 7f, 0f, false, false, 5f, 9f); curveTo(5f, 14.25f, 12f, 22f, 12f, 22f); curveTo(12f, 22f, 19f, 14.25f, 19f, 9f); arcTo(7f, 7f, 0f, false, false, 12f, 2f); close()
            moveTo(12f, 11.5f); arcTo(2.5f, 2.5f, 0f, true, true, 12f, 6.5f); arcTo(2.5f, 2.5f, 0f, false, true, 12f, 11.5f); close()
        }
    }.build()

private val CustomLocationIconGray: ImageVector
    get() = ImageVector.Builder(name = "LocationGray", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(TextGray)) {
            moveTo(12f, 2f); arcTo(7f, 7f, 0f, false, false, 5f, 9f); curveTo(5f, 14.25f, 12f, 22f, 12f, 22f); curveTo(12f, 22f, 19f, 14.25f, 19f, 9f); arcTo(7f, 7f, 0f, false, false, 12f, 2f); close()
            moveTo(12f, 11.5f); arcTo(2.5f, 2.5f, 0f, true, true, 12f, 6.5f); arcTo(2.5f, 2.5f, 0f, false, true, 12f, 11.5f); close()
        }
    }.build()

private val CustomCheckCircle: ImageVector
    get() = ImageVector.Builder(name = "CheckCircle", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 2f); arcTo(10f, 10f, 0f, true, false, 12f, 22f); arcTo(10f, 10f, 0f, true, false, 12f, 2f); close()
            moveTo(10f, 17f); lineTo(5f, 12f); lineTo(6.41f, 10.59f); lineTo(10f, 14.17f); lineTo(17.59f, 6.58f); lineTo(19f, 8f); lineTo(10f, 17f); close()
        }
    }.build()

@Composable
fun LocationSearchScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp).padding(top = 24.dp)
        ) {
            Icon(imageVector = CustomArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(16.dp))
            Text("Khu vực tìm kiếm", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TitleBlue)
        }
        
        // Map Area Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFFE5E7EB)),
            contentAlignment = Alignment.Center
        ) {
            // Simulated map background
            Text("Bản đồ", color = Color(0xFFA0AAB0), fontSize = 18.sp, fontWeight = FontWeight.Medium)
            
            // Search Bar Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = CustomSearchIcon, contentDescription = "Search", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Tìm kiếm quận, phường...", color = Color(0xFF4B5563), fontSize = 14.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Gợi ý quận phổ biến tại TP. HCM", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
            Spacer(modifier = Modifier.height(16.dp))
            
            LocationOption(title = "Quận 1", selected = true)
            Spacer(modifier = Modifier.height(12.dp))
            LocationOption(title = "Quận 3", selected = true)
            Spacer(modifier = Modifier.height(12.dp))
            LocationOption(title = "Quận Bình Thạnh", selected = false)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 48.dp)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Hoàn tất hồ sơ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
fun LocationOption(title: String, selected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, if (selected) BluePrimary else Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (selected) CustomLocationIcon else CustomLocationIconGray, 
            contentDescription = null, 
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 15.sp, color = Color(0xFF1F2937), modifier = Modifier.weight(1f))
        if (selected) {
            Icon(imageVector = CustomCheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
        } else {
            Box(modifier = Modifier.size(20.dp).border(1.dp, Color(0xFFD1D5DB), CircleShape))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSearchScreenPreview() {
    HomEverTheme {
        LocationSearchScreen()
    }
}