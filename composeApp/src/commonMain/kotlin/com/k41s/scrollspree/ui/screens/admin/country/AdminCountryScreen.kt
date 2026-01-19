package com.k41s.scrollspree.ui.screens.admin.country

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.Country
import com.k41s.scrollspree.ui.components.SimpleEntityCard

@Composable
fun AdminCountryScreen(
    countries: List<Country>,
    onAddCountry: (String) -> Unit,
    onUpdateCountry: (Int, String) -> Unit,
    onDeleteCountry: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var nameInput by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var countryToDelete by remember { mutableStateOf<Country?>(null) }

    fun openEditDialog(country: Country? = null) {
        selectedCountry = country
        nameInput = country?.name ?: ""
        showDialog = true
    }

    fun openDeleteDialog(country: Country) {
        countryToDelete = country
        showDeleteDialog = true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openEditDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Country")
            }
        }
    ) { padding ->
        if (countries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No categories found.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(countries) { country ->
                    SimpleEntityCard(
                        name = country.name,
                        onEdit = { openEditDialog(country) },
                        onDelete = { openDeleteDialog(country) }
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(if (selectedCountry == null) "New Country" else "Edit Country") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Country Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nameInput.isNotBlank()) {
                                if (selectedCountry == null) {
                                    onAddCountry(nameInput)
                                } else {
                                    onUpdateCountry(selectedCountry!!.id, nameInput)
                                }
                                showDialog = false
                            }
                        }
                    ) {
                        Text(if (selectedCountry == null) "Create" else "Update")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showDeleteDialog && countryToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Country") },
                text = { Text("Are you sure you want to delete '${countryToDelete?.name}'?") },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        onClick = {
                            countryToDelete?.let { onDeleteCountry(it.id) }
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}