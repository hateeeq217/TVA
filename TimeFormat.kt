package com.tva.app.ui.common

/** "2h 17m" / "38m" style formatting used everywhere in the app. */
fun formatMinutes(totalMinutes: Int): String {
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return when {
        h > 0 && m > 0 -> "${h}h ${m}m"
        h > 0 -> "${h}h"
        else -> "${m}m"
    }
}

fun formatSignedMinutes(delta: Int): String {
    val sign = if (delta > 0) "+" else if (delta < 0) "−" else ""
    return "$sign${formatMinutes(kotlin.math.abs(delta))}"
}

fun formatPercent(value: Float, decimals: Int = 1): String {
    val factor = Math.pow(10.0, decimals.toDouble())
    val rounded = Math.round(value * factor) / factor
    return "${rounded}%"
}
