package com.example.gymappdemo.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.GymSession
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.ui.viewmodels.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val sessionList by viewModel.userSessions.collectAsState()

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
                        text = "Hi 👋, $username",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        item {
            Text(
                text = "Workout History",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(bottom = 16.dp))

            WorkoutHistory(sessionList)
        }

        // Workout Summary Section
        item {
            Spacer(modifier = Modifier.height(24.dp))

            WorkoutSummary(sessionList = sessionList)
        }

        // Start New Workout / Continue Workout Button
        item {
            Spacer(modifier = Modifier.height(64.dp))

            Button(onClick = {
                    if (!isWorkoutActive) {
                        viewModel.startNewWorkout(
                            userId = 1, // αυτο πρεπει να το αλλαξουμε με το πραγματικό
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
                    .height(60.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )) {
                Text(
                    text = if (isWorkoutActive) "Continue Workout" else "Start New Workout",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isWorkoutActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Έχει ήδη ενεργή προπόνηση.",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WorkoutHistory(sessionList: List<GymSession>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        sessionList.forEach { session ->
            GymSessionCard(session)
        }
    }
}

@Composable
fun GymSessionCard(session: GymSession) {
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
                painter = painterResource(id = R.drawable.weightlifter),
                contentDescription = "Workout Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Workout",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ημερομηνία: ${session.date}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Διάρκεια: ${session.duration} λεπτά",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutSummary(sessionList: List<GymSession>) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val totalWorkouts = sessionList.size

    val workoutsPerWeek = (1..7).map { dayIndex ->
        sessionList.count { session ->
            val localDate = LocalDate.parse(session.date, formatter)
            localDate.dayOfWeek.value == dayIndex
        }
    }

    val maxBarHeight = 100.dp
    val maxWorkouts = workoutsPerWeek.maxOrNull() ?: 1

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Σύνοψη Workout",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Συνολικά Workouts: $totalWorkouts",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxBarHeight + 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Δευ", fontSize = 12.sp)
            Text(text = "Τρ", fontSize = 12.sp)
            Text(text = "Τετ", fontSize = 12.sp)
            Text(text = "Πεμ", fontSize = 12.sp)
            Text(text = "Παρ", fontSize = 12.sp)
            Text(text = "Σαβ", fontSize = 12.sp)
            Text(text = "Κυρ", fontSize = 12.sp)
        }
    }
}

data class NavigationItem(val label: String, val icon: ImageVector)
