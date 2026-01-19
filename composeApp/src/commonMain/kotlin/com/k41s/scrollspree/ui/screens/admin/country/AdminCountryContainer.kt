package com.k41s.scrollspree.ui.screens.admin.country

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminCountryContainer() {

    val viewModel: AdminCountryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is AdminCountryUiState.Loading -> {
            BasicLoadingScreen()
        }
        is AdminCountryUiState.Error -> {
            ErrorScreen(
                message = state.message,
                onRetry = { viewModel.loadCountries() }
            )
        }
        is AdminCountryUiState.Success -> {
            AdminCountryScreen(
                countries = state.countries,
                onAddCountry = { name -> viewModel.createCountry(name) },
                onUpdateCountry = { id, name -> viewModel.updateCountry(id, name) },
                onDeleteCountry = { id -> viewModel.deleteCountry(id) }
            )
        }
    }
}