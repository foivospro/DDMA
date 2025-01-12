package com.example.gymappdemo.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.Set
import com.example.gymappdemo.ui.viewmodels.SetRepsViewModel
import com.example.gymappdemo.utils.IconResourceMapper.getIconResource


@Composable
fun SetRepsScreen(
    exerciseId: Int,
    sessionId: Int,
    setRepsViewModel: SetRepsViewModel,
    navController: NavController,
    onSaveClicked: () -> Unit
) {
    val workoutSets by setRepsViewModel.temporarySets.collectAsState(initial = emptyList())
    val exerciseName by setRepsViewModel.exerciseName.collectAsState()
    val exerciseIcon by setRepsViewModel.exerciseImageUrl.collectAsState()

    LaunchedEffect(key1 = exerciseId) {
        setRepsViewModel.loadExerciseDetails(exerciseId)
    }

    LaunchedEffect(key1 = workoutSets.isEmpty()) {
        if (workoutSets.isEmpty()) {
            setRepsViewModel.addTemporarySet(dummySessionExerciseId = -1)
        }
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
                        setRepsViewModel.addExerciseToSession(
                            sessionId = sessionId,
                            exerciseId = exerciseId,
                            onSuccess = { sessionExerciseId ->
                                setRepsViewModel.saveSetsToDb(sessionExerciseId)
                                navController.navigate("CurrentStatus/$sessionId")
                            },
                            onError = { error ->

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Πίσω",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ρύθμιση Άσκησης",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exerciseName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            if (exerciseIcon.isNotEmpty()) {
                val resourceId = getIconResource(exerciseIcon)

                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = "$exerciseName Εικόνα",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Δεν υπάρχει εικόνα διαθέσιμη",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_set),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.add_set),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
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
    var weightInput by remember { mutableStateOf(setItem.weight.toString()) }

    LaunchedEffect(setItem.weight) {
        weightInput = setItem.weight.toString()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = stringResource(id = R.string.decrease_weight)
                            )
                        }
                        Text(
                            text = setItem.weight.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onWeightChange(setItem.weight + 0.5) }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.increase_weight)
                            )
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
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = stringResource(id = R.string.decrease_reps)
                            )
                        }
                        Text(
                            text = setItem.reps.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onRepsChange(setItem.reps + 1) }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.increase_reps)
                            )
                        }
                    }
                }

                IconButton(onClick = onRemove) {
                    Text(
                        text = "Set #${setItem.id}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.remove_set),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Βάρος (kg)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Επαναλήψεις",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onWeightChange((setItem.weight - 0.5).coerceAtLeast(0.0)) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Μείωση Βάρους",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        OutlinedTextField(
                            value = weightInput,
                            onValueChange = { newValue ->
                                if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                                    weightInput = newValue
                                    newValue.toDoubleOrNull()?.let { validWeight ->
                                        onWeightChange(validWeight)
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .width(75.dp)
                                .height(55.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            )
                        )

                        IconButton(
                            onClick = { onWeightChange(setItem.weight + 0.5) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Αύξηση Βάρους",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onRepsChange((setItem.reps - 1).coerceAtLeast(1)) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Μείωση Επαναλήψεων",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = setItem.reps.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { onRepsChange(setItem.reps + 1) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Αύξηση Επαναλήψεων",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}