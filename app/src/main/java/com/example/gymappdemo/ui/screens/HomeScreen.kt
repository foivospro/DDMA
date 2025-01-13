package com.example.gymappdemo.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymappdemo.R
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.ui.viewmodels.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    currentStatusViewModel: CurrentStatusViewModel
) {
    val isWorkoutActive by viewModel.isWorkoutActive.collectAsState()
    val currentSessionId by viewModel.currentSessionId.collectAsState()
    val username by viewModel.username.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0]
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when {
                        locale.language == "el" && username == "Guest" -> stringResource(R.string.hi_user, "Επισκέπτη")
                        else -> stringResource(R.string.hi_user, username)
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Text(
                text = "Workout History",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp, modifier = Modifier.padding(bottom = 16.dp))

            WorkoutHistory()
        }

        // Workout Summary Section
        item {
            Spacer(modifier = Modifier.height(24.dp))

            WorkoutSummary()
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
            Button(onClick = {
                    if (!isWorkoutActive) {
                        viewModel.startNewWorkout(
                            userId = userId,
                            onSessionCreated = { session ->
                                currentStatusViewModel.setSessionId(session.id)
                                navController.navigate("CurrentStatus/${session.id}")
                            },
                            onError = { error ->
                                Log.e("HomeScreen", "Failed to start new workout: $error")
                            }
                        )
                    } else {
                        currentSessionId?.let { sessionId ->
                            navController.navigate("CurrentStatus/$sessionId")
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp), shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                )) {
                Text(
                    text = stringResource(
                        id = if (isWorkoutActive) R.string.continue_workout else R.string.start_new_workout
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isWorkoutActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.active_workout_warning),
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WorkoutHistory() {
    val workoutList = listOf(
        Workout(
            iconRes = R.drawable.weightlifter,
            descriptionRes = R.string.biceps_work,
            date = "2024-04-20",
            duration = "45 λεπτά",
            caloriesBurned = 350
        ),
        Workout(
            iconRes = R.drawable.weightlifter,
            descriptionRes = R.string.cardio_work,
            date = "2024-04-18",
            duration = "30 λεπτά",
            caloriesBurned = 250
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        workoutList.forEach { workout ->
            WorkoutCard(workout = workout)
        }
    }
}

@Composable
fun WorkoutCard(workout: Workout) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = workout.iconRes),
                contentDescription = stringResource(id = workout.descriptionRes),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = workout.descriptionRes),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.date, workout.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.duration, workout.duration),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.calories_burned, workout.caloriesBurned),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Composable
fun WorkoutSummary() {
    // Dummy Data
    val totalWorkouts = 10
    val workoutsPerWeek = listOf(2, 3, 1, 4, 2, 3, 0)
    val maxBarHeight = 100.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.workout_summary),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.total_workouts, totalWorkouts),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxBarHeight + 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            val maxWorkouts = workoutsPerWeek.maxOrNull() ?: 1
            workoutsPerWeek.forEach { count ->
                val barHeight = if (maxWorkouts > 0) {
                    (count.toFloat() / maxWorkouts) * maxBarHeight.value
                } else {
                    0f
                }
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height(barHeight.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = stringResource(R.string.monday), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(text = stringResource(R.string.tuesday), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(text = stringResource(R.string.wednesday), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(text = stringResource(R.string.thursday), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(text = stringResource(R.string.friday), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(text = stringResource(R.string.saturday), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(text = stringResource(R.string.sunday), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}


data class Workout(
    @DrawableRes val iconRes: Int,
    @StringRes val descriptionRes: Int,
    val date: String,
    val duration: String,
    val caloriesBurned: Int
)

