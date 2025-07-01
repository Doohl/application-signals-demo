package com.example.petclinic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.petclinic.navigation.Screen
import com.example.petclinic.navigation.bottomNavItems
import com.example.petclinic.ui.theme.PetClinicTheme

class BottomNavigationFragment : Fragment() {
    
    private var navigationListener: NavigationListener? = null
    private var _currentRoute = mutableStateOf(Screen.Home.route)
    private var composeView: ComposeView? = null
    
    interface NavigationListener {
        fun onNavigationItemSelected(route: String)
    }
    
    fun setNavigationListener(listener: NavigationListener) {
        navigationListener = listener
    }
    
    fun updateCurrentRoute(route: String) {
        _currentRoute.value = route
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        composeView = ComposeView(requireContext()).apply {
            setContent {
                PetClinicTheme {
                    BottomNavigationContent(
                        currentRoute = _currentRoute.value,
                        onNavigationItemClick = { route ->
                            navigationListener?.onNavigationItemSelected(route)
                        }
                    )
                }
            }
        }
        return composeView!!
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}

@Composable
private fun BottomNavigationContent(
    currentRoute: String,
    onNavigationItemClick: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    onNavigationItemClick(screen.route)
                }
            )
        }
    }
}
