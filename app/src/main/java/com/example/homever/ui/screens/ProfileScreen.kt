package com.example.homever.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.homever.ui.theme.*
import java.util.Calendar


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

private val genderOptions = listOf("Nam", "Nữ")

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
    var showYearPicker by remember { mutableStateOf(false) }

    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    // Years from 1960 to currentYear - 16
    val years = remember { (currentYear - 16 downTo 1960).toList() }
    val selectedYear = remember(age) {
        age.toIntOrNull()?.let { ageVal -> currentYear - ageVal } ?: (currentYear - 22)
    }

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
                    AsyncImage(
                        model = avatarUriState,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
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
            placeholder = "Nhập tên của bạn",
            capitalization = KeyboardCapitalization.Words
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

        // Year of birth picker
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Năm sinh *",
                fontSize = 13.sp,
                color = HomeverTextGray,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = if (age.isNotBlank()) {
                    val ageVal = age.toIntOrNull()
                    if (ageVal != null) "${currentYear - ageVal}" else ""
                } else "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Chọn năm sinh", color = Color(0xFFA0AAB0)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showYearPicker)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showYearPicker = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = HomeverGrayBorder,
                    focusedBorderColor = BluePrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                textStyle = TextStyle(fontSize = 15.sp, color = HomeverTextDark),
                enabled = false
            )
            // Show age info
            if (age.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "(${age} tuổi)",
                    fontSize = 12.sp,
                    color = BluePrimary
                )
            }
        }

        // Year picker dialog
        if (showYearPicker) {

            val initialIdx = years.indexOf(selectedYear).coerceAtLeast(0)
            val listState = rememberLazyListState(initialFirstVisibleItemIndex = (initialIdx - 2).coerceAtLeast(0))

            AlertDialog(
                onDismissRequest = { showYearPicker = false },
                title = {
                    Text(
                        "Chọn năm sinh",
                        fontWeight = FontWeight.Bold,
                        color = TitleBlue
                    )
                },
                text = {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        items(years.size) { index ->
                            val year = years[index]
                            val isSelected = year == selectedYear
                            val computedAge = currentYear - year
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) Color(0xFFDDF4FF) else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        age = computedAge.toString()
                                        showYearPicker = false
                                    }
                                    .padding(vertical = 14.dp, horizontal = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = year.toString(),
                                        fontSize = 16.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) BluePrimary else HomeverTextDark
                                    )
                                    Text(
                                        text = "($computedAge tuổi)",
                                        fontSize = 14.sp,
                                        color = TextGray
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showYearPicker = false }) {
                        Text("Đóng", color = BluePrimary)
                    }
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        ProfileInputField(
            label = "Nghề nghiệp hoặc Trường học *",
            value = occupation,
            onValueChange = { occupation = it },
            placeholder = "Ví dụ: Sinh viên ĐH Bách Khoa",
            capitalization = KeyboardCapitalization.Sentences
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
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    singleLine: Boolean = true,
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
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = HomeverGrayBorder,
                focusedBorderColor = BluePrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = capitalization,
                autoCorrectEnabled = true
            ),
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
