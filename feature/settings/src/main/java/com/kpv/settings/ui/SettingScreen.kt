package com.kpv.settings.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import com.kpv.settings.SettingsViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.logoutSuccess) {
        if (state.logoutSuccess) {
            viewModel.consumeLogoutSuccess()
            onLogout()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = StringsObject.settingsTitle.asString(),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            }
            state.error != null -> Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error!!.asString(),
                    color = Color(0xFFFFEB3B),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            state.userProfile != null -> {
                val profile = state.userProfile!!

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = StringsObject.profileSection.asString(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFB3E5FC)
                        )

                        ProfileRow(label = StringsObject.emailLabel, value = profile.email)
                        profile.telephoneNumber?.let { ProfileRow(label = StringsObject.phoneLabel, value = it) }
                        profile.telegramId?.let { ProfileRow(label = StringsObject.telegramLabel, value = it) }
                        ProfileRow(label = StringsObject.roleLabel, value = profile.roleName)
                    }
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.toggleLogoutDialog(true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935).copy(alpha = 0.15f),
                        contentColor = Color(0xFFE53935),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
                        disabledContentColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !state.isLoggingOut,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (state.isLoggingOut) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color(0xFFE53935))
                    } else {
                        Text(StringsObject.logoutButton.asString(), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleLogoutDialog(false) },
            title = { Text(StringsObject.logoutConfirm.asString(), color = Color.White) },
            confirmButton = {
                TextButton(onClick = { viewModel.logout() }) {
                    Text(StringsObject.logoutButton.asString(), color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.toggleLogoutDialog(false) }) {
                    Text(StringsObject.cancel.asString(), color = Color(0xFFB3E5FC))
                }
            },
            containerColor = Color(0xFF0D47A1).copy(alpha = 0.95f),
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ProfileRow(label: UiText, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label.asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFB3E5FC)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}