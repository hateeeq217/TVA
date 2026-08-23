package com.tva.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tva.app.data.mock.MockUsageData

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    var notificationsEnabled by remember { mutableStateOf(MockUsageData.settings.notificationsEnabled) }
    var aiInsightsEnabled by remember { mutableStateOf(MockUsageData.settings.aiInsightsEnabled) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text("Settings", style = MaterialTheme.typography.headlineMedium) }

        item {
            SettingsGroup(title = "Tracking") {
                SettingsRow(title = "Tracked apps", subtitle = "Instagram, TikTok, YouTube")
                SettingsRow(title = "Wake time", subtitle = MockUsageData.settings.wakeTime.toString())
                SettingsRow(title = "Sleep time", subtitle = MockUsageData.settings.sleepTime.toString())
            }
        }

        item {
            SettingsGroup(title = "Notifications") {
                SettingsToggleRow(
                    title = "Enable notifications",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
        }

        item {
            SettingsGroup(title = "AI Insights") {
                SettingsToggleRow(
                    title = "Enable AI-generated insights",
                    checked = aiInsightsEnabled,
                    onCheckedChange = { aiInsightsEnabled = it }
                )
                Text(
                    "When enabled, only aggregated daily statistics are sent for analysis — never raw activity logs or personal content.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        item {
            SettingsGroup(title = "Privacy") {
                Text(
                    "TVA needs usage access to measure how much time you spend in supported apps. " +
                        "TVA does not need access to your messages, passwords, photos, or private conversations. " +
                        "Usage statistics stay on your device.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { /* export flow — Phase 3 */ }) { Text("Export my data") }
            }
        }

        item {
            SettingsGroup(title = "Appearance") {
                SettingsRow(title = "Theme", subtitle = "System")
            }
        }

        item {
            SettingsGroup(title = "About") {
                SettingsRow(title = "Version", subtitle = "0.1.0 (MVP prototype)")
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { showDeleteConfirm = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete all data") }
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete all data?") },
            text = { Text("This removes all locally stored usage history, goals, and achievements. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(16.dp), content = content)
        }
    }
}

@Composable
private fun SettingsRow(title: String, subtitle: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SettingsToggleRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
