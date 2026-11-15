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

private val FilterPrimaryBlue = Color(0xFF3B82F6)
private val FilterSelectedGreen = Color(0xFF2DD4BF)
private val FilterSelectedBg = Color(0xFFCCFBF1)

private val CloseFilterIcon: ImageVector
    get() = ImageVector.Builder(name = "Close", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF334155))) {
            moveTo(19f, 6.41f); lineTo(17.59f, 5f); lineTo(12f, 10.59f); lineTo(6.41f, 5f); lineTo(5f, 6.41f); lineTo(10.59f, 12f); lineTo(5f, 17.59f); lineTo(6.41f, 19f); lineTo(12f, 13.41f); lineTo(17.59f, 19f); lineTo(19f, 17.59f); lineTo(13.41f, 12f); lineTo(19f, 6.41f); close()
        }
    }.build()

@Composable
fun RoomFilterScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onApplyFilter: () -> Unit = {}
) {
    // States cho các bộ lọc
    val selectedLocations = remember { mutableStateListOf("Thủ Đức", "Bình Thạnh") }
    var selectedArea by remember { mutableStateOf("20–30m²") }
    var selectedCapacity by remember { mutableStateOf("2 người") }
    val selectedAmenities = remember { mutableStateListOf("Máy lạnh", "Không chung chủ", "Chỗ để xe") }
    var showElectricWater by remember { mutableStateOf(true) }
    var showDeposit by remember { mutableStateOf(false) }
    val selectedStatuses = remember { mutableStateListOf("Còn trống", "Có thể dọn vào ngay") }

    Scaffold(
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Text("Hủy", color = Color(0xFF1E293B), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onApplyFilter,
                        modifier = Modifier.weight(1.2f).height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B6FE7))
                    ) {
                        Text("Xem kết quả", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Top Bar: (X) icon, "Bộ lọc", "Đặt lại"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = CloseFilterIcon, contentDescription = "Close", modifier = Modifier.size(16.dp))
                }

                Text("Bộ lọc", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))

                Text(
                    text = "Đặt lại",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3B6FE7),
                    modifier = Modifier.clickable {
                        selectedLocations.clear()
                        selectedAmenities.clear()
                        selectedStatuses.clear()
                    }
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Section 1: Khu vực
                Text("Khu vực", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "Thủ Đức", isSelected = selectedLocations.contains("Thủ Đức"), onClick = { toggleList(selectedLocations, "Thủ Đức") })
                    FilterChip(label = "Bình Thạnh", isSelected = selectedLocations.contains("Bình Thạnh"), onClick = { toggleList(selectedLocations, "Bình Thạnh") })
                    FilterChip(label = "Quận 1", isSelected = selectedLocations.contains("Quận 1"), onClick = { toggleList(selectedLocations, "Quận 1") })
                    FilterChip(label = "Quận 3", isSelected = selectedLocations.contains("Quận 3"), onClick = { toggleList(selectedLocations, "Quận 3") })
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "Gò Vấp", isSelected = selectedLocations.contains("Gò Vấp"), onClick = { toggleList(selectedLocations, "Gò Vấp") })
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text("+", fontSize = 14.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: Khoảng giá
                Text("Khoảng giá", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text("Từ 2 triệu", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text("Đến 5 triệu", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Mock Range slider track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color(0xFFE2E8F0), CircleShape))
                    Box(modifier = Modifier.padding(start = 40.dp).width(200.dp).height(4.dp).background(Color(0xFF3B6FE7), CircleShape))
                    Box(modifier = Modifier.padding(start = 32.dp).size(20.dp).background(Color.White, CircleShape).border(2.dp, Color(0xFF3B6FE7), CircleShape))
                    Box(modifier = Modifier.padding(start = 232.dp).size(20.dp).background(Color.White, CircleShape).border(2.dp, Color(0xFF3B6FE7), CircleShape))
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(16.dp))

                // Section 3: Diện tích
                Text("Diện tích", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "Dưới 20m²", isSelected = selectedArea == "Dưới 20m²", onClick = { selectedArea = "Dưới 20m²" })
                    FilterChip(label = "20–30m²", isSelected = selectedArea == "20–30m²", onClick = { selectedArea = "20–30m²" })
                    FilterChip(label = "30–50m²", isSelected = selectedArea == "30–50m²", onClick = { selectedArea = "30–50m²" })
                    FilterChip(label = "Trên 50m²", isSelected = selectedArea == "Trên 50m²", onClick = { selectedArea = "Trên 50m²" })
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(16.dp))

                // Section 4: Sức chứa
                Text("Sức chứa", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "1 người", isSelected = selectedCapacity == "1 người", onClick = { selectedCapacity = "1 người" })
                    FilterChip(label = "2 người", isSelected = selectedCapacity == "2 người", onClick = { selectedCapacity = "2 người" })
                    FilterChip(label = "3 người", isSelected = selectedCapacity == "3 người", onClick = { selectedCapacity = "3 người" })
                    FilterChip(label = "4+ người", isSelected = selectedCapacity == "4+ người", onClick = { selectedCapacity = "4+ người" })
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(16.dp))

                // Section 5: Tiện ích
                Text("Tiện ích", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "Máy lạnh", isSelected = selectedAmenities.contains("Máy lạnh"), onClick = { toggleList(selectedAmenities, "Máy lạnh") })
                    FilterChip(label = "Có gác", isSelected = selectedAmenities.contains("Có gác"), onClick = { toggleList(selectedAmenities, "Có gác") })
                    FilterChip(label = "Không chung chủ", isSelected = selectedAmenities.contains("Không chung chủ"), onClick = { toggleList(selectedAmenities, "Không chung chủ") })
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "Gần trường", isSelected = selectedAmenities.contains("Gần trường"), onClick = { toggleList(selectedAmenities, "Gần trường") })
                    FilterChip(label = "Chỗ để xe", isSelected = selectedAmenities.contains("Chỗ để xe"), onClick = { toggleList(selectedAmenities, "Chỗ để xe") })
                    FilterChip(label = "Giờ giấc tự do", isSelected = selectedAmenities.contains("Giờ giấc tự do"), onClick = { toggleList(selectedAmenities, "Giờ giấc tự do") })
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "Có bếp", isSelected = selectedAmenities.contains("Có bếp"), onClick = { toggleList(selectedAmenities, "Có bếp") })
                    FilterChip(label = "Có máy giặt", isSelected = selectedAmenities.contains("Có máy giặt"), onClick = { toggleList(selectedAmenities, "Có máy giặt") })
                    FilterChip(label = "Cho nuôi thú cưng", isSelected = selectedAmenities.contains("Cho nuôi thú cưng"), onClick = { toggleList(selectedAmenities, "Cho nuôi thú cưng") })
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(16.dp))

                // Section 6: Chi phí rõ ràng (Switches)
                Text("Chi phí rõ ràng", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hiển thị rõ điện nước", fontSize = 14.sp, color = Color(0xFF334155))
                    Switch(
                        checked = showElectricWater,
                        onCheckedChange = { showElectricWater = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF3B6FE7))
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Có tiền cọc rõ ràng", fontSize = 14.sp, color = Color(0xFF334155))
                    Switch(
                        checked = showDeposit,
                        onCheckedChange = { showDeposit = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF3B6FE7))
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(16.dp))

                // Section 7: Trạng thái
                Text("Trạng thái", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(label = "Còn trống", isSelected = selectedStatuses.contains("Còn trống"), onClick = { toggleList(selectedStatuses, "Còn trống") })
                    FilterChip(label = "Mới đăng", isSelected = selectedStatuses.contains("Mới đăng"), onClick = { toggleList(selectedStatuses, "Mới đăng") })
                    FilterChip(label = "Có thể dọn vào ngay", isSelected = selectedStatuses.contains("Có thể dọn vào ngay"), onClick = { toggleList(selectedStatuses, "Có thể dọn vào ngay") })
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) Color(0xFFCCFBF1) else Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF14B8A6) else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = if (isSelected) "✓ $label" else label,
            fontSize = 13.sp,
            color = if (isSelected) Color(0xFF0F766E) else Color(0xFF475569),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

private fun toggleList(list: androidx.compose.runtime.snapshots.SnapshotStateList<String>, item: String) {
    if (list.contains(item)) list.remove(item) else list.add(item)
}

@Preview(showBackground = true)
@Composable
fun RoomFilterScreenPreview() {
    HomEverTheme {
        RoomFilterScreen()
    }
}
