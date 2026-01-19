package com.k41s.scrollspree.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.screens.admin.AdminMainContainer
import com.k41s.scrollspree.ui.screens.auth.AuthContainer
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AppViewState.Unauthorized -> {
                AuthContainer()
            }
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
