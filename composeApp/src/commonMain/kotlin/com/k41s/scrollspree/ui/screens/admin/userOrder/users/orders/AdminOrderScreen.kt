package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.components.DetailRow
import com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.components.InfoSection
import com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.components.ProductThumbnail
import com.k41s.scrollspree.util.formatToString

@Composable
fun AdminOrderScreen(
    order: Order,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Order Details #${order.id}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        InfoSection(title = "Customer Information") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = order.userName, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "User ID: ${order.userId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val dateText = order.orderedAt?.formatToString() ?: "N/A"
            DetailRow(label = "Placed on", value = dateText)
            DetailRow(label = "Payment", value = order.paymentMethod.name)
        }

        InfoSection(title = "Product") {
            Row(modifier = Modifier.fillMaxWidth()) {
                ProductThumbnail(
                    mainImgId = order.product.mainImageId,
                    contentDescription = order.product.name
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.product.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    if (order.product.isDeleted) {
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

        if (order.notes.isNotBlank()) {
            InfoSection(title = "Notes from Customer") {
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
}