package com.k41s.scrollspree.ui.screens.admin.country

import com.k41s.scrollspree.domain.model.Country

sealed class AdminCountryUiState {
    data object Loading : AdminCountryUiState()
    data class Success(val countries: List<Country>) : AdminCountryUiState()
    data class Error(val message: String) : AdminCountryUiState()
}