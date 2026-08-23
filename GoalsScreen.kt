package com.tva.app.ui.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tva.app.data.mock.MockUsageData
import com.tva.app.domain.model.GoalState
import com.tva.app.domain.statistics.StatisticsEngine
import com.tva.app.ui.common.GoalProgressRing
import com.tva.app.ui.common.formatMinutes
import com.tva.app.ui.common.formatPercent
import com.tva.app.ui.theme.AccentAmber
import com.tva.app.ui.theme.AccentCoral
import com.tva.app.ui.theme.AccentTeal

private val presets = listOf(30, 60, 90, 120) // minutes

@Composable
fun GoalsScreen(modifier: Modifier = Modifier) {
    var goalMinutes by remember { mutableStateOf(MockUsageData.settings.dailyGoalMinutes) }
    var customText by remember { mutableStateOf("") }
    val today = MockUsageData.today

    val goal = StatisticsEngine.goalProgress(today.totalMinutes, goalMinutes)
    val color = when (goal.state) {
        GoalState.UNDER -> AccentTeal
        GoalState.APPROACHING -> AccentAmber
        GoalState.OVER -> AccentCoral
    }

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Goals", style = MaterialTheme.typography.headlineMedium)

        Card(shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                GoalProgressRing(fraction = goal.progressFraction, ringColor = color) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(formatMinutes(goal.currentMinutes), style = MaterialTheme.typography.headlineMedium)
                        Text("of ${formatMinutes(goalMinutes)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    if (goal.state == GoalState.OVER) "You've passed today's target."
                    else "${formatMinutes(goal.remainingMinutes)} remaining today",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column {
            Text("Daily target", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            FlowRowPresets(selected = goalMinutes, onSelect = { goalMinutes = it })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = customText,
                onValueChange = { input ->
                    customText = input.filter { it.isDigit() }
                    customText.toIntOrNull()?.let { if (it > 0) goalMinutes = it }
                },
                label = { Text("Custom target (minutes)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FlowRowPresets(selected: Int, onSelect: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        presets.forEach { minutes ->
            FilterChip(
                selected = selected == minutes,
                onClick = { onSelect(minutes) },
                label = { Text(formatMinutes(minutes)) }
            )
        }
    }
}
