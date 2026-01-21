package com.k41s.scrollspree.ui.screens.admin.product.models

import coil3.Uri

data class ProductFormState(
    val id: Int? = null,
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val categoryId: Int? = null,
    val imageBytes: ByteArray? = null,
    val errorMessage: String? = null,
    val availableCountryIds: List<Int> = emptyList(),
    val isSaving: Boolean = false
)

data class ProductFormActions(
    val onNameChange: (String) -> Unit,
    val onPriceChange: (String) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onCategorySelect: (Int) -> Unit,
    val onImageSelected: (ByteArray?) -> Unit,
    val onToggleCountry: (Int) -> Unit,
    val onSave: () -> Unit,
    val onDismiss: () -> Unit
)