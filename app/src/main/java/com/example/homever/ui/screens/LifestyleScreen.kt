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

private val LifestyleArrowBack: ImageVector
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

private val GenericIcon: ImageVector
    get() = ImageVector.Builder(
        name = "GenericIcon", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 2f); arcTo(10f, 10f, 0f, true, false, 12f, 22f)
            arcTo(10f, 10f, 0f, true, false, 12f, 2f); close()
            moveTo(12f, 17f); arcTo(5f, 5f, 0f, true, true, 12f, 7f)
            arcTo(5f, 5f, 0f, true, true, 12f, 17f); close()
        }
    }.build()

@Composable
fun LifestyleScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    var genderPref by remember { mutableStateOf("Mọi người") }
    var schedulePref by remember { mutableStateOf("Cú đêm") }
    var noisePref by remember { mutableStateOf("Khá yên tĩnh") }
    var tidinessPref by remember { mutableStateOf("Rất gọn gàng") }
    var smokingPref by remember { mutableStateOf("Không hút") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        // ── Top Bar ───────────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {
            Icon(
                imageVector = LifestyleArrowBack,
                contentDescription = "Quay lại",
                tint = HomeverTextDark
            )
            Spacer(Modifier.width(16.dp))
            Text(
                "Thói quen sinh hoạt",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }

        Spacer(Modifier.height(28.dp))

        Text("BƯỚC 3/4", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        Spacer(Modifier.height(4.dp))
        Text(
            "Lối sống của bạn thế nào?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Chọn một đáp án cho mỗi câu hỏi. Bạn có thể cuộn để xem tất cả.",
            fontSize = 13.sp,
            color = TextGray,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(20.dp))

        // Progress bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tiến độ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomeverTextDark)
            Spacer(Modifier.weight(1f))
            Text("3/4", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            repeat(3) {
                Box(Modifier.weight(1f).height(4.dp).background(BluePrimary, CircleShape))
            }
            Box(Modifier.weight(1f).height(4.dp).background(HomeverGrayBorder, CircleShape))
        }

        Spacer(Modifier.height(28.dp))

        SelectableQuestionCard(
            title = "Bạn muốn thấy ai?",
            subtitle = "Chọn đối tượng bạn muốn xem",
            selectedValue = genderPref,
            onSelect = { genderPref = it },
            rows = listOf(listOf("Nam", "Nữ", "Mọi người"))
        )
        Spacer(Modifier.height(16.dp))
        SelectableQuestionCard(
            title = "Giờ sinh hoạt",
            subtitle = "Chọn khung giờ phù hợp nhất",
            selectedValue = schedulePref,
            onSelect = { schedulePref = it },
            rows = listOf(listOf("Dậy sớm", "Bình thường", "Cú đêm"), listOf("Linh hoạt"))
        )
        Spacer(Modifier.height(16.dp))
        SelectableQuestionCard(
            title = "Không gian sống",
            subtitle = "Mức độ yên tĩnh bạn mong muốn",
            selectedValue = noisePref,
            onSelect = { noisePref = it },
            rows = listOf(listOf("Rất yên tĩnh", "Khá yên tĩnh"), listOf("Bình thường", "Không ngại ồn"))
        )
        Spacer(Modifier.height(16.dp))
        SelectableQuestionCard(
            title = "Gọn gàng",
            subtitle = "Phong cách sắp xếp không gian của bạn",
            selectedValue = tidinessPref,
            onSelect = { tidinessPref = it },
            rows = listOf(listOf("Rất gọn gàng", "Khá gọn gàng"), listOf("Bình thường", "Khá thoải mái"))
        )
        Spacer(Modifier.height(16.dp))
        SelectableQuestionCard(
            title = "Hút thuốc",
            subtitle = "Tình trạng hút thuốc của bạn",
            selectedValue = smokingPref,
            onSelect = { smokingPref = it },
            rows = listOf(listOf("Không hút", "Thỉnh thoảng"), listOf("Thường xuyên"))
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                val combinedTraits = listOf(schedulePref, noisePref, tidinessPref, "Khói thuốc: $smokingPref")
                    .filter { it.isNotBlank() }
                    .joinToString(", ")
                onNext(combinedTraits)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Tiếp tục", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun SelectableQuestionCard(
    title: String,
    subtitle: String,
    selectedValue: String,
    onSelect: (String) -> Unit,
    rows: List<List<String>>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, HomeverGrayBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFF3F4F6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = GenericIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = BluePrimary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomeverTextDark)
                    Text(subtitle, fontSize = 12.sp, color = TextGray)
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowItems.forEach { text ->
                            val isSelected = text == selectedValue
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) Color(0xFFDDF4FF) else Color.White,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) BluePrimary else HomeverGrayBorder,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onSelect(text) }
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
    com.example.homever.ui.theme.HomEverTheme {
        LifestyleScreen()
    }
}
