package com.example.gymappdemo.navigation


import NewsScreen
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymappdemo.MyApplication
import com.example.gymappdemo.R
import com.example.gymappdemo.data.database.AppDatabase
import com.example.gymappdemo.data.preferences.ThemePreferences
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import com.example.gymappdemo.ui.screens.CurrentStatus
import com.example.gymappdemo.ui.screens.EditProfileScreen
import com.example.gymappdemo.ui.screens.ExercisePickerScreen
import com.example.gymappdemo.ui.screens.HomeScreen
import com.example.gymappdemo.ui.screens.LoginScreen
import com.example.gymappdemo.ui.screens.RegisterScreen
import com.example.gymappdemo.ui.screens.SetRepsScreen
import com.example.gymappdemo.ui.screens.UserProfileScreen
import com.example.gymappdemo.ui.theme.GymAppDemoTheme
import com.example.gymappdemo.ui.viewmodel.AppViewModelFactory
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.ui.viewmodels.ExercisePickerViewModel
import com.example.gymappdemo.ui.viewmodels.HomeViewModel
import com.example.gymappdemo.ui.viewmodels.LoginViewModel
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel
import com.example.gymappdemo.ui.viewmodels.NewsViewModel
import com.example.gymappdemo.ui.viewmodels.RegisterViewModel
import com.example.gymappdemo.ui.viewmodels.SetRepsViewModel

enum class GymAppScreen {
    Home,
    ExercisePicker,
    MyProfile,
    ProfileSettings,
    Login,
    Register,
    News
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    isAuthenticated: Boolean,
) {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as MyApplication
    val container = application.appContainer
    val factory = AppViewModelFactory(
        workoutRepository = container.workoutRepository,
        userRepository = container.userRepository,
        service = container.retrofitService,
        themePreferences = container.themePreferences
    )

    val currentStatusViewModel: CurrentStatusViewModel = viewModel(factory = factory)
    val exercisePickerViewModel: ExercisePickerViewModel = viewModel(factory = factory)
    val homeViewModel: HomeViewModel = viewModel(factory = factory)
    val setRepsViewModel: SetRepsViewModel = viewModel(factory = factory)
    val loginViewModel: LoginViewModel = viewModel(factory = factory)
    val registerViewModel: RegisterViewModel = viewModel(factory = factory)
    val myProfileViewModel: MyProfileViewModel = viewModel(factory = factory)
    val newsViewModel: NewsViewModel = viewModel(factory = factory)

    val accentColor by myProfileViewModel.selectedAccentColor.collectAsState()
    val isDarkMode by myProfileViewModel.isDarkModeEnabled.collectAsState()

    // State to track if the current destination should hide the BottomNavigationBar
    var shouldShowBottomBar by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val themePreferences = ThemePreferences(context)

    LaunchedEffect(Unit) {
        val storedTheme = themePreferences.getTheme()
        myProfileViewModel.updateTheme(storedTheme)
    }

    GymAppDemoTheme(
        darkTheme = isDarkMode,
        appThemeType = accentColor
    ) {
        SetStatusBarColor()
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
                    HomeScreen(
                        navController = navController,
                        viewModel = homeViewModel,
                        currentStatusViewModel = currentStatusViewModel,
                    )
                }
                // MyProfile Screen
                composable(route = GymAppScreen.MyProfile.name) {
                    shouldShowBottomBar = true
                    UserProfileScreen(
                        navController = navController,
                        viewModel = myProfileViewModel)
                }

                // ExercisePicker Screen
                composable("ExercisePicker/{sessionId}") { backStackEntry ->
                    val sessionId = backStackEntry.arguments?.getString("sessionId")?.toIntOrNull() ?: 0

                    ExercisePickerScreen(
                        viewModel = exercisePickerViewModel,
                        navController = navController,
                        sessionId = sessionId
                    )
                }

                // SetReps Screen
                composable(
                    route = "SetReps/{exerciseId}/{sessionId}",
                    arguments = listOf(
                        navArgument("exerciseId") { type = NavType.IntType },
                        navArgument("sessionId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val exerciseId = backStackEntry.arguments?.getInt("exerciseId")
                    val sessionId = backStackEntry.arguments?.getInt("sessionId")
                    if (exerciseId != null && sessionId != null) {
                        SetRepsScreen(
                            exerciseId = exerciseId,
                            sessionId = sessionId,
                            setRepsViewModel = setRepsViewModel,
                            navController = navController,
                            onSaveClicked = { navController.navigateUp() }
                        )
                    }
                }

                // CurrentStatus Screen
                composable(
                    route = "CurrentStatus/{sessionId}",
                    arguments = listOf(navArgument("sessionId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: return@composable
                    CurrentStatus(
                        sessionId = sessionId,
                        navController = navController,
                        viewModel = currentStatusViewModel,
                        onWorkoutTerminated = { duration, calories ->
                            homeViewModel.terminateWorkout(sessionId, duration, calories)
                        }
                    )
                }

                // Profile Settings Screen
                composable(route = GymAppScreen.ProfileSettings.name) {
                    shouldShowBottomBar = false
                    EditProfileScreen(
                        onBackPressed = { navController.navigateUp() },
                        viewModel = myProfileViewModel
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
                        loginViewModel = loginViewModel,
                        homeViewModel = homeViewModel,
                        myProfileViewModel = myProfileViewModel)
                }
                // Register Screen - Hide bottom bar
                composable(route = GymAppScreen.Register.name) {
                    shouldShowBottomBar = false
                    RegisterScreen(
                        registerViewModel = registerViewModel,
                        homeViewModel = homeViewModel,
                        myProfileViewModel = myProfileViewModel,
                        navController = navController)

                }
                composable(route = GymAppScreen.News.name) {
                    shouldShowBottomBar = true

                    NewsScreen(
                        newsViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val context = LocalContext.current

    // Menu elements
    val items = listOf(
        NavigationItem(context.getString(R.string.home), Icons.Filled.Home),
        NavigationItem(context.getString(R.string.news), Icons.Filled.Newspaper),
        NavigationItem(context.getString(R.string.profile), Icons.Filled.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column {
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), thickness = 3.dp)

        NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp),
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(

                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(32.dp),
                    )
                },

                // Check if the item's label matches the current route
                selected = currentRoute == GymAppScreen.entries[index].name,
                onClick = {
                    when (item.label) {
                        context.getString(R.string.home) -> navController.navigate(GymAppScreen.Home.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        context.getString(R.string.news) -> navController.navigate(GymAppScreen.News.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        context.getString(R.string.profile) -> navController.navigate(GymAppScreen.MyProfile.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.background

                ),
            )
        }
      }
    }
}


@Composable
fun SetStatusBarColor() {
    val backgroundColor = MaterialTheme.colorScheme.background
    val activity = (LocalContext.current as? Activity)

    SideEffect {
        activity?.window?.statusBarColor = backgroundColor.toArgb()
    }
}
data class NavigationItem(val label: String, val icon: ImageVector)