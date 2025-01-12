package com.example.gymappdemo.ui.screens

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymappdemo.Navigation.GymAppScreen
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.data.entities.Set
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel

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
    var setToEdit by remember { mutableStateOf<Set?>(null) }
    var setToAdd by remember { mutableStateOf<Int?>(null) }
    val error by viewModel.errorState.collectAsState()

    if (error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text(stringResource(R.string.error)) },
            text = { Text(error!!) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    if (setToEdit != null) {
        EditSetDialog(
            set = setToEdit!!,
            onDismiss = { setToEdit = null },
            onSave = { updatedSet ->
                viewModel.updateSet(updatedSet)
                setToEdit = null
            }
        )
    }

    if (setToAdd != null) {
        AddSetDialog(
            onDismiss = { setToAdd = null },
            onSave = { repetitions, weight ->
                viewModel.addSetToExercise(
                    sessionExerciseId = setToAdd!!,
                    repetitions = repetitions,
                    weight = weight,
                )
                setToAdd = null
            }
        )
    }

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
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.termination_workout))

                Button(
                    onClick = { navController.navigate("ExercisePicker/$sessionId") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "Προσθήκη Άσκησης",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Προσθήκη Άσκησης", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
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
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Τερματισμός Workout")
                }
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Προπόνηση",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.primary
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Πίσω",
                                tint = colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (exercisesWithSets.isEmpty()) {
                            item {

                                Text(stringResource(R.string.no_exercises_available), modifier = Modifier.padding(16.dp))

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Δεν έχει προστεθεί κάποια άσκηση ακόμα.",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                        color = colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            items(exercisesWithSets) { exerciseWithSets ->
                                Log.d(
                                    "CurrentStatus",
                                    "Displaying exercise: ${exerciseWithSets.exercise.name}, sets: ${exerciseWithSets.sets}"
                                )
                                ExerciseWithSetsCard(
                                    exerciseWithSets = exerciseWithSets,
                                    onRemoveSet = { setId ->
                                        viewModel.removeSetFromExercise(setId)
                                    },
                                    onDeleteExercise = { exerciseId ->
                                        viewModel.deleteExercise(exerciseId)
                                    },
                                    onEditSet = { set ->
                                        setToEdit = set
                                    },
                                    onAddSet = { sessionExerciseId ->
                                        setToAdd = sessionExerciseId
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AddSetDialog(
    onDismiss: () -> Unit,
    onSave: (Int, Double) -> Unit
) {
    var repetitions by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_new_set)) },
        text = {
            Column {
                OutlinedTextField(
                    value = repetitions,
                    onValueChange = { repetitions = it },
                    label = { Text(stringResource(R.string.repetitions)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text(stringResource(R.string.weight_kg_2)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reps = repetitions.toIntOrNull()
                    val wt = weight.toDoubleOrNull()
                    if (reps != null && reps > 0 && wt != null && wt >= 0f) {
                        onSave(reps, wt)
                    }
                }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun EditSetDialog(set: Set, onDismiss: () -> Unit, onSave: (Set) -> Unit) {
    var reps by remember { mutableStateOf(set.reps.toString()) }
    var weight by remember { mutableStateOf(set.weight.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_set)) },
        text = {
            Column {
                OutlinedTextField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = { Text(stringResource(R.string.repetitions)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text(stringResource(R.string.weight)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedSet = set.copy(
                        reps = reps.toIntOrNull() ?: set.reps,
                        weight = weight.toDoubleOrNull() ?: set.weight
                    )
                    onSave(updatedSet)
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
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

    val formattedTime = formatTime(timer)

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
                contentDescription = if (isStarted) stringResource(R.string.pause) else stringResource(R.string.start),
                tint = colorScheme.onPrimary,
                modifier = Modifier.size(48.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.calories),
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onBackground
            )
            Text(
                text = "$calories",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.time),
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onBackground
            )
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary
            )
        }
    }
}

fun formatTime(seconds: Int): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hrs > 0) {
        String.format("%d:%02d:%02d", hrs, mins, secs)
    } else {
        String.format("%d:%02d", mins, secs)
    }
}

@Composable
fun ExerciseWithSetsCard(
    exerciseWithSets: ExerciseWithSets,
    onRemoveSet: (Int) -> Unit,
    onDeleteExercise: (Int) -> Unit,
    onEditSet: (Set) -> Unit,
    onAddSet: (Int) -> Unit
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
                Row {
                    IconButton(onClick = { onAddSet(exerciseWithSets.sessionExercise.id) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_set),
                            tint = Color(0xFF0AAD0A)
                        )
                    }
                    IconButton(onClick = { onDeleteExercise(exerciseWithSets.sessionExercise.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_exercise),
                            tint = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            exerciseWithSets.sets.forEach { set ->
                SetCard(
                    set = set,
                    onRemove = { onRemoveSet(set.id) },
                    onEdit = { onEditSet(set) }
                )
            }
        }
    }
}

@Composable
fun SetCard(set: Set, onRemove: () -> Unit, onEdit: (Set) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column {
            Text(text = stringResource(R.string.reps, set.reps), style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(R.string.weight_kg, set.weight), style = MaterialTheme.typography.bodyMedium)
        }
        Row {
            IconButton(onClick = { onEdit(set) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_set),
                    tint = Color.DarkGray
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_exercise),
                    tint = Color.Red
                )
            }
        }
    }
}
