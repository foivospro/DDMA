package com.example.gymappdemo.Navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymappdemo.data.database.AppDatabase
import com.example.gymappdemo.data.repositories.WorkoutRepository
import com.example.gymappdemo.ui.screens.CurrentStatus
import com.example.gymappdemo.ui.screens.EditProfileScreen
import com.example.gymappdemo.ui.screens.ExercisesList
import com.example.gymappdemo.ui.screens.HomeScreen
import com.example.gymappdemo.ui.screens.NavigationItem
import com.example.gymappdemo.ui.screens.QuickStartRoutinesUI
import com.example.gymappdemo.ui.screens.UserProfileScreen
import com.example.gymappdemo.ui.viewmodel.AppViewModelFactory
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.ui.viewmodels.ExercisePickerViewModel

enum class GymAppScreen {
    Home,
    ExercisePicker,
    QuickStartRoutinesUI,
    MyProfile, 
    ProfileSettings,
    CurrentStatus
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Create a shared ViewModel instance for the CurrentStatusViewModel
    val factory = AppViewModelFactory(
        workoutRepository = WorkoutRepository.getInstance(
            AppDatabase.getInstance(context).gymSessionDao(),
            AppDatabase.getInstance(context).sessionExerciseDao(),
            AppDatabase.getInstance(context).exerciseDao(),
            AppDatabase.getInstance(context).setDao()
        )
    )
    val currentStatusViewModel: CurrentStatusViewModel = viewModel(factory = factory)
    val exercisePickerViewModel: ExercisePickerViewModel = viewModel(factory = factory)
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = GymAppScreen.Home.name,
            modifier = Modifier.padding(paddingValues)
        ) {

            // Home Screen in NavHost
            composable(route = GymAppScreen.Home.name) {
                HomeScreen(navController = navController)
            }
            // Quick Start Routines Screen in NavHost
            composable(route = GymAppScreen.QuickStartRoutinesUI.name) {
                QuickStartRoutinesUI(navController = navController)
            }
            // Exercise Picker Screen in NavHost
            composable(route = GymAppScreen.ExercisePicker.name) {
                ExercisesList(exercisePickerViewModel, navController = navController)
            }
            composable(route = GymAppScreen.CurrentStatus.name) {
                CurrentStatus(currentStatusViewModel, navController = navController)
            }
            // MyProfile Screen
            composable(route = GymAppScreen.MyProfile.name) {
                UserProfileScreen(navController = navController)
            }

            // Profile Settings Screen - No bottom bar shown here
            composable(route = GymAppScreen.ProfileSettings.name) {
                EditProfileScreen(
                    false,
                    {},
                    onBackPressed = {
                        navController.navigateUp() // Go back to MyProfileScreen
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    // Menu elements
    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home),
        NavigationItem("Favorites", Icons.Filled.Favorite),
        NavigationItem("Profile", Icons.Filled.Person)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when (item.label) {
                        "Home" -> navController.navigate(GymAppScreen.Home.name)
                        "Favorites" -> navController.navigate(GymAppScreen.QuickStartRoutinesUI.name)
                        "Profile" -> navController.navigate(GymAppScreen.MyProfile.name) // Navigate to MyProfile
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            )
        }
    }
}