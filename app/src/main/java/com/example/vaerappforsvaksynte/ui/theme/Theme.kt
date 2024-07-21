package com.example.vaerappforsvaksynte.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OurOrange,
    onPrimary = Black,
    primaryContainer = DarkGrey,
    onPrimaryContainer = White,
    inversePrimary = White,
    secondary = OurDarkBlue,
    onSecondary = White,
    secondaryContainer = DarkGrey,
    onSecondaryContainer = White,
    tertiary = OurLightGrey,
    onTertiary = Black,
    tertiaryContainer = OurLightBlue,
    onTertiaryContainer = Black,

    background = White,
    onBackground = DarkGrey,
    surface = White,
    onSurface = DarkGrey,
    surfaceVariant = DarkGrey,
    onSurfaceVariant = White,
    surfaceTint = White,
    inverseSurface = White,
    inverseOnSurface = DarkGrey,
    outline = White
)

private val LightColorScheme = lightColorScheme(
    primary = OurOrange,
    onPrimary = Black,
    primaryContainer = DarkGrey,
    onPrimaryContainer = White,
    inversePrimary = White,
    secondary = OurDarkBlue,
    onSecondary = White,
    secondaryContainer = DarkGrey,
    onSecondaryContainer = White,
    tertiary = OurLightGrey,
    onTertiary = Black,
    tertiaryContainer = OurLightBlue,
    onTertiaryContainer = Black,

    background = White,
    onBackground = Black,
    surface = White,
    onSurface = DarkGrey,
    surfaceVariant = DarkGrey,
    onSurfaceVariant = White,
    surfaceTint = White,
    inverseSurface = White,
    inverseOnSurface = DarkGrey,
    outline = White
)

@Composable
fun VaerAppForSvaksynteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && darkTheme -> DarkColorScheme
        dynamicColor && !darkTheme -> LightColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}