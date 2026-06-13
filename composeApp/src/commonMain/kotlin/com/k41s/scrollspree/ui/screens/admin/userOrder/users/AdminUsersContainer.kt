package com.k41s.scrollspree.ui.screens.admin.userOrder.users

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.AdminOrderContainer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminUsersContainer() {

    val viewModel: AdminUsersViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var selectedUser by remember { mutableStateOf<User?>(null) }

    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = selectedUser != null
    ) {
        selectedUser = null
    }

    when (val state = uiState) {
        is AdminUsersUiState.Loading -> BasicLoadingScreen()
        is AdminUsersUiState.Error -> ErrorScreen(state.message) {
            viewModel.loadUsers()
        }
        is AdminUsersUiState.Success -> {
            Crossfade(targetState = selectedUser) { user ->
                if (user != null) {
                    AdminOrderContainer(user) {
                        selectedUser = null
                    }
                }
                else {
                    AdminUsersScreen(state.users, viewModel) {
                        selectedUser = it
                    }
                }
            }
        }
    }
}