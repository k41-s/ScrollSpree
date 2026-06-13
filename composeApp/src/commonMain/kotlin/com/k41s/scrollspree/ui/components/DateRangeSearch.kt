package com.k41s.scrollspree.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun DateRangeSearch(
    onSearch: (startDate: String, endDate: String) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val startState = rememberDatePickerState()
    val endState = rememberDatePickerState()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = startDate,
                onValueChange = { },
                label = { Text("Start Date") },
                placeholder = { Text("YYYY-MM-DD", style = MaterialTheme.typography.bodyMedium) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select Start Date") }
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showStartPicker = true }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = endDate,
                onValueChange = { },
                label = { Text("End Date") },
                placeholder = { Text("YYYY-MM-DD", style = MaterialTheme.typography.bodyMedium) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select End Date") }
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showEndPicker = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSearch(startDate, endDate) },
            modifier = Modifier.fillMaxWidth(),
            enabled = startDate.isNotBlank() && endDate.isNotBlank()
        ) {
            Text("Search History")
        }
    }


    if (showStartPicker) {
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showStartPicker = false
                    startState.selectedDateMillis?.let { millis ->
                        startDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC).date.toString()
                    }
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = startState)
        }
    }

    if (showEndPicker) {
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showEndPicker = false
                    endState.selectedDateMillis?.let { millis ->
                        endDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC).date.toString()
                    }
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = endState)
        }
    }
}