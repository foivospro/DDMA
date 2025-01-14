package com.example.gymappdemo.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    val userId by viewModel.userId.collectAsState()
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0]
    val sessionList by viewModel.userSessions.collectAsState()

    // -- Περνάμε σε Scaffold αντί για απλό LazyColumn --
    Scaffold(
        // bottomBar: pinned στο κάτω μέρος
        bottomBar = {
            // Τοποθετούμε το κουμπί σε Row ή απευθείας σε Surface / Box
            BottomBar(
                isWorkoutActive = isWorkoutActive,
                currentSessionId = currentSessionId,
                userId = userId,
                onStartNewWorkout = { userIdValue ->
                    // Κλήση της συνάρτησης περνώντας userId και onError
                    viewModel.startNewWorkout(
                        userId = userIdValue,
                        onSessionCreated = { session ->
                            currentStatusViewModel.setSessionId(session.id)
                            navController.navigate("CurrentStatus/${session.id}")
                        },
                        onError = { error ->
                            Log.e("HomeScreen", "Failed to start new workout: $error")
                        }
                    )
                },
                onContinueWorkout = { sessionId ->
                    navController.navigate("CurrentStatus/$sessionId")
                }
            )
        }
    ) { innerPadding ->

        // Το scrollable περιεχόμενο πάει στο Scaffold content
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // -- (1) Hello / Hi user row --
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when {
                            locale.language == "el" && username == "Guest" ->
                                stringResource(R.string.hi_user, "Επισκέπτη")
                            else ->
                                stringResource(R.string.hi_user, username)
                        },
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // -- (2) Workout History Title --
            item {
                Text(
                text = stringResource(R.string.workout_history),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // -- (3) Items με GymSessionCard --
            items(sessionList) { session ->
                GymSessionCard(session)
            }

            // -- (4) WorkoutSummary κάτω από τα items --
            item {
                Spacer(modifier = Modifier.height(24.dp))
                WorkoutSummary(sessionList = sessionList)
            }

            // Τέλος LazyColumn
        }
    }
}

// -- Π pinned bottom Bar me to koumpi mas --
@Composable
fun BottomBar(
    isWorkoutActive: Boolean,
    currentSessionId: Int?,
    userId: Int?,
    onStartNewWorkout: (Int) -> Unit,
    onContinueWorkout: (Int) -> Unit
) {
    Surface(shadowElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (!isWorkoutActive) {
                        if (userId == null) {
                            // Αν θέλεις, μπορείς να κάνεις κάτι άλλο
                            Log.e("BottomBar", "UserId is null, maybe show login screen?")
                        } else {
                            onStartNewWorkout(userId)
                        }
                    } else {
                        currentSessionId?.let { onContinueWorkout(it) }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (isWorkoutActive)
                        stringResource(R.string.continue_workout)
                    else
                        stringResource(R.string.start_new_workout),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// -- GymSessionCard: Προσθέτουμε και τις θερμίδες --
@Composable
fun GymSessionCard(session: GymSession) {
    val minutes = session.duration / 60
    val seconds = session.duration % 60
    val durationText = if (seconds > 0) {
        "$minutes λεπτά και $seconds δευτερόλεπτα"
    } else {
        "$minutes λεπτά"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.weightlifter),
                contentDescription = stringResource(R.string.workout_icon_description),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = stringResource(R.string.workout_label),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Ημερομηνία
                Text(
                    text = stringResource(R.string.date_label, session.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Διάρκεια
                Text(
                    text = stringResource(R.string.duration_label, durationText),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Θερμίδες - Ας υποθέσουμε ότι το πεδίο λέγεται `caloriesBurned`
                Text(
                    text = stringResource(R.string.calories_label, session.caloriesBurned),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutSummary(sessionList: List<GymSession>) {
    if (sessionList.isEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.no_workouts_yet),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    } else {
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
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(R.string.monday),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.tuesday),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.wednesday),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.thursday),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.friday),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.saturday),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.sunday),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
