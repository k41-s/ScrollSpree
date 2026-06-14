package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.ui.components.orders.DetailRow
import com.k41s.scrollspree.ui.components.orders.InfoSection
import com.k41s.scrollspree.ui.components.orders.ProductThumbnail
import com.k41s.scrollspree.util.formatToString
import com.k41s.scrollspree.util.toCurrencyDisplay

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
                style = typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        InfoSection(title = "Customer Information") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = order.userName, style = typography.bodyLarge)
                    Text(
                        text = "User ID: ${order.userId}",
                        style = typography.labelSmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }

            val dateText = order.orderedAt?.formatToString() ?: "N/A"
            DetailRow(label = "Placed on", value = dateText)
            DetailRow(label = "Payment", value = order.paymentMethod.name)
        }

        InfoSection(title = "Products") {
            order.items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Quantity: ${item.quantity}",
                            style = typography.bodyMedium,
                            color = colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = item.price.toCurrencyDisplay(),
                            style = typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.primary
                        )

                        if (order.items.firstOrNull()?.isDeleted!!) {
                            Badge(
                                containerColor = colorScheme.errorContainer,
                                contentColor = colorScheme.onErrorContainer,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text("Product Deleted/Discontinued")
                            }
                        }
                    }
                }
                if (index < order.items.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)
                }
            }
        }

        if (order.notes.isNotBlank()) {
            InfoSection(title = "Notes from Customer") {
                Surface(
                    color = colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = order.notes,
                        modifier = Modifier.padding(16.dp),
                        style = typography.bodyMedium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}