package com.example.petclinic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.petclinic.MainActivity
import com.example.petclinic.OwnersActivity
import com.example.petclinic.VetsActivity
import com.example.petclinic.ui.components.BottomNavigationBar
import com.example.petclinic.ui.theme.PetClinicTheme

/**
 * Fragment that contains the bottom navigation bar
 */
class BottomNavigationFragment : Fragment() {
    
    private var selectedRoute: String = "home"
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get the selected route from arguments
        selectedRoute = arguments?.getString(ARG_SELECTED_ROUTE) ?: "home"
        
        return ComposeView(requireContext()).apply {
            setContent {
                PetClinicTheme {
                    BottomNavigationBar(
                        selectedRoute = selectedRoute,
                        onNavigate = { destination ->
                            navigateToActivity(destination)
                        }
                    )
                }
            }
        }
    }
    
    /**
     * Navigate to the appropriate activity based on the destination route
     */
    private fun navigateToActivity(route: String) {
        val intent = when (route) {
            "home" -> Intent(requireContext(), MainActivity::class.java)
            "owners" -> Intent(requireContext(), OwnersActivity::class.java)
            "vets" -> Intent(requireContext(), VetsActivity::class.java)
            else -> return
        }
        
        // Don't start the same activity we're already in
        if (intent.component?.className != requireActivity()::class.java.name) {
            startActivity(intent)
        }
    }
    
    companion object {
        private const val ARG_SELECTED_ROUTE = "selected_route"
        
        /**
         * Create a new instance of BottomNavigationFragment with the specified selected route
         */
        fun newInstance(selectedRoute: String): BottomNavigationFragment {
            return BottomNavigationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SELECTED_ROUTE, selectedRoute)
                }
            }
        }
    }
}
