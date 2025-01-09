package com.example.gymappdemo.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.GymSession
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    private val _currentSessionId = MutableStateFlow<Int?>(null)
    val currentSessionId: StateFlow<Int?> = _currentSessionId.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun startNewWorkout(
        userId: Int,
        onSessionCreated: (GymSession) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (_isWorkoutActive.value) {
                    onError("Έχει ήδη ενεργή προπόνηση.")
                    return@launch
                }

                // Δημιουργία τρέχουσας ημερομηνίας σε ISO 8601 format
                val date = LocalDate.now().toString()

                // Δημιουργία νέου GymSession
                val newSession = GymSession(
                    id = 0, // Το id δημιουργείται αυτόματα από τη βάση
                    userId = userId,
                    date = date,
                    notes = null,
                    duration = 0
                )

                // Εισαγωγή του GymSession στη βάση
                val sessionId = repository.insertSession(newSession).toInt()
                Log.d("HomeViewModel", "New GymSession inserted with ID: $sessionId")

                // Ανάκτηση του GymSession από τη βάση
                val createdSession = repository.getSessionById(sessionId)
                if (createdSession != null) {
                    Log.d("HomeViewModel", "GymSession retrieved: $createdSession")
                    _isWorkoutActive.value = true
                    _currentSessionId.value = sessionId
                    onSessionCreated(createdSession)
                } else {
                    val errorMessage = "Failed to retrieve GymSession with ID: $sessionId"
                    Log.e("HomeViewModel", errorMessage)
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Error occurred while starting new workout: ${e.message}"
                Log.e("HomeViewModel", errorMessage)
                onError(errorMessage)
            }
        }
    }

    fun terminateWorkout(sessionId: Int, duration: Int) {
        viewModelScope.launch {
            try {
                // Φόρτωση του GymSession
                val session = repository.getSessionById(sessionId)
                if (session != null) {
                    // Ενημέρωση του GymSession με τη διάρκεια
                    val updatedSession = session.copy(duration = duration)
                    repository.updateSession(updatedSession)
                    _isWorkoutActive.value = false
                    _currentSessionId.value = null
                    Log.d("HomeViewModel", "Workout terminated: $updatedSession")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error terminating workout: ${e.message}")
            }
        }
    }
}