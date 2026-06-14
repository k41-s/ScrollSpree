package com.k41s.scrollspree.ui.screens.user.productDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.data.repository.ProductRepository
import com.k41s.scrollspree.domain.manager.CartManager
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository,
    authRepository: AuthRepository,
    private val cartManager: CartManager,
    private val productId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProductDetailEvent>()
    val events = _events.asSharedFlow()

    private val isLoggedIn = authRepository.getCurrentUser()
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        loadProduct()

        viewModelScope.launch {
            cartManager.errorEvent.collect { errorMessage ->
                _events.emit(ProductDetailEvent.ShowSnackbar(errorMessage))
            }
        }
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            val result = repository.getById(productId)
            _uiState.value = when (result) {
                is NetworkResult.Success -> {
                    ProductDetailUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    ProductDetailUiState.Error(result.message)
                }
                is NetworkResult.Loading -> {
                    ProductDetailUiState.Loading
                }
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            if (!isLoggedIn.value) {
                _events.emit(ProductDetailEvent.ShowLoginRequired)
                return@launch
            }

            cartManager.addToCart(product)
            _events.emit(ProductDetailEvent.ItemAddedToCart("${product.name} added to cart!"))
        }
    }

    fun onBuyClicked(productId: Int) {
        viewModelScope.launch {
            if (!isLoggedIn.value) {
                _events.emit(ProductDetailEvent.ShowLoginRequired)
                return@launch
            }
            _events.emit(ProductDetailEvent.NavigateToCheckout(productId))
        }
    }
}