package com.k41s.scrollspree.ui.screens.user.mainTabs.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.components.ChangePasswordDialog
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.components.EditProfileDialog
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.components.GuestContent
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.components.ProfileContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserProfileScreen(
    onNavigateToMyOrders: () -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val viewModel: UserProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    actionColor = colorScheme.primary
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val isGuest = state.user == null &&
                    (state.errorMessage == "User session not found" || state.errorMessage == null)

            when {
                state.isLoading && state.user == null -> BasicLoadingScreen()

                isGuest -> {
                    GuestContent {
                        onNavigateToAuth()
                    }
                }

                state.errorMessage != null && state.user == null -> {
                    ErrorScreen(state.errorMessage!!) { viewModel.loadProfile() }
                }
                else -> {
                    ProfileContent(
                        state = state,
                        onEditClick = { viewModel.toggleEditDialog(true) },
                        onPasswordClick = { viewModel.togglePasswordDialog(true) },
                        onMyOrdersClick = onNavigateToMyOrders,
                        onDeleteClick = { viewModel.toggleDeleteDialog(true) }
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

            if (state.isDeleteDialogOpen) {
                AlertDialog(
                    onDismissRequest = { viewModel.toggleDeleteDialog(false) },
                    title = { Text("Delete Account") },
                    text = {
                        Text("Are you sure you want to delete your account? This action cannot be undone and will permanently erase your order history and profile data.")
                    },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorScheme.error
                            ),
                            onClick = viewModel::onDeleteProfileConfirmed
                        ) {
                            Text("Delete Permanently")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.toggleDeleteDialog(false) }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}