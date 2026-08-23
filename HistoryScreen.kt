package com.tva.app.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tva.app.data.mock.MockUsageData
import com.tva.app.domain.model.DailyUsage
import com.tva.app.domain.statistics.StatisticsEngine
import com.tva.app.ui.common.formatMinutes
import com.tva.app.ui.common.formatPercent
import java.time.format.TextStyle
import java.util.Locale

private enum class Range(val label: String, val days: Int) {
    WEEK("7 days", 7), MONTH("30 days", 30), QUARTER("90 days", 90)
}

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    var range by remember { mutableStateOf(Range.WEEK) }
    val full = MockUsageData.last30Days // MVP mock data covers 30 days; 90-day range degrades gracefully
    val windowed = full.take(range.days)

    val total = windowed.sumOf { it.totalMinutes }
    val avg = StatisticsEngine.average(windowed.map { it.totalMinutes })
    val best = StatisticsEngine.bestDay(windowed)
    val highest = StatisticsEngine.highestDay(windowed)

    val prevWindow = full.drop(range.days).take(range.days)
    val changeVsPrevious = if (prevWindow.isNotEmpty()) {
        StatisticsEngine.percentageChange(total, prevWindow.sumOf { it.totalMinutes })
    } else null

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text("History", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(12.dp))
                SingleChoiceSegmented(range, onSelect = { range = it })
            }
        }

        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatLine("Total usage", formatMinutes(total))
                    StatLine("Daily average", formatMinutes(avg.toInt()))
                    best?.let { StatLine("Best day", "${it.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())} · ${formatMinutes(it.totalMinutes)}") }
                    highest?.let { StatLine("Highest day", "${it.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())} · ${formatMinutes(it.totalMinutes)}") }
                    changeVsPrevious?.let {
                        StatLine(
                            "Change vs. previous period",
                            "${if (it >= 0) "+" else ""}${formatPercent(StatisticsEngine.roundedPercent(it))}"
                        )
                    }
                }
            }
        }

        item {
            Column {
                Text("Trend", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                UsageBarChart(windowed.reversed())
            }
        }

        item {
            Column {
                Text("Daily detail", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
            }
        }
        items(windowed) { day -> DailyRow(day) }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun SingleChoiceSegmented(selected: Range, onSelect: (Range) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Range.values().forEach { r ->
            FilterChip(
                selected = selected == r,
                onClick = { onSelect(r) },
                label = { Text(r.label) }
            )
        }
    }
}

@Composable
private fun StatLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun DailyRow(day: DailyUsage) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " · " + day.date.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(formatMinutes(day.totalMinutes), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun UsageBarChart(days: List<DailyUsage>) {
    val maxMinutes = (days.maxOfOrNull { it.totalMinutes } ?: 1).coerceAtLeast(1)
    Row(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEach { day ->
            val fraction = day.totalMinutes.toFloat() / maxMinutes
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(fraction.coerceIn(0.03f, 1f))
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                    )
            )
        }
    }
}
