package com.example.gymappdemo.ui.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappdemo.R
import com.example.gymappdemo.sampledata.Excercise

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ExcercisesList(
    excercises: List<Excercise>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    val muscleGroups = listOf("Chest", "Legs", "Arms")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            // Search Bar
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Search...") },
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
                        val suggestions = listOf("Suggestion 1", "Suggestion 2", "Suggestion 3")
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                for (muscleGroup in muscleGroups) {
                    FilterChip(
                        onClick = { /*TODO*/ },
                        label = { Text(muscleGroup) },
                        selected = false,
                        modifier = Modifier
                            .padding(start = 16.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.filter_alt),
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(50), // Pill-shaped design
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.onSecondary, // Default background
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
            // Filtered Exercise List
            val filteredExercises = if (query.isNotEmpty()) {
                excercises.filter {
                    stringResource(it.name).contains(query, ignoreCase = true)
                }
            } else {
                excercises
            }

            LazyColumn(
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxSize()
            ) {
                if (filteredExercises.isEmpty()) {
                    item {
                        Text(
                            text = "No exercises match your search.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
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
                                initialOffsetY = { it * (index + 1) } // Staggered entrance
                            ),
                            exit = fadeOut()
                        ) {
                            ExerciseCard(
                                iconResource = exercise.image,
                                descriptionRes = exercise.description,
                                name = exercise.name,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }


@Composable
fun ExerciseCard(
    @DrawableRes iconResource: Int,
    @StringRes descriptionRes: Int,
    @StringRes name: Int,
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit = {} // Callback for the button
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier,
        shape = RoundedCornerShape(24.dp) // Rounded corners for the card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {

            // Image and Button Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(iconResource),
                    contentDescription = stringResource(name),
                    modifier = Modifier
                        .size(56.dp) // Adjust size for better fit
                        .clip(RoundedCornerShape(8.dp)),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .sizeIn(minWidth = 80.dp, minHeight = 24.dp)
                ) {
                    Text("Add", style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.width(40.dp))
            // Text Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(name),
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = stringResource(descriptionRes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSample() {
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Scaffold { scaffoldPadding ->
        Box(Modifier.fillMaxSize()) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(scaffoldPadding)
            ) {
                if (active) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Example suggestions
                        val suggestions = listOf("Suggestion 1", "Suggestion 2", "Suggestion 3")
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

            LazyColumn(
                contentPadding = PaddingValues(top = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                val items = List(100) { "Item $it" }
                items(items) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun HeroPreview() {
    GymAppDemoTheme {
        val mockExercises = listOf(
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
            Excercise(name = R.string.biceps, description = R.string.biceps_des, image = R.drawable.weightlifter),
        )
        ExcercisesList(excercises = mockExercises)
    }
}
