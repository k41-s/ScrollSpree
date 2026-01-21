package com.k41s.scrollspree.ui.screens.admin.product.main

import com.k41s.scrollspree.domain.model.Product

data class AdminProductActions(
    val onSearchChange: (String) -> Unit,
    val onSearchSubmit: () -> Unit,
    val onCategoryFilter: (Int?) -> Unit,
    val onLoadMore: () -> Unit,
    val onEdit: (Product) -> Unit,
    val onDelete: (Int) -> Unit,
    val onAddProduct: () -> Unit
)
