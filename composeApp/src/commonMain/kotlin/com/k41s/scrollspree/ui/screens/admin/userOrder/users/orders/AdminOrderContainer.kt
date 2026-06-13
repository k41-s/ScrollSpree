package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.orderList.AdminOrderListScreen

@Composable
fun AdminOrderContainer(
    user: User,
    onBack: () -> Unit
){
    var selectedOrder by remember { mutableStateOf<Order?>(null) }

    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = true
    ) {
        if (selectedOrder != null) {
            selectedOrder = null
        } else {
            onBack()
        }
    }

    Crossfade(targetState = selectedOrder) { order ->
        if (order != null) {
            AdminOrderScreen(order) {
                selectedOrder = null
            }
        }
        else {
            AdminOrderListScreen(
                user = user,
                onBack = onBack
            ) {
                selectedOrder = it
            }
        }
    }
}