package com.k41s.scrollspree.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class Theme{
    LIGHT,
    DARK,
    BLACK_WHITE,
    SYSTEM
}

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF85C4BE),
    onPrimary = Color.White,
    secondary = Color(0xFF336964),
    onSecondary = Color.White,
    tertiary = Color(0xFF000704),
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF000704),
    error = Color(0xFF610404)
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF336964),
    onPrimary = Color.White,
    secondary = Color(0xFF85C4BE),
    onSecondary = Color.Black,
    tertiary = Color.White,
    onTertiary = Color.Black,
    background = Color(0xFF000704),
    onBackground = Color.White,
    error = Color(0xFF610404)
)

val BlackWhiteScheme = lightColorScheme(
    primary = Color(0xFF000704),
    onPrimary = Color.White,
    secondary = Color(0xFF000704),
    onSecondary = Color.White,
    tertiary = Color(0xFF000704),
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF000704),
    error = Color(0xFF610404)
)

@Composable
fun AppTheme(
    selectedTheme: Theme = Theme.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()

    val effectiveTheme = when (selectedTheme) {
        Theme.SYSTEM -> if (systemIsDark) Theme.DARK else Theme.LIGHT
        else -> selectedTheme
    }

    val colorScheme = when (effectiveTheme) {
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
        Theme.BLACK_WHITE -> BlackWhiteScheme
        Theme.SYSTEM -> if (systemIsDark) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}