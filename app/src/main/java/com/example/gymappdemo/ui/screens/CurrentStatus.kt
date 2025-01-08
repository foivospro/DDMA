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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel

@Composable
fun CurrentStatus(
    viewModel: CurrentStatusViewModel,
    navController: NavController,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    // State to track whether the button is in "Start" or "Pause" mode
    var isStarted by remember { mutableStateOf(true) }
    val timer by viewModel.timerState.collectAsState()
    val calories by viewModel.caloriesState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
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
                        if (isStarted) {
                            viewModel.startTimer() // Start the timer
                        } else {
                            viewModel.stopTimer() // Stop the timer
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.size(80.dp),
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isStarted) R.drawable.pause_circle_24dp_e8eaed_fill0_wght400_grad0_opsz24
                            else R.drawable.start_24dp_e8eaed_fill0_wght400_grad0_opsz24
                        ),
                        contentDescription = if (isStarted) "Pause" else "Start",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Calories
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Calories",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$calories",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Time
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$timer",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Card: Exercise List
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Use weight to occupy remaining space below the top row
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(
                            listOf(
                                Pair(R.drawable.weightlifter, "Gym"),
                                Pair(R.drawable.weightlifter, "Test"),
                            )
                        ) { (iconResource, name) ->
                            SetsAndRepsCard(
                                iconResource = iconResource,
                                name = name,
                                sets = 3,
                                reps = 10,
                                weight = 50,
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button: Placed at the bottom-right
        FloatingActionButton(
            onClick = { navController.navigate("ExercisePicker") },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align to the bottom-end of the Box
                .padding(16.dp) // Padding from edges
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Add Exercise"
            )
        }
    }
}

@Composable
fun SetsAndRepsCard(
    @DrawableRes iconResource: Int,
    sets: Int,
    reps: Int,
    weight: Int,
    name: String,
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
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(iconResource),
                    contentDescription = name,
                    modifier = Modifier
                        .size(56.dp) // Adjust size for better fit
                        .clip(RoundedCornerShape(8.dp)),
                    tint = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .sizeIn(minWidth = 80.dp, minHeight = 24.dp)
                ) {
                    Text("Remove", style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.width(40.dp))
            // Text Column

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sets), // Replace with your set icon
                        contentDescription = "Sets",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Sets: ",
                        style = MaterialTheme.typography.bodyLarge, // Slightly larger text
                        color = Color.Black
                    )
                    Text(
                        text = "3",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.DarkGray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.reps), // Replace with your reps icon
                        contentDescription = "Reps",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "Reps: ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    Text(
                        text = reps.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.DarkGray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.weight), // Replace with your weight icon
                        contentDescription = "Weight",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "Weight: ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    Text(
                        text = weight.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.DarkGray
                    )
                }
            }
            }
        }
    }


@Preview
@Composable
fun CurrentStatusPreview() {

}