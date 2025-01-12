package com.example.gymappdemo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.ui.viewmodels.ExercisePickerViewModel
import com.example.gymappdemo.utils.IconResourceMapper.getIconResource
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ExercisePickerScreen(
    viewModel: ExercisePickerViewModel = viewModel(),
    navController: NavController,
    sessionId: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    // Create a SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    val muscleGroups = listOf(
        stringResource(id = R.string.upper_body),
        stringResource(id = R.string.lower_body),
        stringResource(id = R.string.cardio))
    val exercises by viewModel.exercises.collectAsState()
    val selectedExercises by viewModel.selectedExercises.collectAsState()
    var selectedMuscleGroup by remember { mutableStateOf<String?>(null) }

    // Observe error messages to show Snackbar
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.setErrorMessage("") // Reset the error message after showing
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(R.string.select_exercises_title),
                ) }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Τίτλος στο κέντρο
                    Text(
                        text = "Επίλεξε Άσκηση",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    // Back Arrow στην αριστερή πλευρά
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Πίσω",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                // Search Bar
                SearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text(
                        stringResource(id =R.string.search_placeholder))
                                  },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    if (active) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            val suggestions = listOf("Bench Press", "Squat", "Deadlift")
                            suggestions.forEach { suggestion ->
                                ListItem(
                                    headlineContent = { Text(suggestion) },
                                    modifier = Modifier.clickable {
                                        query = suggestion
                                        active = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Muscle Groups Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    muscleGroups.forEach { muscleGroup ->
                        val isSelected = muscleGroup == selectedMuscleGroup
                        FilterChip(
                            onClick = {
                                selectedMuscleGroup = if (isSelected) null else muscleGroup
                            },
                            label = { Text(muscleGroup) },
                            selected = isSelected,
                            modifier = Modifier.padding(start = 8.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.FilterAlt,
                                    contentDescription = null
                                )
                            },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSecondary,
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = MaterialTheme.colorScheme.outline,
                                selectedBorderColor = MaterialTheme.colorScheme.primary,
                                disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                            )
                        )
                    }
                }

                // Filtered Exercises based on search query and muscle group
                val filteredExercises = exercises.filter { exercise ->
                    (selectedMuscleGroup == null || exercise.muscleGroup.equals(
                        selectedMuscleGroup,
                        ignoreCase = true
                    )) &&
                            (query.isBlank() || exercise.name.contains(query, ignoreCase = true))
                }

                // Exercise List
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (filteredExercises.isEmpty()) {
                        item {
                            Text(
                                text = "No exercises match your search.",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        itemsIndexed(filteredExercises) { index, exercise ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessVeryLow,
                                        dampingRatio = Spring.DampingRatioLowBouncy
                                    ),
                                    initialOffsetY = { it * (index + 1) }
                                ),
                                exit = fadeOut()
                            ) {
                                ExerciseCard(
                                    exercise = exercise,
                                    navController = navController,
                                    currentSessionId = sessionId,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    )
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    navController: NavController,
    currentSessionId: Int,
    viewModel: ExercisePickerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language = context.resources.configuration.locales[0].language
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise Icon
            Icon(
                painter = painterResource(id = getIconResource(exercise.icon)),
                contentDescription = exercise.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Exercise Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when {
                        language == "en" -> exercise.descriptionEn
                        language == "es" -> exercise.descriptionEl
                        else -> exercise.muscleGroup
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Add Button
            Button(
                onClick = {
                    navController.navigate("SetReps/${exercise.id}/$currentSessionId")
                },
                modifier = Modifier
                    .sizeIn(minWidth = 80.dp, minHeight = 36.dp)
            ) {
                Text(
                    stringResource(id = R.string.add),
                    style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}