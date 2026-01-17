package com.k41s.scrollspree.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class Theme{
    LIGHT,
    DARK,
    BLACK_WHITE
}

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF85C4BE),
    onPrimary = Color.White,
    secondary = Color(0xFF336964),
    onSecondary = Color.White,
    tertiary = Color(0xFF000704),
    onTertiary = Color.White,
    background = Color.White
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF336964),
    onPrimary = Color.White,
    secondary = Color(0xFF85C4BE),
    onSecondary = Color.Black,
    tertiary = Color.White,
    onTertiary = Color.Black,
    background = Color(0xFF000704)
)

val BlackWhiteScheme = lightColorScheme(
    primary = Color(0xFF000704),
    onPrimary = Color.White,
    secondary = Color(0xFF000704),
    onSecondary = Color.White,
    tertiary = Color(0xFF000704),
    onTertiary = Color.White,
    background = Color.White
)

@Composable
fun AppTheme(
    selectedTheme: Theme = if (isSystemInDarkTheme()) Theme.DARK else Theme.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (selectedTheme) {
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
        Theme.BLACK_WHITE -> BlackWhiteScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}