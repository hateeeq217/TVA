package com.tva.app.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * A calm, restrained circular goal-progress indicator.
 * fraction may exceed 1f (over target) — the ring simply caps its visual fill at 1f
 * while the center label still shows the real percentage.
 */
@Composable
fun GoalProgressRing(
    fraction: Float,
    ringColor: Color,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    size: androidx.compose.ui.unit.Dp = 180.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 14.dp,
    centerContent: @Composable () -> Unit
) {
    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val diameter = size.toPx() - strokeWidth.toPx()
            val topLeft = androidx.compose.ui.geometry.Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2)
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * fraction.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke
            )
        }
        centerContent()
    }
}
