package com.k41s.scrollspree.ui.screens.admin.product.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.ui.screens.admin.product.components.ProductFormDialog
import dev.icerock.moko.media.compose.BindMediaPickerEffect
import dev.icerock.moko.media.compose.rememberMediaPickerControllerFactory
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductContainer() {
    val viewModel: AdminProductViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val mediaFactory = rememberMediaPickerControllerFactory()
    val picker = remember(mediaFactory) { mediaFactory.createMediaPickerController() }

    //val permissionFactory = rememberPermissionsControllerFactory()
    //val permissions = remember(permissionFactory) { permissionFactory.createPermissionsController() }

    BindMediaPickerEffect(picker)
    //BindEffect(permissions)

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            val isUndoable = event.endsWith("|UNDO")
            val displayMessage = event.removeSuffix("|UNDO")

            val snackbarResult = snackbarHostState.showSnackbar(
                message = displayMessage,
                actionLabel = if (isUndoable) "Undo" else null,
                duration = SnackbarDuration.Short
            )

            if (snackbarResult == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshCountries()
        viewModel.refreshCategories()
    }

    val actions = remember(viewModel) {
        AdminProductActions(
            onSearchChange = viewModel::onSearchChange,
            onSearchSubmit = { viewModel.loadProducts(true) },
            onCategoryFilter = viewModel::onCategoryFilter,
            onLoadMore = { viewModel.loadProducts(false) },
            onEdit = { product -> viewModel.openEditDialog(product) },
            onDelete = { productId -> viewModel.deleteProduct(productId) },
            onAddProduct = { viewModel.openAddDialog() }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = actions.onAddProduct,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (viewModel.isAddEditDialogVisible) {
                ProductFormDialog(
                    state = viewModel.formState,
                    actions = viewModel.getFormActions(),
                    categories = viewModel.getCategories(),
                    countries = viewModel.getCountries(),
                    picker = picker,
                    scope = scope
                )
            }

            when (val uiState = state) {
                is AdminProductUiState.Loading -> {
                    BasicLoadingScreen()
                }

                is AdminProductUiState.Error -> {
                    ErrorScreen(uiState.message) {
                        viewModel.loadProducts(true)
                    }
                }

                is AdminProductUiState.Success -> {
                    AdminProductScreen(
                        uiState,
                        viewModel.searchQuery,
                        viewModel.selectedCategoryId,
                        actions
                    )
                }
            }
        }
    }
}