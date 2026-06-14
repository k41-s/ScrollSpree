package com.k41s.scrollspree.ui.screens.user.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.enums.PaymentMethod
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.util.toCurrencyDisplay
import com.k41s.scrollspree.util.toDisplayName
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    productId: Int?,
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    val viewModel: CheckoutViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(productId) {
        viewModel.loadInitialData(productId)
    }

    LaunchedEffect(state.isOrderPlaced) {
        if (state.isOrderPlaced) {

            state.paypalRedirectUrl?.let { url ->
                try {
                    uriHandler.openUri(url)
                } catch (e: Exception) {
                    println("Failed to open browser: ${e.message}")
                }
            }

            onOrderPlaced()
            viewModel.resetState()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Checkout") },
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
                state.isLoading && state.cartItems.isEmpty() -> BasicLoadingScreen()

                state.errorMessage != null && state.cartItems.isEmpty() -> {
                    ErrorScreen(state.errorMessage!!) { viewModel.loadInitialData(productId) }
                }

                state.cartItems.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Your cart is empty.", style = typography.titleMedium)
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.cartItems.entries.toList()) { (product, quantity) ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(product.name, fontWeight = FontWeight.Bold)
                                            Text(
                                                "${product.price.toCurrencyDisplay()} each",
                                                color = colorScheme.secondary
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = {
                                                viewModel.updateQuantity(
                                                    product,
                                                    -1
                                                )
                                            }) {
                                                Icon(
                                                    Icons.Default.Remove,
                                                    contentDescription = "Decrease"
                                                )
                                            }
                                            Text(
                                                "$quantity",
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )
                                            IconButton(onClick = {
                                                viewModel.updateQuantity(
                                                    product,
                                                    1
                                                )
                                            }) {
                                                Icon(
                                                    Icons.Default.Add,
                                                    contentDescription = "Increase"
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                OutlinedTextField(
                                    value = state.notes,
                                    onValueChange = viewModel::onNotesChanged,
                                    label = { Text("Order Notes") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                var expanded by remember { mutableStateOf(false) }

                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded }
                                ) {
                                    OutlinedTextField(
                                        value = state.selectedPaymentMethod.toDisplayName(),
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Payment Method") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                        },
                                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth()
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        PaymentMethod.entries.forEach { method ->
                                            DropdownMenuItem(
                                                text = { Text(method.toDisplayName()) },
                                                onClick = {
                                                    viewModel.onPaymentMethodSelected(method)
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total:", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    text = state.cartTotal.toCurrencyDisplay(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = viewModel::placeOrder,
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !state.isLoading
                            ) {
                                Text(if (state.isLoading) "Processing..." else "Place Order")
                            }
                            if (state.errorMessage != null) {
                                Text(
                                    text = state.errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}