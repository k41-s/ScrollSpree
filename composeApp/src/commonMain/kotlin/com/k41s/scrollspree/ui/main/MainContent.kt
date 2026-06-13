package com.k41s.scrollspree.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import com.k41s.scrollspree.ui.screens.admin.AdminMainContainer
import com.k41s.scrollspree.ui.screens.splash.SplashScreen
import com.k41s.scrollspree.ui.screens.user.UserMainContainer
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent() {

    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.viewState.collectAsState()

    Crossfade(
        targetState = state,
        label = "AppMainTransition"
    ) { screenState ->
        when (screenState) {
            is AppViewState.Loading -> {
                SplashScreen()
            }
            is AppViewState.Unauthorized,
            is AppViewState.UserAuthenticated -> {
                UserMainContainer()
            }
            is AppViewState.AdminAuthenticated -> {
                AdminMainContainer {
                    viewModel.logout()
                }
            }
        }
    }
}
