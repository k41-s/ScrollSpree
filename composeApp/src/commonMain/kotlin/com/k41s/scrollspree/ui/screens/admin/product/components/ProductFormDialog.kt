package com.k41s.scrollspree.ui.screens.admin.product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.Category
import com.k41s.scrollspree.domain.model.Country
import com.k41s.scrollspree.ui.screens.admin.product.models.ProductFormActions
import com.k41s.scrollspree.ui.screens.admin.product.models.ProductFormState
import dev.icerock.moko.media.compose.BindMediaPickerEffect
import dev.icerock.moko.media.compose.rememberMediaPickerControllerFactory
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch

@Composable
fun ProductFormDialog(
    state: ProductFormState,
    actions: ProductFormActions,
    categories: List<Category>,
    countries: List<Country>
) {
    val focusManager = LocalFocusManager.current

    val mediaFactory = rememberMediaPickerControllerFactory()
    val picker = remember(mediaFactory) { mediaFactory.createMediaPickerController() }

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissions = remember(permissionFactory) { permissionFactory.createPermissionsController() }

    val scope = rememberCoroutineScope()

    BindMediaPickerEffect(picker)
    BindEffect(permissions)

    AlertDialog(
        onDismissRequest = actions.onDismiss,
        title = { Text(if (state.name.isEmpty()) "New Product" else "Edit Product") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.errorMessage != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                ProductImagePicker(
                    selectedBytes = state.imageBytes,
                    onPickImage = {
                        scope.launch {
                            try {
                                actions.onError("")
                                permissions.providePermission(dev.icerock.moko.permissions.Permission.GALLERY)
                                val result = picker.pickImage(dev.icerock.moko.media.picker.MediaSource.GALLERY)
                                actions.onImageSelected(result.toByteArray())
                            } catch (e: Exception) {
                                handlePickerError(e, actions.onError)
                            }
                        }
                    },
                    onClearImage = { actions.onImageSelected(null) }
                )

                OutlinedTextField(
                    value = state.name,
                    onValueChange = actions.onNameChange,
                    label = { Text("Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                OutlinedTextField(
                    value = state.price,
                    onValueChange = actions.onPriceChange,
                    label = { Text("Price") },
                    prefix = { Text("$ ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                OutlinedTextField(
                    value = state.description,
                    onValueChange = actions.onDescriptionChange,
                    label = { Text("Description") },
                    minLines = 3,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                CategoryDropdown(
                    categories,
                    state.categoryId,
                    actions.onCategorySelect
                )

                MultiCountrySelect(
                    countries,
                    state.availableCountryIds,
                    actions.onToggleCountry
                )
            }
        },
        confirmButton = {
            Button(
                onClick = actions.onSave,
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Saving...")
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = { TextButton(onClick = actions.onDismiss) { Text("Cancel") } }
    )
}

private fun handlePickerError(e: Exception, onError: (String) -> Unit) {
    when (e) {
        is dev.icerock.moko.permissions.DeniedException -> {
            onError("Gallery permission was denied.")
        }
        is dev.icerock.moko.permissions.DeniedAlwaysException -> {
            onError("Permission permanently denied. Please enable it in settings.")
        }
        is kotlinx.coroutines.CancellationException -> {
            // Do nothing, user just closed the picker
        }
        else -> {
            onError("An unexpected error occurred: ${e.message}")
        }
    }
}