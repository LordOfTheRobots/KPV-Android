package com.kpv.lesson.navscreens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.kpv.lesson.notifications.NotificationHelper

@Composable
fun EditScreen() {
    var notificationId by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Редактирование уведомления",
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = notificationId,
                onValueChange = { notificationId = it },
                label = { Text("ID уведомления") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = newText,
                onValueChange = { newText = it },
                label = { Text("Новый текст уведомления") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            Button(
                onClick = {
                    if (notificationId.isBlank() || newText.isBlank()) {
                        Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            val id = notificationId.toInt()
                            val success = notificationHelper.updateNotification(id, newText)
                            if (success) {
                                Toast.makeText(context, "Уведомление обновлено", Toast.LENGTH_SHORT).show()
                                notificationId = ""
                                newText = ""
                            } else {
                                Toast.makeText(context, "Уведомление с таким ID не найдено", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: NumberFormatException) {
                            Toast.makeText(context, "ID должен быть числом", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Обновить уведомление")
            }

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = {
                    if (notificationHelper.hasActiveNotifications()) {
                        notificationHelper.cancelAllNotifications()
                        Toast.makeText(context, "Все уведомления удалены", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Нет активных уведомлений", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Удалить все уведомления")
            }
        }
    }
}