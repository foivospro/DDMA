package com.example.gymappdemo.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.GymSession
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WorkoutRepository) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun startNewWorkout(userId: Int, onSessionCreated: (GymSession) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Δημιουργία τρέχουσας ημερομηνίας σε ISO 8601 format
                val date = java.time.LocalDate.now().toString()

                // Δημιουργία νέου GymSession
                val newSession = GymSession(
                    id = 0, // To id δημιουργείται αυτόματα από τη βάση
                    userId = userId, // Ορίζεται το ID του χρήστη
                    date = date,
                    notes = null,
                    duration = 0
                )

                // Εισαγωγή του GymSession στη βάση
                val sessionId = repository.insertSession(newSession)
                Log.d("Debug", "New GymSession inserted with ID: $sessionId")

                // Ανάκτηση του GymSession από τη βάση
                val createdSession = repository.getSessionById(sessionId.toInt())
                if (createdSession != null) {
                    Log.d("Debug", "GymSession retrieved: $createdSession")
                    onSessionCreated(createdSession)
                } else {
                    val errorMessage = "Failed to retrieve GymSession with ID: $sessionId"
                    Log.e("DatabaseError", errorMessage)
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Error occurred while starting new workout: ${e.message}"
                Log.e("DatabaseError", errorMessage)
                onError(errorMessage)
            }
        }
    }
}
