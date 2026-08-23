package com.tva.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = AccentTeal,
    background = InkBlack,
    surface = Charcoal,
    surfaceVariant = Slate,
    onBackground = Paper,
    onSurface = Paper,
    onSurfaceVariant = Mist,
    error = AccentCoral
)

private val LightColors = lightColorScheme(
    primary = AccentTeal,
    background = Paper,
    surface = Color.White,
    surfaceVariant = PaperDim,
    onBackground = InkBlack,
    onSurface = InkBlack,
    onSurfaceVariant = Mist,
    error = AccentCoral
)

enum class TvaThemeMode { LIGHT, DARK, SYSTEM }

@Composable
fun TvaTheme(
    themeMode: TvaThemeMode = TvaThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        TvaThemeMode.LIGHT -> false
        TvaThemeMode.DARK -> true
        TvaThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (useDark) DarkColors else LightColors,
        typography = TvaTypography,
        content = content
    )
}
