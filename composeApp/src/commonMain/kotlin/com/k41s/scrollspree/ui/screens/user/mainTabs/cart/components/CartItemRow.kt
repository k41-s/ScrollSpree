package com.k41s.scrollspree.ui.screens.user.mainTabs.cart.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.util.toCurrencyDisplay
import org.koin.compose.koinInject

@Composable
fun CartItemRow(
    product: Product,
    quantity: Int,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = product.images.firstOrNull()?.url
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name,
                    imageLoader = koinInject<ImageLoader>(),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                    error = ColorPainter(MaterialTheme.colorScheme.error)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.price.toCurrencyDisplay(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onUpdateQuantity(-1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text(
                        text = quantity.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(
                        onClick = { onUpdateQuantity(1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}