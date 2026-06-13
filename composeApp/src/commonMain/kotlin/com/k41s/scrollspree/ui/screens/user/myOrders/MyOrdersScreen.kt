package com.k41s.scrollspree.ui.screens.user.myOrders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.DateRangeSearch
import com.k41s.scrollspree.ui.screens.user.myOrders.components.OrderCard
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    onBack: () -> Unit,
    onOrderClick: (Int) -> Unit
) {
    val viewModel: MyOrdersViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    val imageLoader = koinInject<ImageLoader>()

    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "My Purchase History",
                style = typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        HorizontalDivider(thickness = 0.5.dp)

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {
                DateRangeSearch(
                    onSearch = { start, end ->
                        viewModel.searchOrdersByDateRange(start, end)
                    }
                )
            }

            when (val state = state) {
                is MyOrdersUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is MyOrdersUiState.Error -> {
                    item {
                        val errorMsg = state.message
                        if (errorMsg.contains("404") ||
                            errorMsg.contains("No orders", ignoreCase = true)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No orders found for this time period.",
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp)
                                , contentAlignment = Alignment.Center
                            ) {
                                Text(errorMsg, color = colorScheme.error)
                            }
                        }
                    }
                }
                is MyOrdersUiState.Success -> {
                    val orders = state.orders
                    if (orders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No orders found.", color = colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(items = orders, key = { it.id }) { order ->
                            OrderCard(order = order, imageLoader = imageLoader, onClick = onOrderClick)
                        }
                    }
                }
            }
        }
    }
}