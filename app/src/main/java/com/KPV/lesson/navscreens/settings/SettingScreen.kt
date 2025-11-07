package com.kpv.lesson.navscreens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kpv.lesson.notifications.Importance
import com.kpv.lesson.notifications.NotificationHelper
import com.kpv.lesson.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }
    var expandable by remember { mutableStateOf(false) }
    var openApp by remember { mutableStateOf(false) }
    var withReply by remember { mutableStateOf(false) }

    var priorityExpanded by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf(Importance.MEDIUM) }
    var notificationId by remember { mutableStateOf(1) }

    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }

    val priorities = listOf(
        stringResource(R.string.high_priority) to Importance.HIGH,
        stringResource(R.string.medium_priority) to Importance.MEDIUM,
        stringResource(R.string.low_priority) to Importance.LOW,
        stringResource(R.string.min_priority) to Importance.MIN
    )

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.notification_settings),
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                label = { Text(stringResource(R.string.notification_title)) },
                modifier = Modifier.fillMaxWidth(),
                isError = titleError,
                singleLine = true
            )

            if (titleError) {
                Text(
                    text = stringResource(R.string.title_cannot_be_empty),
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.notification_text)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = !priorityExpanded }
            ) {
                OutlinedTextField(
                    value = priorities.find { it.second == selectedPriority }?.first ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.priority)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    priorities.forEach { (priorityName, importance) ->
                        DropdownMenuItem(
                            text = { Text(priorityName) },
                            onClick = {
                                selectedPriority = importance
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            RowSwitch(
                text = stringResource(R.string.expandable_notification),
                checked = expandable,
                onCheckedChange = { expandable = it },
                enabled = text.isNotEmpty()
            )

            RowSwitch(
                text = stringResource(R.string.open_app),
                checked = openApp,
                onCheckedChange = { openApp = it }
            )

            RowSwitch(
                text = stringResource(R.string.add_reply),
                checked = withReply,
                onCheckedChange = { withReply = it }
            )

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        notificationHelper.sendNotification(
                            id = notificationId,
                            title = title.trim(),
                            text = text.trim().takeIf { it.isNotEmpty() },
                            priority = selectedPriority,
                            expandable = expandable,
                            openApp = openApp,
                            withReply = withReply
                        )
                        notificationId++
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.send_notification))
            }
        }
    }
}

@Composable
private fun RowSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}