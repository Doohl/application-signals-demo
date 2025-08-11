package com.example.petclinic

import android.content.Intent
import androidx.compose.runtime.Composable
import com.example.petclinic.ui.fragment.AddOwnerFragment
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
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, AddOwnerFragment())
                    .addToBackStack(null)
                    .commit()
            }
        )
    }
    
    override fun getSelectedBottomNavRoute(): String = "owners"
}
