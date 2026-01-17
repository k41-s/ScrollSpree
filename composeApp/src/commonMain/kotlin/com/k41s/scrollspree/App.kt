package com.k41s.scrollspree

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.main.MainContent
import com.k41s.scrollspree.ui.theme.AppTheme
import com.k41s.scrollspree.ui.theme.Theme

@Composable
fun App() {
    val systemIsDark = isSystemInDarkTheme()
    val currentTheme = if (systemIsDark) Theme.DARK else Theme.LIGHT

    AppTheme(currentTheme) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            MainContent()
        }
    }
}