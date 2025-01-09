package com.example.gymappdemo.Navigation


import HomeScreenViewModel
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
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import com.example.gymappdemo.ui.screens.CurrentStatus
import com.example.gymappdemo.ui.screens.EditProfileScreen
import com.example.gymappdemo.ui.screens.ExercisesList
import com.example.gymappdemo.ui.screens.HomeScreen
import com.example.gymappdemo.ui.screens.LoginScreen
import com.example.gymappdemo.ui.screens.NavigationItem
import com.example.gymappdemo.ui.screens.QuickStartRoutinesUI
import com.example.gymappdemo.ui.screens.RegisterScreen
import com.example.gymappdemo.ui.screens.UserProfileScreen
import com.example.gymappdemo.ui.viewmodel.AppViewModelFactory
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.ui.viewmodels.ExercisePickerViewModel
import com.example.gymappdemo.ui.viewmodels.LoginViewModel
import com.example.gymappdemo.ui.viewmodels.RegisterViewModel

enum class GymAppScreen {
    Home,
    ExercisePicker,
    QuickStartRoutinesUI,
    MyProfile, 
    ProfileSettings,
    CurrentStatus,
    Login,
    Register
}

@Composable
fun AppNavHost(isAuthenticated: Boolean) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val factory = AppViewModelFactory(
        workoutRepository = WorkoutRepository.getInstance(
            AppDatabase.getInstance(context).gymSessionDao(),
            AppDatabase.getInstance(context).sessionExerciseDao(),
            AppDatabase.getInstance(context).exerciseDao(),
            AppDatabase.getInstance(context).setDao()
        ),
        userRepository = UserRepository.getInstance(
            AppDatabase.getInstance(context).userDao(),
            context
        )
    )
    val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = factory)
    val currentStatusViewModel: CurrentStatusViewModel = viewModel(factory = factory)
    val exercisePickerViewModel: ExercisePickerViewModel = viewModel(factory = factory)
    val loginViewModel: LoginViewModel = viewModel(factory = factory)
    val registerViewModel: RegisterViewModel = viewModel(factory = factory)

    // List of routes where the BottomNavigationBar should not be shown
    val hideBottomBarScreens = listOf(
        GymAppScreen.Login.name,
        GymAppScreen.Register.name,
        GymAppScreen.ProfileSettings.name
    )

    // State to track if the current destination should hide the BottomNavigationBar
    var shouldShowBottomBar by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) GymAppScreen.Home.name else GymAppScreen.Login.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Home Screen
            composable(route = GymAppScreen.Home.name) {
                shouldShowBottomBar = true
                HomeScreen(homeScreenViewModel,navController = navController)
            }
            // Quick Start Routines Screen
            composable(route = GymAppScreen.QuickStartRoutinesUI.name) {
                shouldShowBottomBar = true
                QuickStartRoutinesUI(navController = navController)
            }
            // Exercise Picker Screen
            composable(route = GymAppScreen.ExercisePicker.name) {
                shouldShowBottomBar = true
                ExercisesList(exercisePickerViewModel, navController = navController)
            }
            // Current Status Screen
            composable(route = GymAppScreen.CurrentStatus.name) {
                shouldShowBottomBar = true
                CurrentStatus(currentStatusViewModel, navController = navController)
            }
            // MyProfile Screen
            composable(route = GymAppScreen.MyProfile.name) {
                shouldShowBottomBar = true
                UserProfileScreen(navController = navController)
            }
            // Profile Settings Screen - Hide bottom bar
            composable(route = GymAppScreen.ProfileSettings.name) {
                shouldShowBottomBar = false
                EditProfileScreen(
                    false,
                    {},
                    onBackPressed = {
                        navController.navigateUp() // Go back to MyProfileScreen
                    }
                )
            }
            // Login Screen - Hide bottom bar
            composable(route = GymAppScreen.Login.name) {
                shouldShowBottomBar = false
                LoginScreen(onLoginSuccess = {
                    navController.navigate(GymAppScreen.Home.name) {
                        popUpTo(GymAppScreen.Login.name) { inclusive = true }
                    }
                },
                    navController = navController,
                    loginViewModel)
            }
            // Register Screen - Hide bottom bar
            composable(route = GymAppScreen.Register.name) {
                shouldShowBottomBar = false
                RegisterScreen(
                    registerViewModel = registerViewModel,
                    navController = navController)
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