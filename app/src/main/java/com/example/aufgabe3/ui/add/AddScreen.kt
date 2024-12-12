package com.example.aufgabe3.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.model.BookingEntry
import com.example.aufgabe3.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Composable function for the AddScreen.
 * Displays a form where users can add a new booking entry.
 * Includes fields for a name and a date range picker.
 *
 * @param navController Navigation controller for navigating between screens.
 * @param sharedViewModel ViewModel shared across screens to manage bookings.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    // State variables for user input

    var name by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf<LocalDate?>(null) }
    var departureDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDateRangePicker by remember { mutableStateOf(false) }

    // Formatter for displaying dates in dd.MM.yyyy format

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    // Snackbar host for displaying validation messages

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Main screen layout

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Booking Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            /** Input field for the booking name */

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            /** Input field for the date range (read-only, opens date picker) */

            OutlinedTextField(
                value = if (arrivalDate != null && departureDate != null) {
                    "${arrivalDate!!.format(dateFormatter)} - ${departureDate!!.format(dateFormatter)}"
                } else {
                    ""
                },
                onValueChange = {},
                label = { Text("Select Date Range") },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateRangePicker = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            /** Save button for adding the booking entry */

            Button(
                onClick = {
                    when {
                        name.isBlank() -> {
                            // Show an error if the name is empty
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Bitte gib deinen Namen ein.")
                            }
                        }
                        arrivalDate == null || departureDate == null -> {
                            // Show an error if the date range is not selected
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Bitte wähle einen Datum aus.")
                            }
                        }
                        else -> {
                            // Create and add a new booking entry
                            val newEntry = BookingEntry(
                                name = name,
                                arrivalDate = arrivalDate!!,
                                departureDate = departureDate!!
                            )
                            sharedViewModel.addBookingEntry(newEntry)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }

    /** Show the date range picker dialog when required */

    if (showDateRangePicker) {
        DateRangePickerModal(
            onDismiss = { showDateRangePicker = false },
            onDateSelected = { startMillis, endMillis ->
                // Convert selected dates from milliseconds to LocalDate
                arrivalDate = Instant.ofEpochMilli(startMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                departureDate = Instant.ofEpochMilli(endMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                showDateRangePicker = false
            }
        )
    }
}

/**
 * Composable function for displaying a DateRangePicker in a modal dialog.
 * Allows the user to select a start and end date.
 *
 * @param onDismiss Callback to dismiss the dialog.
 * @param onDateSelected Callback when a date range is selected.
 * Takes the start and end dates in milliseconds.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDismiss: () -> Unit,
    onDateSelected: (Long, Long) -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Date Range") },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                DateRangePicker(
                    state = dateRangePickerState,
                    showModeToggle = false,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val startMillis = dateRangePickerState.selectedStartDateMillis
                    val endMillis = dateRangePickerState.selectedEndDateMillis
                    if (startMillis != null && endMillis != null) {
                        onDateSelected(startMillis, endMillis)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
