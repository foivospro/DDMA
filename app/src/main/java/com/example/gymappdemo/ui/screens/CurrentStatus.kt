package com.example.gymappdemo.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
@Composable
fun CurrentStatus(
    sessionId: Int,
    viewModel: CurrentStatusViewModel,
    navController: NavController,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val timer by viewModel.timerState.collectAsState()
    val calories by viewModel.caloriesState.collectAsState()
    val exercisesWithSets by viewModel.currentExercises.collectAsState()

    // Φόρτωση ασκήσεων με τα σετ όταν ανοίγει η οθόνη
    LaunchedEffect(sessionId) {
        viewModel.loadExercises(sessionId)
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
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.background)
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
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

                    // List of Exercises with Sets
                    LazyColumn(
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(exercisesWithSets) { exerciseWithSets ->
                            ExerciseCard(
                                exerciseWithSets = exerciseWithSets,
                                onRemoveSet = { setId ->
                                    viewModel.removeSetAndSessionExercise(setId) // Διαγραφή του set
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
fun ExerciseCard(
    exerciseWithSets: ExerciseWithSets,
    onRemoveSet: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = exerciseWithSets.exercise.name,
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Σετ:",
                style = MaterialTheme.typography.titleSmall,
                color = colorScheme.onSurface
            )
            exerciseWithSets.sets.forEach { set ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Επαναλήψεις: ${set.reps}, Βάρος: ${set.weight}kg",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                    IconButton(onClick = { onRemoveSet(set.id) }) {
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun CurrentStatusPreview() {

}