package com.k41s.scrollspree

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.main.MainContent
import com.k41s.scrollspree.ui.main.MainViewModel
import com.k41s.scrollspree.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val mainViewModel: MainViewModel = koinViewModel()

    val savedTheme by mainViewModel.themePreference.collectAsState()

    AppTheme(savedTheme) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            MainContent()
        }
    }
}