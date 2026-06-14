package com.k41s.scrollspree.ui.screens.user.myOrders.orderDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.ui.components.orders.DetailRow
import com.k41s.scrollspree.ui.components.orders.InfoSection
import com.k41s.scrollspree.ui.components.orders.ProductThumbnail
import com.k41s.scrollspree.util.formatToString
import com.k41s.scrollspree.util.toCurrencyDisplay
import com.k41s.scrollspree.util.toDisplayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: Int,
    onBack: () -> Unit,
    onNavigateToProduct: (Int) -> Unit
) {
    val viewModel: OrderDetailViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> BasicLoadingScreen()
                state.errorMessage != null -> ErrorScreen(state.errorMessage!!) { viewModel.loadOrder(orderId) }
                state.order != null -> {
                    val order = state.order!!
                    val totalAmount = order.items.sumOf { it.price * it.quantity }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        item {
                            InfoSection(title = "Order Summary") {
                                DetailRow(label = "Order ID", value = order.id.toString())

                                val dateText = order.orderedAt?.formatToString() ?: "N/A"
                                DetailRow(label = "Placed on", value = dateText)

                                DetailRow(label = "Payment", value = order.paymentMethod.toDisplayName())
                                DetailRow(label = "Total", value = totalAmount.toCurrencyDisplay())
                            }
                        }

                        if (order.notes.isNotBlank()) {
                            item {
                                InfoSection(title = "Your Notes") {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = order.notes,
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Items",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        items(order.items) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToProduct(item.productId) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ProductThumbnail(
                                        mainImgId = item.mainImageId,
                                        contentDescription = item.productName
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.productName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Quantity: ${item.quantity}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = item.price.toCurrencyDisplay(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (item.isDeleted) {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                Text("Product Deleted/Discontinued")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}