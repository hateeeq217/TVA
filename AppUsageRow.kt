package com.tva.app.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tva.app.domain.model.AppUsage

@Composable
fun AppUsageRow(app: AppUsage, maxMinutes: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(app.displayName, style = MaterialTheme.typography.titleMedium)
            Text(formatMinutes(app.minutes), style = MaterialTheme.typography.titleMedium)
        }
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 4.dp))
        LinearProgressIndicator(
            progress = { if (maxMinutes > 0) app.minutes.toFloat() / maxMinutes else 0f },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
