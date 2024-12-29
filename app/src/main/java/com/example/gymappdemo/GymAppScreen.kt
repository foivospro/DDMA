package com.example.gymappdemo


// For composable() and NavHost

// For NavController
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymappdemo.sampledata.Excercise
import com.example.gymappdemo.ui.QuickStartRoutinesUI
import com.example.gymappdemo.ui.theme.ExcercisesList

enum class GymAppScreen {
    Home,
    ExercisePicker
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar() }
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
            composable("QuickStartRoutinesUI") { QuickStartRoutinesUI() }
            // Exercise Picker Screen in NavHost
            composable(route = GymAppScreen.ExercisePicker.name) {
                ExcercisesList(
                    listOf(
                        Excercise(R.string.snatch, R.drawable.weightlifter, R.string.snatch),
                        Excercise(R.string.biceps, R.drawable.weightlifter, R.string.biceps),
                        Excercise(R.string.bench, R.drawable.weightlifter, R.string.bench),
                        Excercise(R.string.snatch, R.drawable.weightlifter, R.string.snatch),
                        Excercise(R.string.biceps, R.drawable.weightlifter, R.string.biceps),
                        Excercise(R.string.bench, R.drawable.weightlifter, R.string.bench),
                        Excercise(R.string.snatch, R.drawable.weightlifter, R.string.snatch),
                        Excercise(R.string.biceps, R.drawable.weightlifter, R.string.biceps),
                        Excercise(R.string.bench, R.drawable.weightlifter, R.string.bench)
                    )
                )
            }
        }
    }
}



@Composable
fun BottomNavigationBar() {
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
                onClick = { selectedItem = index },
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