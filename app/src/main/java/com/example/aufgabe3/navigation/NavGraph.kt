package com.example.aufgabe3.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aufgabe3.ui.add.AddScreen
import com.example.aufgabe3.ui.home.HomeScreen
import com.example.aufgabe3.viewmodel.SharedViewModel

/**
 * AppNavHost:
 * The main navigation container of the app, defining routes and handling navigation between screens.
 *
 * Available routes:
 * - "home": Displays the list of booking entries.
 * - "add": Provides a screen to add a new booking entry.
 *
 * @param navController The NavHostController to manage navigation between routes.
 */
@Composable
fun AppNavHost(navController: NavHostController) {
    // Shared ViewModel to manage booking entries across screens
    val sharedViewModel: SharedViewModel = viewModel()

    /**
     * NavHost:
     * - Sets the start destination to "home".
     * - Defines all the available routes and their corresponding Composable screens.
     */
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        /**
         * HomeScreen:
         * - Route: "home"
         * - Displays the list of booking entries.
         * - Allows navigation to the AddScreen for adding new entries.
         */
        composable("home") {
            HomeScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        /**
         * AddScreen:
         * - Route: "add"
         * - Provides a form for the user to create a new booking entry.
         * - Allows returning to the HomeScreen after adding a new entry.
         */
        composable("add") {
            AddScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }
}
