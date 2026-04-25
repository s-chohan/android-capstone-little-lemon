package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationComposable(navController: NavHostController, database: AppDatabase) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    // Check if user is already registered
    val isRegistered = sharedPreferences.getBoolean("userRegistered", false)

    NavHost(
        navController = navController,
        startDestination = if (isRegistered) Home.route else Onboarding.route
    ) {
        composable(Onboarding.route) {
            Onboarding(navController)
        }
        composable(Home.route) {
            Home(
                navController,
                database = database
            )}

        composable(Profile.route) {
            Profile(navController)
        }

    }
}