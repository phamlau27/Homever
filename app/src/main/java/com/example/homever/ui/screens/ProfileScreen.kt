package com.example.homever.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.*

val ProfArrowBackIcon: ImageVector
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

val ProfKeyboardArrowDownIcon: ImageVector
    get() = ImageVector.Builder(
        name = "KeyboardArrowDown", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(7.41f, 8.59f); lineTo(12f, 13.17f); lineTo(16.59f, 8.59f)
            lineTo(18f, 10f); lineTo(12f, 16f); lineTo(6f, 10f); close()
        }
    }.build()

private val genderOptions = listOf("Nam", "Nữ", "Khác")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    avatarUri: Uri? = null,
    // Pre-fill from ViewModel when user navigates back
    initialDisplayName: String = "",
    initialGender: String = "",
    initialAge: String = "",
    initialOccupation: String = "",
    onAvatarSelected: (Uri?) -> Unit = {},
    onBack: () -> Unit = {},
    onComplete: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    var displayName by remember { mutableStateOf(initialDisplayName) }
    var gender by remember { mutableStateOf(initialGender) }
    var age by remember { mutableStateOf(initialAge) }
    var occupation by remember { mutableStateOf(initialOccupation) }
    var avatarUriState by remember { mutableStateOf<Uri?>(null) }
    var genderExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(avatarUri) { avatarUriState = avatarUri }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            avatarUriState = uri
            onAvatarSelected(uri)
        }
    )

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
                imageVector = ProfArrowBackIcon,
                contentDescription = "Quay lại",
                tint = HomeverTextDark
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Hồ sơ cơ bản",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Avatar ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(HomeverGrayBorder)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUriState != null) {
                    Text(
                        "✓",
                        color = BluePrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    // Placeholder camera icon
                    Text("📷", fontSize = 32.sp)
                }

                // "+" badge
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .background(BluePrimary, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "+",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = if (avatarUriState != null) "Đã chọn ảnh" else "Tải ảnh đại diện (tuỳ chọn)",
                color = BluePrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── Form ──────────────────────────────────────────────────────────
        ProfileInputField(
            label = "Tên hiển thị *",
            value = displayName,
            onValueChange = { displayName = it },
            placeholder = "Nhập tên của bạn"
        )

        Spacer(Modifier.height(16.dp))

        // Gender dropdown
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Giới tính *",
                fontSize = 13.sp,
                color = HomeverTextGray,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Chọn giới tính", color = Color(0xFFA0AAB0)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = HomeverGrayBorder,
                        focusedBorderColor = BluePrimary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    textStyle = TextStyle(fontSize = 15.sp, color = HomeverTextDark)
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        ProfileInputField(
            label = "Tuổi *",
            value = age,
            onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) age = it },
            placeholder = "Ví dụ: 24",
            keyboardType = KeyboardType.Number
        )

        Spacer(Modifier.height(16.dp))

        ProfileInputField(
            label = "Nghề nghiệp hoặc Trường học *",
            value = occupation,
            onValueChange = { occupation = it },
            placeholder = "Ví dụ: Sinh viên ĐH Bách Khoa"
        )

        // ── Error ─────────────────────────────────────────────────────────
        if (errorMessage != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = HomeverRed,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(32.dp))

        val isFormValid = displayName.isNotBlank() && gender.isNotBlank() &&
                age.isNotBlank() && occupation.isNotBlank()

        Button(
            onClick = { onComplete(displayName, gender, age, occupation) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(14.dp),
            enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Tiếp theo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = HomeverTextGray,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, color = Color(0xFFA0AAB0)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = HomeverGrayBorder,
                focusedBorderColor = BluePrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(fontSize = 15.sp, color = HomeverTextDark)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    com.example.homever.ui.theme.HomEverTheme {
        ProfileScreen()
    }
}
