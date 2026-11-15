package com.example.homever.ui.screens

import com.example.homever.domain.model.LifestyleCriteria

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.*
import kotlinx.coroutines.launch

// ── Custom Vector Icons ──────────────────────────────────────────────

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

private val EyeIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Eye", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 4.5f)
            curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f)
            curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
            curveTo(17f, 19.5f, 21.27f, 16.39f, 23f, 12f)
            curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f)
            close()
            moveTo(12f, 17f)
            arcTo(5f, 5f, 0f, true, true, 12f, 7f)
            arcTo(5f, 5f, 0f, true, true, 12f, 17f)
            close()
            moveTo(12f, 9.5f)
            arcTo(2.5f, 2.5f, 0f, true, false, 12f, 14.5f)
            arcTo(2.5f, 2.5f, 0f, true, false, 12f, 9.5f)
            close()
        }
    }.build()

private val ClockIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Clock", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(11.99f, 2f)
            arcTo(10f, 10f, 0f, true, false, 12f, 22f)
            arcTo(10f, 10f, 0f, true, false, 11.99f, 2f)
            close()
            moveTo(12f, 20f)
            arcTo(8f, 8f, 0f, true, true, 12f, 4f)
            arcTo(8f, 8f, 0f, true, true, 12f, 20f)
            close()
            moveTo(12.5f, 7f)
            lineTo(11f, 7f)
            lineTo(11f, 13f)
            lineTo(16.25f, 16.15f)
            lineTo(17f, 14.92f)
            lineTo(12.5f, 12.25f)
            close()
        }
    }.build()

private val QuietIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Quiet", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(3f, 9f)
            lineTo(3f, 15f)
            lineTo(7f, 15f)
            lineTo(12f, 20f)
            lineTo(12f, 4f)
            lineTo(7f, 9f)
            lineTo(3f, 9f)
            close()
            moveTo(16.5f, 12f)
            arcTo(4.5f, 4.5f, 0f, false, false, 14f, 7.97f)
            lineTo(14f, 16.02f)
            arcTo(4.45f, 4.45f, 0f, false, false, 16.5f, 12f)
            close()
        }
    }.build()

private val TidinessIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Tidiness", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 2f)
            arcTo(10f, 10f, 0f, true, false, 12f, 22f)
            arcTo(10f, 10f, 0f, true, false, 12f, 2f)
            close()
            moveTo(12f, 20f)
            arcTo(8f, 8f, 0f, true, true, 12f, 4f)
            arcTo(8f, 8f, 0f, true, true, 12f, 20f)
            close()
            moveTo(12.5f, 7f)
            lineTo(11f, 7f)
            lineTo(11f, 13f)
            lineTo(16.25f, 16.15f)
            lineTo(17f, 14.92f)
            lineTo(12.5f, 12.25f)
            close()
        }
    }.build()

private val SmokingIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Smoking", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(3f, 10f)
            lineTo(3f, 14f)
            lineTo(6f, 14f)
            lineTo(10f, 18f)
            lineTo(10f, 6f)
            lineTo(6f, 10f)
            lineTo(3f, 10f)
            close()
            moveTo(14f, 8.5f)
            lineTo(12.5f, 10f)
            lineTo(14.5f, 12f)
            lineTo(12.5f, 14f)
            lineTo(14f, 15.5f)
            lineTo(16f, 13.5f)
            lineTo(18f, 15.5f)
            lineTo(19.5f, 14f)
            lineTo(17.5f, 12f)
            lineTo(19.5f, 10f)
            lineTo(18f, 8.5f)
            lineTo(16f, 10.5f)
            close()
        }
    }.build()

