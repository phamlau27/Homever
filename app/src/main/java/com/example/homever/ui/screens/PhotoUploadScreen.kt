package com.example.homever.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.homever.ui.theme.HomEverTheme

private val PhotoCheckIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Check",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF2563EB))) {
            moveTo(9f, 16.17f); lineTo(4.83f, 12f); lineTo(3.41f, 13.41f); lineTo(9f, 19f); lineTo(21f, 7f); lineTo(19.59f, 5.59f); close()
        }
    }.build()

/**
 * Màn hình xem trước hồ sơ / thêm ảnh:
 * - Giao diện giống hệt màn hình xem trước (card lớn tràn màn hình)
 * - Ảnh đầu tiên là ảnh đại diện khi người dùng đã chọn
 * - Nhấn bên phải màn hình: chuyển sang ảnh tiếp theo; nếu hết ảnh sẽ chuyển tới trang có dấu (+) để thêm ảnh
 * - Nhấn bên trái màn hình: chuyển về ảnh trước đó
 * - Nếu không thêm nữa: nhấn nút tròn check (hoặc nút Next) góc trên phải để hoàn tất và vào trang chủ
 */
@Composable
fun PhotoUploadScreen(
    modifier: Modifier = Modifier,
    displayName: String = "Tôi",
    age: String = "20",
    budget: String = "3–5 triệu/tháng",
    location: String = "TP.HCM",
    lifestyle: String = "",
    avatarUri: Uri? = null,
    existingPhotos: List<Uri> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onBack: () -> Unit = {},
    onComplete: (List<Uri>) -> Unit = {}
) {
    val maxPhotos = 6
    // Danh sách các ảnh bổ sung (ngoài avatar nếu có)
    val additionalPhotos = remember {
        mutableStateListOf<Uri>().apply {
            addAll(existingPhotos.filter { it != avatarUri })
        }
    }

    // Danh sách tất cả ảnh để hiển thị: avatar đầu tiên (nếu có), sau đó là các ảnh thêm
    val allPhotos: List<Uri> = remember(avatarUri, additionalPhotos.size) {
        val list = mutableListOf<Uri>()
        if (avatarUri != null) list.add(avatarUri)
        list.addAll(additionalPhotos)
        list
    }

    // Tổng số slide = allPhotos.size + (1 slide slot dấu "+" nếu chưa đạt maxPhotos)
    val totalSlots = if (allPhotos.size < maxPhotos) allPhotos.size + 1 else allPhotos.size
    var currentSlideIndex by remember { mutableIntStateOf(0) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxPhotos - allPhotos.size)
    ) { uris ->
        val remaining = maxPhotos - allPhotos.size
        val newOnes = uris.take(remaining)
        additionalPhotos.addAll(newOnes)
        // Khi thêm xong ảnh, quay về xem ảnh vừa thêm
        currentSlideIndex = (allPhotos.size - 1).coerceAtLeast(0)
    }

    // Phân tích danh sách các tags lối sống
    val tagsList = remember(lifestyle) {
        val parsed = lifestyle.split(",", ";").map { it.trim() }.filter { it.isNotBlank() }
        if (parsed.isNotEmpty()) parsed else listOf("Cú đêm", "Gọn gàng", "Không hút thuốc", "Thích nấu ăn", "Cần riêng tư")
    }

    val locationAndBudget = remember(location, budget) {
        val loc = location.ifBlank { "TP.HCM" }
        val bgt = budget.ifBlank { "Thỏa thuận" }
        "$loc • $bgt"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(44.dp))

        // ── Top Bar: "Xem trước hồ sơ" + nút hoàn tất / Next (hình tròn check) ──
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

            // Nút Check / Next góc phải để chuyển tiếp tới trang chủ
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    .background(Color.White, CircleShape)
                    .clickable(enabled = !isLoading) {
                        onComplete(allPhotos)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color(0xFF2563EB),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = PhotoCheckIcon,
                        contentDescription = "Hoàn tất và tiếp tục",
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF2563EB)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Main Card (Cover image + overlay at bottom) ───────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            val isAddSlot = currentSlideIndex >= allPhotos.size

            if (isAddSlot) {
                // ── Màn hình dấu (+) để thêm ảnh mới ──────────────────
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFFE0E7FF), Color(0xFFCBD5E1))
                            )
                        )
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .background(Color(0xFF2563EB), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nhấn để thêm ảnh khác",
                            color = Color(0xFF1E3A8A),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Hoặc nhấn nút ✓ góc trên để sang trang chủ",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                // ── Hiển thị ảnh (ảnh đại diện hoặc ảnh bổ sung) ──────────
                val currentPhoto = allPhotos.getOrNull(currentSlideIndex)
                if (currentPhoto != null) {
                    AsyncImage(
                        model = currentPhoto,
                        contentDescription = "Ảnh ${currentSlideIndex + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
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
                }
            }

            // ── Vùng chạm: Trái (lùi ảnh) | Phải (ảnh tiếp hoặc slot +) ──
            Row(modifier = Modifier.fillMaxSize()) {
                // Nửa bên trái: lùi về ảnh trước
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (currentSlideIndex > 0) {
                                currentSlideIndex--
                            }
                        }
                )

                // Nửa bên phải: tiến tới ảnh tiếp theo hoặc tới slot (+)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (currentSlideIndex < totalSlots - 1) {
                                currentSlideIndex++
                            }
                        }
                )
            }

            // ── Top indicator (Thanh pill + dots) ─────────────────────────
            if (totalSlots > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until totalSlots) {
                        val isCurrent = i == currentSlideIndex
                        Box(
                            modifier = Modifier
                                .height(5.dp)
                                .weight(if (isCurrent) 2f else 1f)
                                .background(
                                    if (isCurrent) Color(0xFF2563EB) else Color.White.copy(alpha = 0.6f),
                                    CircleShape
                                )
                        )
                    }
                }
            }

            // ── Dark gradient overlay at bottom ───────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.88f))
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text = "${displayName.ifBlank { "Người dùng" }}, ${age.ifBlank { "20" }}",
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

                    // Hiển thị các tiêu chí lối sống (tối đa 2 hàng tag)
                    tagsList.chunked(3).take(2).forEach { rowTags ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            rowTags.forEach { tag ->
                                PreviewTagPill(tag)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoUploadScreenPreview() {
    HomEverTheme {
        PhotoUploadScreen(
            displayName = "Phạm Hữu Lầu",
            age = "20",
            location = "Thủ Đức",
            budget = "3–5 triệu/tháng"
        )
    }
}
