package com.example.gymappdemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.gymappdemo.Navigation.AppNavHost
import com.example.gymappdemo.ui.theme.GymAppDemoTheme

class MainActivity : ComponentActivity() {
    private var isDarkMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check system theme preference and apply
        isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        setContent {
            GymAppDemoTheme(darkTheme = isDarkMode) { // Pass darkTheme state here
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(
                        isDarkMode = isDarkMode,
                        onDarkModeToggle = { isDarkMode = it }
                    )
                }
            }
        }
    }
}

