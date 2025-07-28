package com.example.petclinic

import android.content.Intent
import androidx.compose.runtime.Composable
import com.example.petclinic.ui.screen.OwnersScreen

/**
 * Activity that displays the owners list
 */
class OwnersActivity : BaseActivity() {
    
    @Composable
    override fun ActivityContent() {
        OwnersScreen(
            onOwnerClick = { ownerId ->
                val intent = Intent(this, OwnerDetailActivity::class.java)
                intent.putExtra("ownerId", ownerId)
                startActivity(intent)
            },
            onAddOwnerClick = {
                val intent = Intent(this, AddOwnerActivity::class.java)
                startActivity(intent)
            }
        )
    }
    
    override fun getSelectedBottomNavRoute(): String = "owners"
}
