package com.example.aufgabe3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.aufgabe3.model.BookingEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * SharedViewModel:
 * A ViewModel shared across multiple screens to manage booking entries.
 *
 * Responsibilities:
 * - Maintain a list of booking entries.
 * - Provide functions to add and delete entries.
 * - Expose a read-only flow of booking entries to observe changes.
 */
class SharedViewModel : ViewModel() {

    // Mutable state flow to hold the list of booking entries
    private val _bookingsEntries = MutableStateFlow<List<BookingEntry>>(emptyList())

    // Read-only state flow exposed to the UI for observing booking entries
    val bookingsEntries: StateFlow<List<BookingEntry>> = _bookingsEntries

    /**
     * Adds a new booking entry to the list.
     *
     * @param entry The BookingEntry to be added.
     */
    fun addBookingEntry(entry: BookingEntry) {
        _bookingsEntries.value = _bookingsEntries.value + entry
    }

    /**
     * Deletes a booking entry from the list.
     *
     * @param entry The BookingEntry to be removed.
     */
    fun deleteBookingEntry(entry: BookingEntry) {
        _bookingsEntries.value = _bookingsEntries.value - entry
    }
}
