package com.tva.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tva.app.data.mock.MockUsageData
import com.tva.app.domain.model.GoalState
import com.tva.app.domain.statistics.StatisticsEngine
import com.tva.app.ui.common.AppUsageRow
import com.tva.app.ui.common.GoalProgressRing
import com.tva.app.ui.common.formatMinutes
import com.tva.app.ui.common.formatPercent
import com.tva.app.ui.common.formatSignedMinutes
import com.tva.app.ui.theme.AccentAmber
import com.tva.app.ui.theme.AccentCoral
import com.tva.app.ui.theme.AccentGreen
import com.tva.app.ui.theme.AccentTeal

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val settings = MockUsageData.settings
    val today = MockUsageData.today
    val yesterday = MockUsageData.yesterday
    val last7 = MockUsageData.last7Days

    val pctOfDay = StatisticsEngine.percentageOfDay(today.totalMinutes)
    val pctOfWaking = StatisticsEngine.percentageOfWakingTime(
        today.totalMinutes, settings.wakeTime, settings.sleepTime
    )
    val delta = StatisticsEngine.minuteDelta(today.totalMinutes, yesterday.totalMinutes)
    val avg7 = StatisticsEngine.average(last7.map { it.totalMinutes })
    val pctVsAvg = StatisticsEngine.percentageChange(today.totalMinutes, avg7.toInt())
    val goal = StatisticsEngine.goalProgress(today.totalMinutes, settings.dailyGoalMinutes)

    val goalColor = when (goal.state) {
        GoalState.UNDER -> AccentTeal
        GoalState.APPROACHING -> AccentAmber
        GoalState.OVER -> AccentCoral
    }
    val goalMessage = when (goal.state) {
        GoalState.UNDER -> "You are under your target today."
        GoalState.APPROACHING -> "You're getting close to today's target."
        GoalState.OVER -> "You've passed today's target."
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column {
                Text("TODAY", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text(formatMinutes(today.totalMinutes), style = MaterialTheme.typography.displayLarge)
                Text(
                    "Short-form & social usage",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        formatPercent(StatisticsEngine.roundedPercent(pctOfDay)),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text("of your entire day", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (pctOfWaking != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${formatPercent(StatisticsEngine.roundedPercent(pctOfWaking))} of your estimated waking time",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            ComparisonCard(deltaMinutes = delta, pctVsAvg = pctVsAvg)
        }

        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(
                    Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Today's goal", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    GoalProgressRing(fraction = goal.progressFraction, ringColor = goalColor) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(formatPercent(StatisticsEngine.roundedPercent(goal.progressFraction * 100)), style = MaterialTheme.typography.headlineMedium)
                            Text(
                                if (goal.state == GoalState.OVER) "over target" else "${formatMinutes(goal.remainingMinutes)} left",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(goalMessage, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Column {
                Text("App breakdown", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                val maxMinutes = today.byApp.maxOf { it.minutes }
                today.byApp.sortedByDescending { it.minutes }.forEach { app ->
                    AppUsageRow(app = app, maxMinutes = maxMinutes)
                }
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun ComparisonCard(deltaMinutes: Int, pctVsAvg: Float?) {
    Card(shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ComparisonRow(
                label = "compared with yesterday",
                improved = deltaMinutes <= 0,
                text = "${formatSignedMinutes(deltaMinutes)}"
            )
            if (pctVsAvg != null) {
                ComparisonRow(
                    label = "compared with your 7-day average",
                    improved = pctVsAvg <= 0,
                    text = formatPercent(StatisticsEngine.roundedPercent(pctVsAvg))
                )
            }
        }
    }
}

@Composable
private fun ComparisonRow(label: String, improved: Boolean, text: String) {
    val color = if (improved) AccentGreen else AccentAmber
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = if (improved) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
            contentDescription = null,
            tint = color
        )
        Column {
            Text(text, style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.SemiBold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
