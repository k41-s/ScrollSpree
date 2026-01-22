package com.k41s.scrollspree.ui.screens.user.mainTabs.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.components.ChangePasswordDialog
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.components.EditProfileDialog
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.components.ProfileContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserProfileScreen() {
    val viewModel: UserProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading && state.user == null -> BasicLoadingScreen()
                state.errorMessage != null && state.user == null -> {
                    ErrorScreen(state.errorMessage!!) { viewModel.loadProfile() }
                }
                else -> {
                    ProfileContent(
                        state = state,
                        onEditClick = { viewModel.toggleEditDialog(true) },
                        onPasswordClick = { viewModel.togglePasswordDialog(true) }
                    )
                }
            }

            if (state.isEditDialogVisible) {
                EditProfileDialog(
                    state = state,
                    onDismiss = { viewModel.toggleEditDialog(false) },
                    onSave = viewModel::onSaveProfileClicked
                )
            }

            if (state.isPasswordDialogVisible) {
                ChangePasswordDialog(
                    state = state,
                    onOldChange = viewModel::onOldPasswordChanged,
                    onNewChange = viewModel::onNewPasswordChanged,
                    onConfirmChange = viewModel::onConfirmPasswordChanged,
                    onDismiss = { viewModel.togglePasswordDialog(false) },
                    onSave = viewModel::onChangePasswordClicked
                )
            }
        }
    }
}