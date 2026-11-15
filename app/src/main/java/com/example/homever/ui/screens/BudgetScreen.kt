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

private val BudgetArrowBack: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.42f, 5.41f); lineTo(12f, 4f)
            lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f)
            lineTo(20f, 13f); close()
        }
    }.build()

private val budgetPresets = listOf(
    "Dưới 2 triệu",
    "2.0 - 4.0 triệu/tháng",
    "4.0 - 6.0 triệu/tháng",
    "6.0 - 8.0 triệu/tháng",
    "Trên 8 triệu/tháng"
)

@Composable
fun BudgetScreen(
    modifier: Modifier = Modifier,
    initialBudget: String = "2.0 - 5.0 triệu/tháng",
    onBack: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    var selectedBudget by remember { mutableStateOf(if (initialBudget.isNotBlank()) initialBudget else "2.0 - 4.0 triệu/tháng") }

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
                imageVector = BudgetArrowBack,
                contentDescription = "Quay lại",
                tint = HomeverTextDark
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Ngân sách",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("BƯỚC 4A/5", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Khoảng giá mong muốn",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Chúng tôi sẽ gợi ý những bạn cùng phòng hoặc phòng trọ phù hợp với khả năng chi trả của bạn.",
            fontSize = 14.sp,
            color = TextGray,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Selected Range Display Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.5.dp, BluePrimary, RoundedCornerShape(16.dp))
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Khoảng ngân sách đã chọn", color = TextGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedBudget,
                    color = BluePrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Gợi ý các mức phổ biến",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomeverTextDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            budgetPresets.forEach { preset ->
                val isSelected = selectedBudget == preset
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSelected) Color(0xFFDDF4FF) else Color.White, RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (isSelected) BluePrimary else HomeverGrayBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedBudget = preset }
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = preset,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) BluePrimary else HomeverTextDark
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(BluePrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .border(1.dp, Color(0xFFD1D5DB), CircleShape)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onNext(selectedBudget) },
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

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetScreenPreview() {
    HomEverTheme {
        BudgetScreen()
    }
}