private val PetsIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Pets", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(12f, 11f)
            curveTo(9.8f, 11f, 8f, 12.8f, 8f, 15f)
            curveTo(8f, 17.5f, 10f, 20.5f, 12f, 20.5f)
            curveTo(14f, 20.5f, 16f, 17.5f, 16f, 15f)
            curveTo(16f, 12.8f, 14.2f, 11f, 12f, 11f)
            close()
            moveTo(4.5f, 13.5f)
            arcTo(2f, 2f, 0f, true, false, 4.5f, 9.5f)
            arcTo(2f, 2f, 0f, true, false, 4.5f, 13.5f)
            close()
            moveTo(8.5f, 8.5f)
            arcTo(2f, 2f, 0f, true, false, 8.5f, 4.5f)
            arcTo(2f, 2f, 0f, true, false, 8.5f, 8.5f)
            close()
            moveTo(15.5f, 8.5f)
            arcTo(2f, 2f, 0f, true, false, 15.5f, 4.5f)
            arcTo(2f, 2f, 0f, true, false, 15.5f, 8.5f)
            close()
            moveTo(19.5f, 13.5f)
            arcTo(2f, 2f, 0f, true, false, 19.5f, 9.5f)
            arcTo(2f, 2f, 0f, true, false, 19.5f, 13.5f)
            close()
        }
    }.build()

private val GuestsIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Guests", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(16.5f, 12f)
            arcTo(3.5f, 3.5f, 0f, true, false, 16.5f, 5f)
            arcTo(3.5f, 3.5f, 0f, true, false, 16.5f, 12f)
            close()
            moveTo(8.5f, 11f)
            arcTo(3f, 3f, 0f, true, false, 8.5f, 5f)
            arcTo(3f, 3f, 0f, true, false, 8.5f, 11f)
            close()
            moveTo(8.5f, 13f)
            curveTo(6.33f, 13f, 2f, 14.17f, 2f, 16.5f)
            lineTo(2f, 19f)
            lineTo(15f, 19f)
            lineTo(15f, 16.5f)
            curveTo(15f, 14.17f, 10.67f, 13f, 8.5f, 13f)
            close()
            moveTo(16.5f, 14f)
            curveTo(16.18f, 14f, 15.79f, 14.04f, 15.37f, 14.11f)
            curveTo(16.48f, 14.94f, 17f, 16.03f, 17f, 17.5f)
            lineTo(17f, 19f)
            lineTo(22f, 19f)
            lineTo(22f, 16.5f)
            curveTo(22f, 14.83f, 18.67f, 14f, 16.5f, 14f)
            close()
        }
    }.build()

private val PrivacyIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Privacy", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(18f, 8f)
            lineTo(17f, 8f)
            lineTo(17f, 6f)
            curveTo(17f, 3.24f, 14.76f, 1f, 12f, 1f)
            curveTo(9.24f, 1f, 7f, 3.24f, 7f, 6f)
            lineTo(7f, 8f)
            lineTo(6f, 8f)
            curveTo(4.9f, 8f, 4f, 8.9f, 4f, 10f)
            lineTo(4f, 20f)
            curveTo(4f, 21.1f, 4.9f, 22f, 6f, 22f)
            lineTo(18f, 22f)
            curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f)
            lineTo(20f, 10f)
            curveTo(20f, 8.9f, 19.1f, 8f, 18f, 8f)
            close()
            moveTo(9f, 6f)
            curveTo(9f, 4.34f, 10.34f, 3f, 12f, 3f)
            curveTo(13.66f, 3f, 15f, 4.34f, 15f, 6f)
            lineTo(15f, 8f)
            lineTo(9f, 8f)
            close()
            moveTo(12f, 17f)
            arcTo(2f, 2f, 0f, true, true, 12f, 13f)
            arcTo(2f, 2f, 0f, true, true, 12f, 17f)
            close()
        }
    }.build()

