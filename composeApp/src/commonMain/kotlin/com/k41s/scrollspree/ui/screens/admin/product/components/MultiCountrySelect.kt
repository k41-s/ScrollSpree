package com.k41s.scrollspree.ui.screens.admin.product.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiCountrySelect(
    allCountries: List<Country>,
    selectedCountryIds: List<Int>,
    onToggleCountry: (Int) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Available in Countries", style = MaterialTheme.typography.labelLarge)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            allCountries.filter { it.id in selectedCountryIds }.forEach { country ->
                InputChip(
                    selected = true,
                    onClick = { onToggleCountry(country.id) },
                    label = { Text(country.name) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            AssistChip(
                onClick = { showDialog = true },
                label = { Text("Add Country") },
                leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) }
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Countries") },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    allCountries.forEach { country ->
                        val isSelected = country.id in selectedCountryIds
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onToggleCountry(country.id)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { onToggleCountry(country.id) }
                            )
                            Text(
                                country.name,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Done")
                }
            }
        )
    }
}