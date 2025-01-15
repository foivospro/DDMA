package com.pmdk.gymapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.pmdk.gymapp.navigation.AppNavHost
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // State to hold the authentication status
        var isAuthenticated by mutableStateOf<Boolean?>(null)
        // Check authentication in a coroutine
        lifecycleScope.launch {
            isAuthenticated = checkUserAuthentication()
        }

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.material3.MaterialTheme.colorScheme.background
            ) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    if (isAuthenticated != null) {
                        AppNavHost(
                            isAuthenticated = isAuthenticated!!
                        )
                    }
                }
            }
        }
    }

    private fun checkUserAuthentication(): Boolean {
        val sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE)
        val email = sharedPreferences.getString("logged_in_user_email", null)
        return email != null
    }
}
