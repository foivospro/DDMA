package com.example.gymappdemo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.Set
import com.example.gymappdemo.ui.viewmodels.SetRepsViewModel


@Composable
fun SetRepsScreen(
    exerciseId: Int,
    sessionId: Int,
    setRepsViewModel: SetRepsViewModel = viewModel(),
    navController: NavController,
    onSaveClicked: () -> Unit
) {
    val workoutSets by setRepsViewModel.temporarySets.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        // Save the exercise and get the generated sessionExerciseId
                        setRepsViewModel.addExerciseToSession(
                            sessionId = sessionId,
                            exerciseId = exerciseId,
                            onSuccess = { sessionExerciseId ->
                                // Save the sets after successfully saving the exercise
                                setRepsViewModel.saveSetsToDb(sessionExerciseId)
                                // Navigate to CurrentStatus
                                navController.navigate("CurrentStatus/$sessionId")
                            },
                            onError = { error ->
                                // Display a Snackbar or Log the error

                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.save))
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = setRepsViewModel.snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(id = R.string.exercise_settings),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(workoutSets) { setItem ->
                    WorkoutSetCard(
                        setItem = setItem,
                        onWeightChange = { newWeight ->
                            setRepsViewModel.updateTemporarySet(setItem.id, setItem.reps, newWeight)
                        },
                        onRepsChange = { newReps ->
                            setRepsViewModel.updateTemporarySet(setItem.id, newReps, setItem.weight)
                        },
                        onRemove = {
                            setRepsViewModel.removeTemporarySet(setItem.id)
                        }
                    )
                }

                item {
                    ElevatedButton(
                        onClick = {
                            setRepsViewModel.addTemporarySet(-1)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_set)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.add_set),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutSetCard(
    setItem: Set,
    onWeightChange: (Double) -> Unit,
    onRepsChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.set_item, setItem.id.toString()),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.weight_kg_2),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row {
                        IconButton(onClick = { onWeightChange(setItem.weight - 0.5) }) {
                            Icon(Icons.Default.Remove, contentDescription = stringResource(id = R.string.decrease_weight))
                        }
                        Text(
                            text = setItem.weight.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onWeightChange(setItem.weight + 0.5) }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.increase_weight))
                        }
                    }
                }

                Column {
                    Text(
                        text = stringResource(id = R.string.reps),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row {
                        IconButton(onClick = { onRepsChange(setItem.reps - 1) }) {
                            Icon(Icons.Default.Remove, contentDescription = stringResource(id = R.string.decrease_reps))
                        }
                        Text(
                            text = setItem.reps.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onRepsChange(setItem.reps + 1) }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.increase_reps))
                        }
                    }
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.remove_set),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}