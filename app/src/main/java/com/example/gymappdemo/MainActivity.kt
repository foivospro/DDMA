package com.example.gymappdemo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymappdemo.Navigation.AppNavHost
import com.example.gymappdemo.data.database.AppDatabase
import com.example.gymappdemo.data.preferences.ThemePreferences
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import com.example.gymappdemo.ui.theme.GymAppDemoTheme
import com.example.gymappdemo.ui.viewmodel.AppViewModelFactory
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userRepository = UserRepository.getInstance(
            AppDatabase.getInstance(this).userDao(),
            this
        )
        val workoutRepository = WorkoutRepository.getInstance(
            AppDatabase.getInstance(this).gymSessionDao(),
            AppDatabase.getInstance(this).sessionExerciseDao(),
            AppDatabase.getInstance(this).exerciseDao(),
            AppDatabase.getInstance(this).setDao()
        )

        val themePreferences = ThemePreferences(applicationContext)

        val appViewModelFactory = AppViewModelFactory(
            workoutRepository,
            userRepository,
            themePreferences
        )

        var isAuthenticatedOutside by mutableStateOf<Boolean?>(null)
        lifecycleScope.launch {
            isAuthenticatedOutside = checkUserAuthentication()
        }

        setContent {

            val myProfileViewModel: MyProfileViewModel = viewModel(
                factory = appViewModelFactory
            )

            val accentColor by myProfileViewModel.selectedAccentColor.collectAsState()
            val isDarkMode by myProfileViewModel.isDarkModeEnabled.collectAsState()

            var isAuthenticated by remember { mutableStateOf(isAuthenticatedOutside) }

            LaunchedEffect(Unit) {
                val auth = checkUserAuthentication()
                isAuthenticated = auth

                val storedTheme = themePreferences.getTheme()
                myProfileViewModel.updateTheme(storedTheme)
            }

            GymAppDemoTheme(
                darkTheme = isDarkMode,
                appThemeType = accentColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isAuthenticated != null) {
                        AppNavHost(
                            isAuthenticated = isAuthenticated!!,
                            isDarkMode = isDarkMode,
                            onDarkModeToggle = { enabled ->
                                myProfileViewModel.toggleDarkMode(enabled)
                            }
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
