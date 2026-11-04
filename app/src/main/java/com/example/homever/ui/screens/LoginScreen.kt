package com.example.homever.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

val GoogleIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Google",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            stroke = SolidColor(BluePrimary),
            strokeLineWidth = 2f
        ) {
            moveTo(12f, 22f)
            arcTo(10f, 10f, 0f, true, false, 12f, 2f)
            arcTo(10f, 10f, 0f, true, false, 12f, 22f)
            close()
            moveTo(8f, 12f)
            lineTo(16f, 12f)
            moveTo(12f, 8f)
            lineTo(16f, 12f)
            lineTo(12f, 16f)
        }
    }.build()

val FacebookIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Facebook",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            stroke = SolidColor(BluePrimary),
            strokeLineWidth = 2f
        ) {
            moveTo(14f, 2f)
            lineTo(12f, 2f)
            arcTo(4f, 4f, 0f, false, false, 8f, 6f)
            lineTo(8f, 10f)
            moveTo(6f, 10f)
            lineTo(14f, 10f)
            moveTo(10f, 10f)
            lineTo(10f, 22f)
        }
    }.build()

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onContinueClick: (String, String) -> Unit = { _, _ -> },
    onGoogleLoginClick: () -> Unit = {}
) {
    var emailOrPhone by remember { mutableStateOf("") }
    var selectedCountryCode by remember { mutableStateOf("+84") }
    var expandedCountryCode by remember { mutableStateOf(false) }

    val isPhoneNumber = emailOrPhone.all { it.isDigit() || it == '+' || it == ' ' } && emailOrPhone.isNotBlank()

    val countryCodes = listOf("+84" to "🇻🇳 VN", "+1" to "🇺🇸 US", "+44" to "🇬🇧 UK", "+81" to "🇯🇵 JP")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = "Xin chào bạn!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Đăng nhập hoặc tạo tài khoản mới để tiếp tục",
            fontSize = 14.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Số điện thoại hoặc Email",
            fontSize = 14.sp,
            color = TextGray,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isPhoneNumber) {
                Box {
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                            .clickable { expandedCountryCode = true }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedCountryCode, fontSize = 14.sp, color = Color(0xFF1F2937), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = GoogleIcon, contentDescription = null, modifier = Modifier.size(0.dp)) // Placeholder
                        Text("▼", fontSize = 10.sp, color = Color.Gray)
                    }

                    DropdownMenu(
                        expanded = expandedCountryCode,
                        onDismissRequest = { expandedCountryCode = false }
                    ) {
                        countryCodes.forEach { (code, label) ->
                            DropdownMenuItem(
                                text = { Text("\$label (\$code)") },
                                onClick = {
                                    selectedCountryCode = code
                                    expandedCountryCode = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it },
                placeholder = {
                    Text(
                        text = "Nhập số điện thoại hoặc email của bạn...",
                        color = Color(0xFFA0AAB0),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedBorderColor = BluePrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isPhoneNumber) KeyboardType.Phone else KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onContinueClick(emailOrPhone, selectedCountryCode) },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(28.dp),
            enabled = !isLoading && emailOrPhone.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Tiếp tục",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
            Text(
                text = "Hoặc kết nối qua",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = TextGray,
                fontSize = 12.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
        }

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedButton(
            onClick = onGoogleLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Icon(
                imageVector = GoogleIcon,
                contentDescription = "Google",
                modifier = Modifier.size(20.dp),
                tint = BluePrimary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Tiếp tục với Google",
                color = Color(0xFF1F2937),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onGoogleLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Icon(
                imageVector = FacebookIcon,
                contentDescription = "Facebook",
                modifier = Modifier.size(20.dp),
                tint = BluePrimary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Tiếp tục với Facebook",
                color = Color(0xFF1F2937),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Bằng cách tiếp tục, bạn đồng ý với Điều khoản dịch vụ.",
            fontSize = 12.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    HomEverTheme {
        LoginScreen()
    }
}