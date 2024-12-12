package com.example.aufgabe3.ui.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.model.BookingEntry
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val bookingsEntries by sharedViewModel.bookingsEntries.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var bookingToDelete by remember { mutableStateOf<BookingEntry?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gästebuch") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add booking")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (bookingsEntries.isEmpty()) {
                Text("Keine Buchungen vorhanden.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(bookingsEntries) { booking ->
                        BookingEntryItem(
                            booking = booking,
                            onDeleteClick = {
                                bookingToDelete = booking
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Bestätigungsdialog für das Löschen
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Buchung löschen") },
            text = {
                Text("Möchtest du die Buchung \"${bookingToDelete?.name}\" wirklich löschen?")
            },
            confirmButton = {
                TextButton(onClick = {
                    bookingToDelete?.let { sharedViewModel.deleteBookingEntry(it) }
                    showDeleteDialog = false
                }) {
                    Text("Löschen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

@Composable
fun BookingEntryItem(
    booking: BookingEntry,
    onDeleteClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = booking.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${booking.arrivalDate.format(dateFormatter)} - ${booking.departureDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete booking")
            }
        }
    }
}
