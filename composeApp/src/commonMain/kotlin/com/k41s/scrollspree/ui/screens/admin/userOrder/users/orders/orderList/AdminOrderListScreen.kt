package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.orderList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.ui.components.DateRangeSearch
import com.k41s.scrollspree.ui.components.orders.SharedOrderCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminOrderListScreen(
    user: User,
    onBack: () -> Unit,
    onOrderClick: (Order) -> Unit
) {
    val viewModel: AdminOrderListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initInitialOrders(user.orders ?: emptyList())
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colorScheme.primary
                    )
                }

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = "${user.fullName}'s Orders",
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${user.orders?.size ?: 0} total transactions",
                        style = typography.labelSmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = colorScheme.outlineVariant)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    DateRangeSearch(
                        onSearch = { start, end ->
                            if (user.id != null) {
                                viewModel.searchOrdersByDateRange(user.id, start, end)
                            }
                        }
                    )
                }
                when (val state = uiState) {
                    is AdminOrderListUiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is AdminOrderListUiState.Error -> {
                        item {
                            val errorMsg = state.message
                            if (errorMsg.contains("404")
                                || errorMsg.contains("No orders", ignoreCase = true)
                            ) {
                                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                    Text("No orders found for this time period.", color = colorScheme.onSurfaceVariant)
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                    Text(errorMsg, color = colorScheme.error)
                                }
                            }
                        }
                    }
                    is AdminOrderListUiState.Success -> {
                        val orders = state.orders
                        if (orders.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No orders found for this time period.", color = colorScheme.onSurfaceVariant)
                                }
                            }
                        } else {
                            items(items = orders, key = { it.id }) { order ->
                                SharedOrderCard(order) { onOrderClick(order) }
                            }
                        }
                    }
                }
            }
        }
    }
}