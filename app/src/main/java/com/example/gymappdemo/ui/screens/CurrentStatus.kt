package com.example.gymappdemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.data.entities.Set
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.Navigation.GymAppScreen


@Composable
fun CurrentStatus(
    sessionId: Int,
    viewModel: CurrentStatusViewModel,
    navController: NavController,
    onWorkoutTerminated: (Int) -> Unit
) {
    val timer by viewModel.timerState.collectAsState()
    val calories by viewModel.caloriesState.collectAsState()
    val exercisesWithSets by viewModel.currentExercises.collectAsState()

    LaunchedEffect(sessionId) {
        viewModel.setSessionId(sessionId)
        viewModel.startTimer()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("ExercisePicker/$sessionId") },
                shape = CircleShape,
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add Exercise"
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.resetTimer()
                    onWorkoutTerminated(timer)
                    navController.navigate(GymAppScreen.Home.name) {
                        popUpTo("CurrentStatus/$sessionId") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Τερματισμός Workout")
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.background)
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Timer and Calories
                    TimerAndCalories(
                        timer = timer,
                        calories = calories,
                        onTimerToggle = { isStarted ->
                            if (isStarted) viewModel.startTimer()
                            else viewModel.stopTimer()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // List of Exercises with Sets
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(exercisesWithSets) { exerciseWithSets ->
                            ExerciseWithSetsCard(
                                exerciseWithSets = exerciseWithSets,
                                onRemoveSet = { setId ->
                                    viewModel.removeSetAndSessionExercise(setId)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun TimerAndCalories(
    timer: Int,
    calories: Int,
    onTimerToggle: (Boolean) -> Unit
) {
    var isStarted by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                isStarted = !isStarted
                onTimerToggle(isStarted)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary
            ),
            modifier = Modifier.size(80.dp),
        ) {
            Icon(
                painter = painterResource(
                    id = if (isStarted) R.drawable.pause_circle_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    else R.drawable.start_24dp_e8eaed_fill0_wght400_grad0_opsz24
                ),
                contentDescription = if (isStarted) "Pause" else "Start",
                tint = colorScheme.onPrimary,
                modifier = Modifier.size(48.dp)
            )
        }

        // Calories Display
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Calories",
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onBackground
            )
            Text(
                text = "$calories",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary
            )
        }

        // Timer Display
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Time",
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onBackground
            )
            Text(
                text = "$timer",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary
            )
        }
    }
}

@Composable
fun ExerciseWithSetsCard(
    exerciseWithSets: ExerciseWithSets,
    onRemoveSet: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.weightlifter),
                        contentDescription = exerciseWithSets.exercise.name,
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 16.dp)
                    )
                    Text(
                        text = exerciseWithSets.exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground
                    )
                }
                // κουμπι για  διαγραφή της άσκησης
            }

            Spacer(modifier = Modifier.height(8.dp))

            exerciseWithSets.sets.forEach { set ->
                SetCard(set = set, onRemove = { onRemoveSet(set.id) })
            }
        }
    }
}

@Composable
fun SetCard(set: Set, onRemove: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column {
            Text(text = "Reps: ${set.reps}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Weight: ${set.weight} kg", style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Set",
                tint = Color.Red
            )
        }
    }
}
