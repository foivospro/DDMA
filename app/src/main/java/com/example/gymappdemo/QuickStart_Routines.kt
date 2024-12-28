package com.example.gymappdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.res.painterResource



@Composable
fun QuickStartRoutinesUI() {
    // Background color of the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Black background
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title "Quick Start"
            Text(
                text = "Quick Start",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // "Start Empty Workout" Button
            StartWorkoutButton()

            Spacer(modifier = Modifier.height(32.dp)) // Space between sections

            // Title "Routines"
            Text(
                text = "Routines",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // "New Routine" Button
            NewRoutineButton()

            Spacer(modifier = Modifier.height(16.dp)) // Space between buttons

            // "Explore Routines" Button
            ExploreRoutinesButton()
        }
    }
}

@Composable
fun StartWorkoutButton() {
    Button(
        onClick = { /* TODO: Add functionality */ },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.start_new_workout_icon), // Use your drawable resource
                contentDescription = "Start New Workout Icon",
                modifier = Modifier.size(24.dp) // Adjust size
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            Text(text = "Start Empty Workout",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun NewRoutineButton() {
    Button(
        onClick = { /* TODO: Add functionality */ },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.new_routine_icon), // Use your drawable resource
                contentDescription = "New Routine Icon",
                modifier = Modifier.size(24.dp) // Adjust size
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            Text(text = "New Routine",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ExploreRoutinesButton() {
    Button(
        onClick = { /* TODO: Add functionality */ },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.explore_routines_icon), // Use your drawable resource
                contentDescription = "Explore Routines Icon",
                modifier = Modifier.size(24.dp) // Adjust size
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            Text(text = "Explore Routines",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}