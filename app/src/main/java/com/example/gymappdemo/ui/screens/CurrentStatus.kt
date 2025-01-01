package com.example.gymappdemo.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Box(
        modifier = Modifier
            .fillMaxSize(), // Occupy the full screen
        contentAlignment = Alignment.Center // Center content both horizontally and vertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f) // Occupy 75% of the screen width
                .fillMaxHeight(0.75f) // Occupy 75% of the screen height
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp), // Inner padding for the border
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                items(
                    listOf(
                        Pair(R.drawable.weightlifter, "Gym"), // Sample data
                        Pair(R.drawable.weightlifter, "Test")
                    )
                ) { (iconResource, name) ->
                    ExerciseCard(
                        iconResource = iconResource,
                        description = "Test Description",
                        name = name
                    )
                }
            }
        }
        Button(
            onClick = { navController.navigate("ExercisePicker") },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Aligns the button at the bottom center of the parent
                .padding(bottom = 32.dp) // Adds padding to the top of the button
        ) {
            Text("Start Workout")
        }

    }
}


@Preview
@Composable
fun CurrentStatusPreview() {
    val mockNavController = rememberNavController()
    CurrentStatus(mockNavController)
}
