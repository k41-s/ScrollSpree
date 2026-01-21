package com.k41s.scrollspree.ui.screens.admin.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.Uri
import coil3.compose.AsyncImage
import com.k41s.scrollspree.data.local.TokenManager
import org.koin.compose.koinInject

@Composable
fun ProductImagePicker(
    selectedBytes: ByteArray?,
    onPickImage: () -> Unit
) {
    val tokenManager = koinInject<TokenManager>()
    val imageLoader = koinInject<ImageLoader>()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onPickImage() },
        contentAlignment = Alignment.Center
    ) {
        if (selectedBytes != null) {
            AsyncImage(
                model = selectedBytes,
                imageLoader = imageLoader,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                Icons.Default.AddAPhoto,
                contentDescription = "Add Photo",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}