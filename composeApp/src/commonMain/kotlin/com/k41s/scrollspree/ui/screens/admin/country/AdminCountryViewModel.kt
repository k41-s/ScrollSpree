package com.k41s.scrollspree.ui.screens.admin.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.CountryRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminCountryViewModel(
    private val repository: CountryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminCountryUiState>(AdminCountryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            _uiState.value = AdminCountryUiState.Loading

            val result = repository.getAll()

            _uiState.value = when (result) {
                is NetworkResult.Success -> {
                    AdminCountryUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    AdminCountryUiState.Error(result.message)
                }
                is NetworkResult.Loading -> AdminCountryUiState.Loading
            }
        }
    }

    fun createCountry(name: String) {
        viewModelScope.launch {
            when (val result = repository.create(name)) {
                is NetworkResult.Success -> {
                    loadCountries()
                }
                is NetworkResult.Error -> {
                    println("Create failed: ${result.message}")
                }
                else -> {}
            }
        }
    }

    fun updateCountry(id: Int, name: String) {
        viewModelScope.launch {
            when (val result = repository.update(id, name)) {
                is NetworkResult.Success -> {
                    loadCountries()
                }
                is NetworkResult.Error -> {
                    println("Update failed: ${result.message}")
                }
                else -> {}
            }
        }
    }

    fun deleteCountry(id: Int) {
        viewModelScope.launch {
            when (val result = repository.delete(id)) {
                is NetworkResult.Success -> {
                    loadCountries()
                }
                is NetworkResult.Error -> {
                    println("Delete failed: ${result.message}")
                }
                else -> {}
            }
        }
    }
}