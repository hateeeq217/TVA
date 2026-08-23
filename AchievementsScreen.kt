package com.tva.app.ui.achievements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tva.app.data.mock.MockUsageData
import com.tva.app.domain.model.Achievement
import com.tva.app.domain.statistics.StatisticsEngine

@Composable
fun AchievementsScreen(modifier: Modifier = Modifier) {
    val history = MockUsageData.last30Days
    val currentStreak = StatisticsEngine.currentStreak(history)
    val longestStreak = StatisticsEngine.longestStreak(history)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text("Achievements", style = MaterialTheme.typography.headlineMedium) }

        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(20.dp)) {
                    Text("Streak", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "$currentStreak consecutive day${if (currentStreak == 1) "" else "s"} under your target",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Longest streak: $longestStreak days",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(MockUsageData.achievements) { achievement ->
            AchievementRow(achievement)
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun AchievementRow(achievement: Achievement) {
    val unlocked = achievement.unlockedDate != null
    Card(shape = RoundedCornerShape(16.dp)) {
        Row(
            Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (unlocked) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (unlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column {
                Text(achievement.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (unlocked) {
                    Text(
                        "Unlocked ${achievement.unlockedDate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
