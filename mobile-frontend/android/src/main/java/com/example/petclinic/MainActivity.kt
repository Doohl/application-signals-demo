package com.example.petclinic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petclinic.navigation.Screen
import com.example.petclinic.navigation.bottomNavItems
import com.example.petclinic.ui.fragment.BottomNavigationFragment
import com.example.petclinic.ui.screen.*
import com.example.petclinic.ui.theme.PetClinicTheme

class MainActivity : FragmentActivity() {
    
    private lateinit var bottomNavFragment: BottomNavigationFragment
    private val bottomNavContainerId = 12345 // Stable container ID
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create the bottom navigation fragment only once
        bottomNavFragment = BottomNavigationFragment()
        
        enableEdgeToEdge()
        setContent {
            PetClinicTheme {
                PetClinicApp(
                    bottomNavFragment = bottomNavFragment,
                    fragmentActivity = this@MainActivity,
                    containerId = bottomNavContainerId
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetClinicApp(
    bottomNavFragment: BottomNavigationFragment,
    fragmentActivity: FragmentActivity,
    containerId: Int
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route
    var isFragmentAdded by remember { mutableStateOf(false) }
    
    // Determine if we should show the bottom navigation
    val shouldShowBottomNav = currentRoute in bottomNavItems.map { it.route }
    
    // Set up navigation listener for the fragment
    LaunchedEffect(Unit) {
        bottomNavFragment.setNavigationListener(object : BottomNavigationFragment.NavigationListener {
            override fun onNavigationItemSelected(route: String) {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        })
    }
    
    // Update fragment's current route whenever it changes
    LaunchedEffect(currentRoute) {
        bottomNavFragment.updateCurrentRoute(currentRoute)
    }
    
    Scaffold(
        bottomBar = {
            // Always create the AndroidView, but control visibility
            AndroidView(
                factory = { context ->
                    androidx.fragment.app.FragmentContainerView(context).apply {
                        id = containerId
                    }
                },
                update = { containerView ->
                    // Control visibility based on shouldShowBottomNav
                    containerView.visibility = if (shouldShowBottomNav) {
                        android.view.View.VISIBLE
                    } else {
                        android.view.View.GONE
                    }
                    
                    // Only add the fragment once
                    if (!isFragmentAdded) {
                        fragmentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(containerId, bottomNavFragment)
                            .commitNow()
                        isFragmentAdded = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            
            composable(Screen.Owners.route) {
                OwnersScreen(
                    onOwnerClick = { ownerId ->
                        navController.navigate(Screen.OwnerDetail.createRoute(ownerId))
                    },
                    onAddOwnerClick = {
                        navController.navigate(Screen.AddOwner.route)
                    }
                )
            }
            
            composable(Screen.Vets.route) {
                VetsScreen()
            }
            
            composable(Screen.OwnerDetail.route) { backStackEntry ->
                val ownerId = backStackEntry.arguments?.getString("ownerId")?.toIntOrNull() ?: 0
                OwnerDetailScreen(
                    ownerId = ownerId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.AddOwner.route) {
                AddOwnerScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