private val CookingIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Cooking", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(11f, 9f); lineTo(9f, 9f); lineTo(9f, 2f); lineTo(7f, 2f); lineTo(7f, 9f); lineTo(5f, 9f); lineTo(5f, 2f); lineTo(3f, 2f); lineTo(3f, 9f)
            curveTo(3f, 11.21f, 4.79f, 13f, 7f, 13f); lineTo(7f, 22f); lineTo(9f, 22f); lineTo(9f, 13f)
            curveTo(11.21f, 13f, 13f, 11.21f, 13f, 9f); lineTo(13f, 2f); lineTo(11f, 2f); close()
            moveTo(17f, 2f); lineTo(15f, 2f); lineTo(15f, 9f); curveTo(15f, 11.21f, 16.79f, 13f, 19f, 13f)
            lineTo(19f, 22f); lineTo(21f, 22f); lineTo(21f, 2f); curveTo(18.79f, 2f, 17f, 3.79f, 17f, 6f); close()
        }
    }.build()

private val ResponsibilityIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Responsibility", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(18f, 16.08f)
            curveTo(17.24f, 16.08f, 16.56f, 16.38f, 16.04f, 16.85f)
            lineTo(8.91f, 12.7f)
            curveTo(8.96f, 12.47f, 9f, 12.24f, 9f, 12f)
            curveTo(9f, 11.76f, 8.96f, 11.53f, 8.91f, 11.3f)
            lineTo(15.96f, 7.19f)
            curveTo(16.5f, 7.69f, 17.21f, 8f, 18f, 8f)
            arcTo(3f, 3f, 0f, true, false, 18f, 2f)
            arcTo(3f, 3f, 0f, true, false, 18f, 8f)
            close()
            moveTo(6f, 15f)
            arcTo(3f, 3f, 0f, true, false, 6f, 9f)
            arcTo(3f, 3f, 0f, true, false, 6f, 15f)
            close()
            moveTo(18f, 22f)
            arcTo(3f, 3f, 0f, true, false, 18f, 16f)
            arcTo(3f, 3f, 0f, true, false, 18f, 22f)
            close()
        }
    }.build()

private val WalletIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Wallet", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(BluePrimary)) {
            moveTo(21f, 18f); lineTo(21f, 6f); curveTo(21f, 4.9f, 20.1f, 4f, 19f, 4f); lineTo(5f, 4f)
            curveTo(3.9f, 4f, 3f, 4.9f, 3f, 6f); lineTo(3f, 18f); curveTo(3f, 19.1f, 3.9f, 20f, 5f, 20f)
            lineTo(19f, 20f); curveTo(20.1f, 20f, 21f, 19.1f, 21f, 18f); close()
            moveTo(5f, 6f); lineTo(19f, 6f); lineTo(19f, 8f); lineTo(5f, 8f); close()
            moveTo(19f, 18f); lineTo(5f, 18f); lineTo(5f, 10f); lineTo(19f, 10f); close()
            moveTo(16f, 15f); arcTo(1.5f, 1.5f, 0f, true, false, 16f, 12f); arcTo(1.5f, 1.5f, 0f, true, false, 16f, 15f); close()
        }
    }.build()

// ── Screen Component ─────────────────────────────────────────────────

