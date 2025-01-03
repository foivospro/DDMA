package com.example.gymappdemo.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gymappdemo.R

@Composable
fun CurrentStatus(
    navController: NavController,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    // State to track whether the button is in "Start" or "Pause" mode
    var isStarted by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .size(80.dp) // Uniform size
                    .padding(horizontal = 8.dp)
                    .clickable { isStarted = !isStarted },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = if (isStarted) {
                                    listOf(
                                        Color(0xFF90A4AE), // Formal steel blue for Pause
                                        Color(0xFF78909C)
                                    )
                                } else {
                                    listOf(
                                        Color(0xFFECEFF1), // Subtle gray for Start
                                        Color(0xFFCFD8DC)
                                    )
                                }
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp), // Ensure spacing inside the card
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isStarted) R.drawable.pause_circle_24dp_e8eaed_fill0_wght400_grad0_opsz24
                                else R.drawable.start_24dp_e8eaed_fill0_wght400_grad0_opsz24
                            ),
                            contentDescription = if (isStarted) "Pause" else "Start",
                            modifier = Modifier.size(36.dp), // Slightly smaller icon for better balance
                            tint = Color.White
                        )
                        Text(
                            text = if (isStarted) "Pause" else "Start",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp) // Space between icon and text
                        )
                    }
                }
            }

            // Card 1: Calories
                Card(
                    modifier = Modifier
                        .size(80.dp) // Uniform size
                        .padding(horizontal = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFECEFF1), // Subtle gray gradient
                                        Color(0xFFCFD8DC)
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.calories),
                                contentDescription = "Calories",
                                modifier = Modifier.size(32.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = "Calories",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            // Card 2: Timer
            Card(
                modifier = Modifier
                    .size(80.dp)
                    .padding(horizontal = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFECEFF1), // Subtle gray gradient
                                    Color(0xFFCFD8DC)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.timer),
                            contentDescription = "Timer",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "Minutes: 15",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize(), // Occupy the remaining screen space
            contentAlignment = Alignment.Center // Center content both horizontally and vertically
        ) {

            Card(
                modifier = Modifier
                    .fillMaxSize() // Occupy most of the screen height
                    .padding(16.dp), // Outer padding for spacing
                elevation = CardDefaults.cardElevation(10.dp), // Slightly higher elevation for prominence
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp) // Rounded corners for modern design
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Inner padding for content
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = modifier.fillMaxWidth()
                    ) {
                        items(
                            listOf(
                                Pair(R.drawable.weightlifter, "Gym"), // Sample data
                                Pair(R.drawable.weightlifter, "Test"),
                                Pair(R.drawable.weightlifter, "Gym"), // Sample data
                                Pair(R.drawable.weightlifter, "Test"),
                                Pair(R.drawable.weightlifter, "Gym"), // Sample data
                                Pair(R.drawable.weightlifter, "Test")
                            )
                        ) { (iconResource, name) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,

                            ) {
                                SetsAndRepsCard(
                                    iconResource = R.drawable.weightlifter,
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

                Button(
                    onClick = { navController.navigate("ExercisePicker") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd) // Align at the bottom left
                        .padding(16.dp) // Padding from edges,
                    , shape = RoundedCornerShape(100) // Fully rounded button for a circular appearance
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "Add Exercise"
                    )
                }

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
    val mockNavController = rememberNavController()
    CurrentStatus(mockNavController)
}