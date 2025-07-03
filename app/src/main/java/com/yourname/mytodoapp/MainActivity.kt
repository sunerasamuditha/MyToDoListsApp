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

        // Set up navigation safely with a post delay
        findViewById<View>(R.id.nav_host_fragment)?.post {
            try {
                val navController = findNavController(R.id.nav_host_fragment)
                setupActionBarWithNavController(navController)
            } catch (e: Exception) {
                Log.e("MainActivity", "Navigation setup failed: ${e.message}")
            }
        }

        // Firebase anonymous login with error handling
        try {
            FirebaseAuth.getInstance().signInAnonymously()
                .addOnSuccessListener {
                    Log.d("FirebaseAuth", "Signed in anonymously.")
                }
                .addOnFailureListener {
                    Log.e("FirebaseAuth", "Failed to sign in: ${it.message}")
                }
        } catch (e: Exception) {
            Log.e("MainActivity", "Firebase initialization failed: ${e.message}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}