@Composable
fun LifestyleScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    // Current step (1, 2, or 3)
    var currentStep by remember { mutableStateOf(1) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // ── Step 1 Choices ───────────────────────────────────────────────
    var genderPref by remember { mutableStateOf("") }
    var schedulePref by remember { mutableStateOf("") }
    var noisePref by remember { mutableStateOf("") }
    var tidinessPref by remember { mutableStateOf("") }
    var smokingPref by remember { mutableStateOf("") }

    // ── Step 2 Choices ───────────────────────────────────────────────
    var petsPref by remember { mutableStateOf("") }
    var guestsPref by remember { mutableStateOf("") }
    var privacyPref by remember { mutableStateOf("") }

    // ── Step 3 Choices ───────────────────────────────────────────────
    var cookingPref by remember { mutableStateOf("") }
    var responsibilityPref by remember { mutableStateOf("") }
    var costSharingPref by remember { mutableStateOf("") }

    // Android back button intercepts inside multi-step flow
    BackHandler(enabled = currentStep > 1) {
        currentStep--
        coroutineScope.launch { scrollState.animateScrollTo(0) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        // ── Top Bar ───────────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                if (currentStep > 1) {
                    currentStep--
                    coroutineScope.launch { scrollState.animateScrollTo(0) }
                } else {
                    onBack()
                }
            }
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

        // ── Sub-header / Progress title ───────────────────────────────────
        Text(
            text = "BƯỚC $currentStep/3",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = BluePrimary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Lối sống của bạn thế nào?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Chọn một đáp án cho mỗi câu hỏi. Bạn có thể cuộn để xem tất cả.",
            fontSize = 13.sp,
            color = TextGray,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(20.dp))

        // ── Progress Bar ──────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Tiến độ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = HomeverTextDark
            )
            Spacer(Modifier.weight(1f))
            Text(
                "$currentStep/3",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(3) { index ->
                val isFilled = (index + 1) <= currentStep
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            if (isFilled) BluePrimary else Color(0xFFE5E7EB),
                            CircleShape
                        )
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Step Content with Smooth Fade Transitions ─────────────────────
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "StepContent"
        ) { step ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                when (step) {
                    1 -> {
                        // 1. Bạn muốn thấy ai?
                        LifestyleQuestionCard(
                            icon = EyeIcon,
                            title = "Bạn muốn thấy ai?",
                            subtitle = "Chọn đối tượng bạn muốn xem",
                            selectedValue = genderPref,
                            onSelect = { genderPref = it },
                            rows = listOf(listOf("Nam", "Nữ", "Mọi người"))
                        )

                        // 2. Giờ sinh hoạt
                        LifestyleQuestionCard(
                            icon = ClockIcon,
                            title = "Giờ sinh hoạt",
                            subtitle = "Chọn khung giờ phù hợp nhất",
                            selectedValue = schedulePref,
                            onSelect = { schedulePref = it },
                            rows = listOf(
                                listOf("Dậy sớm", "Bình thường", "Cú đêm"),
                                listOf("Linh hoạt")
                            )
                        )

                        // 3. Không gian sống
                        LifestyleQuestionCard(
                            icon = QuietIcon,
                            title = "Không gian sống",
                            subtitle = "Mức độ yên tĩnh bạn mong muốn",
                            selectedValue = noisePref,
                            onSelect = { noisePref = it },
                            rows = listOf(
                                listOf("Rất yên tĩnh", "Khá yên tĩnh"),
                                listOf("Bình thường", "Không ngại ồn")
                            )
                        )

                        // 4. Gọn gàng
                        LifestyleQuestionCard(
                            icon = TidinessIcon,
                            title = "Gọn gàng",
                            subtitle = "Phong cách sắp xếp không gian của bạn",
                            selectedValue = tidinessPref,
                            onSelect = { tidinessPref = it },
                            rows = listOf(
                                listOf("Rất gọn gàng", "Khá gọn gàng"),
                                listOf("Bình thường", "Khá thoải mái")
                            )
                        )

                        // 5. Hút thuốc
                        LifestyleQuestionCard(
                            icon = SmokingIcon,
                            title = "Hút thuốc",
                            subtitle = "Tình trạng hút thuốc của bạn",
                            selectedValue = smokingPref,
                            onSelect = { smokingPref = it },
                            rows = listOf(
                                listOf("Không hút", "Thỉnh thoảng"),
                                listOf("Thường xuyên")
                            )
                        )
                    }
                    2 -> {
                        // 6. Thú cưng
                        LifestyleQuestionCard(
                            icon = PetsIcon,
                            title = "Thú cưng",
                            subtitle = "Chó, mèo hoặc động vật khác",
                            selectedValue = petsPref,
                            onSelect = { petsPref = it },
                            rows = listOf(
                                listOf("Rất thích", "Có thể sống cùng"),
                                listOf("Không quan tâm", "Không muốn ở cùng")
                            )
                        )

                        // 7. Khách đến phòng
                        LifestyleQuestionCard(
                            icon = GuestsIcon,
                            title = "Khách đến phòng",
                            subtitle = "Tần suất bạn tiếp khách",
                            selectedValue = guestsPref,
                            onSelect = { guestsPref = it },
                            rows = listOf(
                                listOf("Hầu như không", "Thỉnh thoảng"),
                                listOf("Cuối tuần", "Thường xuyên")
                            )
                        )

                        // 8. Riêng tư
                        LifestyleQuestionCard(
                            icon = PrivacyIcon,
                            title = "Riêng tư",
                            subtitle = "Bạn cần bao nhiêu không gian riêng",
                            selectedValue = privacyPref,
                            onSelect = { privacyPref = it },
                            rows = listOf(
                                listOf("Rất cần riêng tư", "Cân bằng"),
                                listOf("Thích sinh hoạt chung")
                            )
                        )
                    }
                    3 -> {
                        // 9. Nấu ăn
                        LifestyleQuestionCard(
                            icon = CookingIcon,
                            title = "Nấu ăn",
                            subtitle = "Tần suất bạn tự nấu ăn",
                            selectedValue = cookingPref,
                            onSelect = { cookingPref = it },
                            rows = listOf(
                                listOf("Thường xuyên", "Thỉnh thoảng"),
                                listOf("Chủ yếu mua ngoài", "Hầu như không nấu")
                            )
                        )

                        // 10. Trách nhiệm với việc chung
                        LifestyleQuestionCard(
                            icon = ResponsibilityIcon,
                            title = "Trách nhiệm với việc chung",
                            subtitle = "Bạn sẵn sàng chia sẻ đồ dùng ra sao",
                            selectedValue = responsibilityPref,
                            onSelect = { responsibilityPref = it },
                            rows = listOf(
                                listOf("Rất chủ động", "Thích phân công rõ ràng"),
                                listOf("Linh hoạt theo tình huống", "Bình thường")
                            )
                        )

                        // 11. Chia chi phí
                        LifestyleQuestionCard(
                            icon = WalletIcon,
                            title = "Chia chi phí",
                            subtitle = "Cách bạn muốn chia chi phí chung",
                            selectedValue = costSharingPref,
                            onSelect = { costSharingPref = it },
                            rows = listOf(
                                listOf("Chia đều", "Theo sử dụng"),
                                listOf("Tự thanh toán", "Linh hoạt")
                            )
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(36.dp))

        // ── Action Button ─────────────────────────────────────────────────
        Button(
            onClick = {
                if (currentStep < 3) {
                    currentStep++
                    coroutineScope.launch { scrollState.animateScrollTo(0) }
                } else {
                    // Combine all answers cleanly
                    val combinedTraits = LifestyleCriteria.encode(listOf(
                        genderPref,
                        schedulePref,
                        noisePref,
                        tidinessPref,
                        smokingPref,
                        petsPref,
                        guestsPref,
                        privacyPref,
                        cookingPref,
                        responsibilityPref,
                        costSharingPref
                    ))

                    onNext(combinedTraits)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text(
                text = "Tiếp tục",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

// ── Reusable Lifestyle Question Card ─────────────────────────────────

@Composable
fun LifestyleQuestionCard(
    icon: ImageVector,
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
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFEFF6FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = BluePrimary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = HomeverTextDark
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rows.flatten().forEach { text ->
                    val isSelected = text.isNotBlank() && text == selectedValue
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) Color(0xFFDDF4FF) else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) BluePrimary else Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onSelect(text) }
                            .padding(horizontal = 16.dp, vertical = 9.dp)
                    ) {
                        Text(
                            text = LifestyleCriteria.labels("${title.removeSuffix("?")}: $text").single(),
                            fontSize = 13.sp,
                            color = if (isSelected) BluePrimary else Color(0xFF4B5563),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
