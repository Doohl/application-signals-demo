package com.example.petclinic.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation destinations for the Pet Clinic app
 */
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Owners : Screen("owners", "Owners", Icons.Default.People)
    data object Vets : Screen("vets", "Veterinarians", Icons.Default.Person)
    
    // Detail screens (not in bottom nav)
    data object OwnerDetail : Screen("owner_detail/{ownerId}", "Owner Details", Icons.Default.Person) {
        fun createRoute(ownerId: Int) = "owner_detail/$ownerId"
    }
    data object AddOwner : Screen("add_owner", "Add Owner", Icons.Default.Person)
}

/**
 * Bottom navigation items
 */
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Owners,
    Screen.Vets
)
