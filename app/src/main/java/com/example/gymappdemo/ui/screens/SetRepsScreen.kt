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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymappdemo.ui.viewmodels.SetRepsViewModel
import com.example.gymappdemo.data.entities.Set


@Composable
fun SetRepsScreen(
    sessionExerciseId: Int,
    setRepsViewModel: SetRepsViewModel = viewModel(),
    navController: NavController,
    onSaveClicked: () -> Unit
) {
    val workoutSets by setRepsViewModel.temporarySets.collectAsState(initial = emptyList())

    LaunchedEffect(sessionExerciseId) {
        setRepsViewModel.loadSets(sessionExerciseId)
    }

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
                        setRepsViewModel.saveSetsToDb(sessionExerciseId)
                        navController.navigate("CurrentStatus/$sessionExerciseId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Αποθήκευση")
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
                text = "Ρύθμιση Άσκησης",
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
                            setRepsViewModel.addTemporarySet(sessionExerciseId)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Set"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Προσθήκη Set",
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
                text = "Set #${setItem.id}",
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
                        text = "Βάρος (kg)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row {
                        IconButton(onClick = { onWeightChange(setItem.weight - 0.5) }) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease Weight")
                        }
                        Text(
                            text = setItem.weight.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onWeightChange(setItem.weight + 0.5) }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase Weight")
                        }
                    }
                }

                Column {
                    Text(
                        text = "Επαναλήψεις",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row {
                        IconButton(onClick = { onRepsChange(setItem.reps - 1) }) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease Reps")
                        }
                        Text(
                            text = setItem.reps.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onRepsChange(setItem.reps + 1) }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase Reps")
                        }
                    }
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Set",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}