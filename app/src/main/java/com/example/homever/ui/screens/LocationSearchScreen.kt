package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.domain.model.MatchingPreferenceCatalog
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

@Composable
fun LocationSearchScreen(
    modifier: Modifier = Modifier,
    initialLocation: String = "",
    onBack: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedIds by rememberSaveable(initialLocation) {
        mutableStateOf(MatchingPreferenceCatalog.districtIdsFromDisplay(initialLocation))
    }
    val districts = MatchingPreferenceCatalog.districts
    val allSelected = selectedIds.size == districts.size && districts.all { it.id in selectedIds }
    val visibleDistricts = remember(searchQuery) {
        districts.filter { it.label.contains(searchQuery.trim(), ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
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
            text = "Chọn một hoặc nhiều khu vực tìm kiếm tại TP. HCM. Chỉ những người có khu vực chung với bạn mới được gợi ý.",
            fontSize = 14.sp,
            color = TextGray,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Tìm khu vực trong danh sách...") },
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

        Spacer(modifier = Modifier.height(20.dp))
        LocationChoice(
            label = "Toàn TP. HCM",
            selected = allSelected,
            onSelectedChange = { checked ->
                selectedIds = if (checked) districts.map { it.id } else emptyList()
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (selectedIds.isEmpty()) "Chưa chọn khu vực" else "Đã chọn ${selectedIds.size} khu vực",
            fontSize = 13.sp,
            color = TextGray
        )
        if (selectedIds.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = MatchingPreferenceCatalog.locationDisplay(selectedIds),
                fontSize = 13.sp,
                color = HomeverTextDark,
                lineHeight = 19.sp
            )
        }
        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            visibleDistricts.forEach { district ->
                LocationChoice(
                    label = district.label,
                    selected = district.id in selectedIds,
                    onSelectedChange = { checked ->
                        selectedIds = if (checked) {
                            (selectedIds + district.id).distinct()
                        } else {
                            selectedIds - district.id
                        }
                    }
                )
            }
            if (visibleDistricts.isEmpty()) {
                Text(
                    text = "Không tìm thấy khu vực trong danh sách.",
                    fontSize = 14.sp,
                    color = TextGray
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
        Button(
            onClick = { onNext(MatchingPreferenceCatalog.locationDisplay(selectedIds)) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(14.dp),
            enabled = selectedIds.isNotEmpty()
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

@Composable
private fun LocationChoice(
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) Color(0xFFDDF4FF) else Color.White, RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (selected) BluePrimary else HomeverGrayBorder,
                RoundedCornerShape(14.dp)
            )
            .toggleable(value = selected, role = Role.Checkbox, onValueChange = onSelectedChange)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = HomeverTextDark,
            modifier = Modifier.weight(1f)
        )
        Checkbox(checked = selected, onCheckedChange = null)
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSearchScreenPreview() {
    HomEverTheme {
        LocationSearchScreen()
    }
}
