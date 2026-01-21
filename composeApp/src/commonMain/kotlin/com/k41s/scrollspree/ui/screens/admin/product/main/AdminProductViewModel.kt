package com.k41s.scrollspree.ui.screens.admin.product.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.k41s.scrollspree.data.repository.CategoryRepository
import com.k41s.scrollspree.data.repository.ProductRepository
import com.k41s.scrollspree.domain.model.Category
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.remote.dto.ProductDTO
import com.k41s.scrollspree.data.repository.CountryRepository
import com.k41s.scrollspree.data.repository.ProductImageRepository
import com.k41s.scrollspree.domain.model.Country
import com.k41s.scrollspree.ui.screens.admin.product.models.ProductFormActions
import com.k41s.scrollspree.ui.screens.admin.product.models.ProductFormState
import com.k41s.scrollspree.util.toNumericString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AdminProductViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val imageRepository: ProductImageRepository,
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminProductUiState>(AdminProductUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    var searchQuery by mutableStateOf("")
    var selectedCategoryId by mutableStateOf<Int?>(null)
    private var currentPage = 0
    private var currentProducts = mutableListOf<Product>()

    private var allCategories = listOf<Category>()
    private var allCountries = listOf<Country>()

    var isAddEditDialogVisible by mutableStateOf(false)
    var editingProductId by mutableStateOf<Int?>(null)
    var formState by mutableStateOf(ProductFormState())

    private var lastDeletedProduct: Product? = null

    init {
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            val categoryResult = categoryRepository.getAll()
            val countryResult = countryRepository.getAll()

            if (categoryResult is NetworkResult.Success) {
                allCategories = categoryResult.data
            }
            if (countryResult is NetworkResult.Success) {
                allCountries = countryResult.data
            }

            loadProducts(true)
        }
    }

    fun loadProducts(reset: Boolean = false) {
        if (reset) {
            currentPage = 0
            currentProducts.clear()
        }

        viewModelScope.launch {
            if (reset) _uiState.value = AdminProductUiState.Loading

            val result = productRepository.getAll(
                search = searchQuery.takeIf { it.isNotBlank() },
                categoryId = selectedCategoryId,
                page = currentPage
            )

            when (result) {
                is NetworkResult.Success -> {
                    currentProducts.addAll(result.data.products)
                    _uiState.value = AdminProductUiState.Success(
                        products = currentProducts.toList(),
                        categories = allCategories,
                        isLastPage = result.data.isLastPage
                    )
                    if (!result.data.isLastPage) currentPage++
                }
                is NetworkResult.Error -> {
                    _uiState.value = AdminProductUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun onSearchChange(query: String) {
        searchQuery = query
    }

    fun onCategoryFilter(id: Int?) {
        selectedCategoryId = id
        loadProducts(true)
    }

    fun openAddDialog() {
        formState = ProductFormState()
        editingProductId = null
        isAddEditDialogVisible = true
    }

    fun openEditDialog(product: Product) {
        editingProductId = product.id
        formState = ProductFormState(
            name = product.name,
            price = product.price.toNumericString(),
            description = product.description,
            categoryId = product.category.id,
            imageBytes = null,
            availableCountryIds = product.countries.map { it.id }
        )
        isAddEditDialogVisible = true
    }

    fun toggleCountry(countryId: Int) {
        val currentIds = formState.availableCountryIds
        val newIds = if (countryId in currentIds) {
            currentIds - countryId
        } else {
            currentIds + countryId
        }
        formState = formState.copy(availableCountryIds = newIds)
    }

    fun saveProduct() {
        if (formState.isSaving) return

        val priceValue = formState.price.toDoubleOrNull()
        if (formState.name.isBlank() || priceValue == null) {
            formState = formState.copy(errorMessage = "Please enter a valid name and price")
            return
        }

        val dto = ProductDTO(
            name = formState.name,
            description = formState.description,
            price = formState.price.toDoubleOrNull() ?: 0.0,
            categoryId = formState.categoryId,
            categoryName = "",
            imageIds = emptyList(),
            countryIds = formState.availableCountryIds,
            countryNames = emptyList()
        )

        viewModelScope.launch {
            formState = formState.copy(isSaving = true, errorMessage = null)

            var savedProductId: Int? = null

            val result = if (editingProductId == null) {
                productRepository.create(dto)
            } else {
                productRepository.update(editingProductId!!, dto).let { res ->
                    if (res is NetworkResult.Success)
                        NetworkResult.Success(editingProductId!!)
                    else
                        res
                }
            }

            when (result) {
                is NetworkResult.Success -> {
                    savedProductId =
                        if (result.data is Product) result.data.id else result.data as Int

                    formState.imageBytes?.let { bytes ->
                        val imageResult = imageRepository.uploadImage(
                            productId = savedProductId,
                            fileBytes = bytes,
                            fileName = "${formState.name.replace(" ", "_")}.jpg"
                        )

                        if (imageResult is NetworkResult.Error) {
                            formState = formState
                                .copy(
                                    errorMessage = "Product saved, but image upload failed: ${imageResult.message}. Please try again via edit.",
                                    isSaving = false
                                )
                            return@launch
                        }
                    }

                    if (formState.errorMessage == null) {
                        _events.send("Product saved successfully!")
                        isAddEditDialogVisible = false
                        loadProducts(true)

                    }
                }
                is NetworkResult.Error -> {
                    formState = formState.copy(errorMessage = result.message)
                }
                else -> {}
            }
            formState = formState.copy(isSaving = false)
        }
    }

    fun getFormActions() = ProductFormActions(
        onNameChange = { formState = formState.copy(name = it) },
        onPriceChange = { formState = formState.copy(price = it) },
        onDescriptionChange = { formState = formState.copy(description = it) },
        onCategorySelect = { formState = formState.copy(categoryId = it) },
        onImageSelected = { formState = formState.copy(imageBytes = it) },
        onSave = { saveProduct() },
        onToggleCountry = { toggleCountry(it) },
        onDismiss = { isAddEditDialogVisible = false }
    )

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            val product = currentProducts.find { it.id == id }

            val result = productRepository.delete(id)
            if (result is NetworkResult.Success) {
                lastDeletedProduct = product
                loadProducts(true)
                _events.send("Product deleted|UNDO")
            }
        }
    }

    fun undoDelete() {
        val product = lastDeletedProduct ?: return
        viewModelScope.launch {
            val result = productRepository.restore(product)
            if (result is NetworkResult.Success) {
                lastDeletedProduct = null
                _events.send("Restored ${product.name}")
                loadProducts(true)
            }
        }
    }

    fun getCategories() = allCategories
    fun getCountries() = allCountries
}