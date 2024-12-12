package com.example.aufgabe3.model

import java.time.LocalDate

/**
 * BookingEntry:
 * A data model representing a single booking entry.
 *
 * Fields:
 * - arrivalDate: The date of arrival (type: LocalDate).
 * - departureDate: The date of departure (type: LocalDate).
 * - name: The name associated with the booking (type: String).
 */
data class BookingEntry(
    val arrivalDate: LocalDate,   // Date of arrival
    val departureDate: LocalDate, // Date of departure
    val name: String              // Name of the person or group making the booking
)
