package com.kpv.bankcardsmanagement.auth.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kpv.bankcardsmanagement.auth.model.AuthCredentials
import com.kpv.bankcardsmanagement.auth.viewmodel.AuthResult
import com.kpv.bankcardsmanagement.auth.viewmodel.AuthViewModel
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.feature.core.brush.GlassCard
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToMain: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var screenMode by remember { mutableStateOf<AuthState>(AuthState.onLoggingScreen) }

    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<UiText?>(null) }

    val scope = rememberCoroutineScope()
    val isLoginMode by remember { derivedStateOf { screenMode == AuthState.onLoggingScreen } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(if (isLoading) 0.6f else 1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isLoginMode) StringsObject.loginTitle.asString()
                else StringsObject.registerTitle.asString(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    AuthTextField(
                        value = email,
                        onValueChange = { email = it; error = null },
                        label = StringsObject.email.asString(),
                        keyboardType = KeyboardType.Email,
                        enabled = !isLoading
                    )

                    Spacer(Modifier.height(16.dp))

                    AuthTextField(
                        value = password,
                        onValueChange = { password = it; error = null },
                        label = StringsObject.password.asString(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardType = KeyboardType.Password,
                        enabled = !isLoading
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    error = null
                    isLoading = true
                    val credentials = AuthCredentials(
                        email = email.trim(),
                        password = password
                    )

                    scope.launch {
                        val result = if (isLoginMode) {
                            viewModel.login(credentials)
                        } else {
                            viewModel.register(credentials)
                        }
                        isLoading = false

                        when (result) {
                            is AuthResult.Success -> {
                                onNavigateToMain()
                            }
                            is AuthResult.Error -> {
                                error = mapAuthExceptionToUiText(result.error)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0D47A1)
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF1565C0),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (isLoginMode) StringsObject.buttonLogin.asString()
                        else StringsObject.buttonRegister.asString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            error?.let { msg ->
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF000000).copy(alpha = 0.25f))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = msg.asString(),
                        color = Color(0xFFFFEB3B),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = {
                    screenMode = if (isLoginMode) AuthState.onSignInScreen
                    else AuthState.onLoggingScreen
                    error = null
                },
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoginMode) StringsObject.toggleToRegister.asString()
                    else StringsObject.toggleToLogin.asString(),
                    color = Color(0xFFE3F2FD),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = Color(0xFFB3E5FC).copy(
                    alpha = if (enabled) 0.9f else 0.4f
                )
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.12f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White.copy(alpha = 0.95f),
            focusedBorderColor = Color.White.copy(alpha = 0.6f),
            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
            cursorColor = Color.White,
            disabledContainerColor = Color.White.copy(alpha = 0.03f),
            disabledBorderColor = Color.White.copy(alpha = 0.1f),
            disabledTextColor = Color.White.copy(alpha = 0.3f)
        )
    )
}

sealed interface AuthState {
    data object onLoggingScreen : AuthState
    data object onSignInScreen : AuthState
}

private fun mapAuthExceptionToUiText(e: AuthException): UiText = when (e) {
    AuthException.InvalidCredentials -> StringsObject.errorInvalidCredentials
    AuthException.ShortPassword -> StringsObject.errorPasswordShort
    AuthException.NoDigits -> StringsObject.errorPasswordNoDigits
    AuthException.NoSpecialCharacters -> StringsObject.errorPasswordNoSpecialChars
    AuthException.NoLowerCaseLetters -> StringsObject.errorPasswordNoLowercase
    AuthException.NoUpperCaseLetters -> StringsObject.errorPasswordNoUppercase
    AuthException.NetworkError -> StringsObject.errorNetwork
    else -> StringsObject.errorUnknown
}