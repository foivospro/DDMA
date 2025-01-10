package com.example.gymappdemo

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.gymappdemo.Navigation.AppNavHost
import com.example.gymappdemo.ui.theme.GymAppDemoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var isDarkMode by mutableStateOf(false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check system theme preference and apply
        isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        // State to hold the authentication status
        var isAuthenticated by mutableStateOf<Boolean?>(null)
        // Check authentication in a coroutine
        lifecycleScope.launch {
            isAuthenticated = checkUserAuthentication()
        }

        setContent {
            GymAppDemoTheme(darkTheme = isDarkMode) { // Pass darkTheme state here
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isAuthenticated != null) {
                        AppNavHost(
                            isDarkMode = isDarkMode,
                            onDarkModeToggle = { isDarkMode = it },
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
        return if (email != null) {
            true
        } else {
            false
        }
    }
}

