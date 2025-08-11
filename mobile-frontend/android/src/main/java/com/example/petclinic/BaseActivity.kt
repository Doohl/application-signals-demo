package com.example.petclinic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.example.petclinic.ui.fragment.BottomNavigationFragment
import com.example.petclinic.ui.theme.PetClinicTheme
import software.amazon.opentelemetry.android.api.AwsRum

/**
 * Base activity that provides common functionality for all activities
 */
abstract class BaseActivity : FragmentActivity() {
    
    /**
     * Override this to provide the content for the activity
     */
    @Composable
    abstract fun ActivityContent()
    
    /**
     * Override this to specify which bottom nav item should be selected
     * Return null if this activity shouldn't show bottom navigation
     */
    abstract fun getSelectedBottomNavRoute(): String?
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            PetClinicTheme {
                val selectedRoute = getSelectedBottomNavRoute()
                
                Scaffold(
                    bottomBar = {
                        selectedRoute?.let { route ->
                            // Use AndroidView to embed the Fragment
                            AndroidView(
                                factory = { context ->
                                    FragmentContainerView(context).apply {
                                        id = android.view.View.generateViewId()
                                        
                                        // Add the BottomNavigationFragment
                                        this@BaseActivity.supportFragmentManager.beginTransaction()
                                            .replace(
                                                this.id,
                                                BottomNavigationFragment.newInstance(route)
                                            )
                                            .commit()
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ActivityContent()
                    }
                }
            }
        }
    }
}
