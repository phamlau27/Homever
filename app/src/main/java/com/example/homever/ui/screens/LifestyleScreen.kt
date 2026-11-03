package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

private val GenericIcon: ImageVector
    get() = ImageVector.Builder(name = "GenericIcon", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 2f); arcTo(10f, 10f, 0f, true, false, 12f, 22f); arcTo(10f, 10f, 0f, true, false, 12f, 2f); close()
            moveTo(12f, 17f); arcTo(5f, 5f, 0f, true, true, 12f, 7f); arcTo(5f, 5f, 0f, true, true, 12f, 17f); close()
        }
    }.build()

@Composable
fun LifestyleScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp, vertical = 48.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = CustomArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(16.dp))
            Text("Thói quen sinh hoạt", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TitleBlue)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("BƯỚC 1/3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Lối sống của bạn thế nào?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TitleBlue)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Chọn một đáp án cho mỗi câu hỏi. Bạn có thể cuộn\nđể xem tất cả.", fontSize = 13.sp, color = TextGray, lineHeight = 18.sp)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Progress Bar
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Tiến độ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Spacer(modifier = Modifier.weight(1f))
            Text("1/3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f).height(4.dp).background(BluePrimary, CircleShape))
            Box(modifier = Modifier.weight(1f).height(4.dp).background(Color(0xFFE5E7EB), CircleShape))
            Box(modifier = Modifier.weight(1f).height(4.dp).background(Color(0xFFE5E7EB), CircleShape))
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Questions
        QuestionCard(
            title = "Bạn muốn thấy ai?",
            subtitle = "Chọn đối tượng bạn muốn xem",
            rows = listOf(
                listOf("Nam" to true, "Nữ" to false, "Mọi người" to false)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        QuestionCard(
            title = "Giờ sinh hoạt",
            subtitle = "Chọn khung giờ phù hợp nhất",
            rows = listOf(
                listOf("Dậy sớm" to true, "Bình thường" to false, "Cú đêm" to false),
                listOf("Linh hoạt" to false)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        QuestionCard(
            title = "Không gian sống",
            subtitle = "Mức độ yên tĩnh bạn mong muốn",
            rows = listOf(
                listOf("Rất yên tĩnh" to true, "Khá yên tĩnh" to false),
                listOf("Bình thường" to false, "Không ngại ồn" to false)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        QuestionCard(
            title = "Gọn gàng",
            subtitle = "Phong cách sắp xếp không gian của bạn",
            rows = listOf(
                listOf("Rất gọn gàng" to true, "Khá gọn gàng" to false),
                listOf("Bình thường" to false, "Khá thoải mái" to false)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        QuestionCard(
            title = "Hút thuốc",
            subtitle = "Tình trạng hút thuốc của bạn",
            rows = listOf(
                listOf("Không hút" to true, "Thỉnh thoảng" to false),
                listOf("Thường xuyên" to false)
            )
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Tiếp tục", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
fun QuestionCard(title: String, subtitle: String, rows: List<List<Pair<String, Boolean>>>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFF3F4F6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = GenericIcon, contentDescription = null, modifier = Modifier.size(20.dp), tint = BluePrimary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                    Text(subtitle, fontSize = 12.sp, color = TextGray)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowItems.forEach { (text, isSelected) ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) Color(0xFFDDF4FF) else Color.White,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) BluePrimary else Color(0xFFE5E7EB),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = text,
                                    fontSize = 13.sp,
                                    color = if (isSelected) BluePrimary else TextGray,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LifestyleScreenPreview() {
    HomEverTheme {
        LifestyleScreen()
    }
}