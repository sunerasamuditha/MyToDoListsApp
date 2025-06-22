package com.yourname.mytodoapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Wait until nav_host_fragment is laid out
        findViewById<View>(R.id.nav_host_fragment).viewTreeObserver.addOnGlobalLayoutListener {
            val navController = findNavController(R.id.nav_host_fragment)
            setupActionBarWithNavController(navController)
        }

        // Firebase anonymous login
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnSuccessListener {
                Log.d("FirebaseAuth", "Signed in anonymously.")
            }
            .addOnFailureListener {
                Log.e("FirebaseAuth", "Failed to sign in: ${it.message}")
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}