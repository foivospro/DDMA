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
import androidx.compose.foundation.lazy.items
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
                text = stringResource(R.string.workout_history),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(bottom = 16.dp))
        }


        items(sessionList) { session ->
            GymSessionCard(session)
        }


        item {
            Spacer(modifier = Modifier.height(24.dp))

            WorkoutSummary(sessionList = sessionList)
        }

        // Start New Workout / Continue Workout Button
        item {
            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                    if (!isWorkoutActive) {
                        // Πάρε το userId από το ViewModel
                        val userIdValue = userId  // ή viewModel.userId.collectAsState().value

                        if (userIdValue == null) {
                            // Εδώ μπορείς να κάνεις π.χ. πλοήγηση σε Login Screen
                            navController.navigate("LoginScreen")
                        } else {
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
                        }
                    } else {
                        // Αν υπάρχει ενεργή προπόνηση, πήγαινε στην CurrentStatus
                        currentSessionId?.let { sessionId ->
                            navController.navigate("CurrentStatus/$sessionId")
                        }
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
fun GymSessionCard(session: GymSession) {
    // Υπολογισμός χρόνου
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
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(
                        R.string.date_label,
                        session.date
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Text(
                    text = stringResource(
                        R.string.duration_label,
                        durationText
                    ),
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
            text = stringResource(R.string.workout_summary),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.total_workouts, totalWorkouts),
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
            Text(text = stringResource(R.string.monday), fontSize = 12.sp)
            Text(text = stringResource(R.string.tuesday), fontSize = 12.sp)
            Text(text = stringResource(R.string.wednesday), fontSize = 12.sp)
            Text(text = stringResource(R.string.thursday), fontSize = 12.sp)
            Text(text = stringResource(R.string.friday), fontSize = 12.sp)
            Text(text = stringResource(R.string.saturday), fontSize = 12.sp)
            Text(text = stringResource(R.string.sunday), fontSize = 12.sp)
        }
    }
}

