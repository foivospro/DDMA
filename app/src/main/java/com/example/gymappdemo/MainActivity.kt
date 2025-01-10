package com.example.gymappdemo

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
import com.example.gymappdemo.data.database.AppDatabase
import com.example.gymappdemo.ui.theme.GymAppDemoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database and DAO
        val database = AppDatabase.getInstance(this)
        val userDao = database.userDao()

        // State to hold the authentication status
        var isAuthenticated by mutableStateOf<Boolean?>(null)

        // Check authentication in a coroutine
        lifecycleScope.launch {
            isAuthenticated = checkUserAuthentication()
        }

        setContent {
            GymAppDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isAuthenticated != null) {
                        AppNavHost(isAuthenticated = isAuthenticated!!)
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
