package com.example.gymappdemo.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.GymSession
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> = _userId.asStateFlow()

    // Κατάσταση Προπόνησης
    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    private val _currentSessionId = MutableStateFlow<Int?>(null)
    val currentSessionId: StateFlow<Int?> = _currentSessionId.asStateFlow()

    // Κατάσταση Username
    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> get() = _username

    init {
        updateViewModel()
    }
    fun updateViewModel() {
        viewModelScope.launch {
            fetchUsername()
            _userId.value = fetchUserId()
            val activeSession = workoutRepository.getActiveSession()
            if (activeSession != null) {
                _isWorkoutActive.value = true
                _currentSessionId.value = activeSession.id
            }
        }
    }

    private suspend fun fetchUsername() {
        // Χρήση του Dispatcher.IO για λειτουργίες βάσης δεδομένων
        val email = withContext(Dispatchers.IO) {
            userRepository.getLoggedInUserEmail()
        }

        val user = if (email != "Guest") {
            withContext(Dispatchers.IO) {
                userRepository.getUserByEmail(email!!)
            }
        } else {
            null
        }

        // Ενημέρωση της κατάστασης στο κύριο νήμα
        _username.value = user?.name ?: "Guest"
    }

     private suspend fun fetchUserId(): Int {
        // Χρήση του Dispatcher.IO για λειτουργίες βάσης δεδομένων
        val email = withContext(Dispatchers.IO) {
            userRepository.getLoggedInUserEmail()
        }

        val user = if (email != "Guest") {
            withContext(Dispatchers.IO) {
                userRepository.getUserByEmail(email!!)
            }
        } else {
            null
        }

        // Ενημέρωση της κατάστασης στο κύριο νήμα
        return  user?.id ?: 0
    }

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
                val date = java.time.LocalDate.now().toString()

                val newSession = GymSession(
                    id = 0, // Το ID δημιουργείται αυτόματα από τη βάση δεδομένων
                    userId = userId,
                    date = date,
                    notes = null,
                    duration = 0
                )

                val sessionId = workoutRepository.insertSession(newSession).toInt()
                val createdSession = workoutRepository.getSessionById(sessionId)
                if (createdSession != null) {
                    _isWorkoutActive.value = true
                    _currentSessionId.value = sessionId
                    onSessionCreated(createdSession)
                } else {
                    onError("Αποτυχία ανάκτησης προπόνησης με ID: $sessionId")
                }
            } catch (e: Exception) {
                onError("Σφάλμα κατά την έναρξη προπόνησης: ${e.message}")
            }
        }
    }

    fun terminateWorkout(sessionId: Int, duration: Int) {
        viewModelScope.launch {
            try {
                val session = workoutRepository.getSessionById(sessionId)
                if (session != null) {
                    val updatedSession = session.copy(duration = duration)
                    workoutRepository.updateSession(updatedSession)
                    _isWorkoutActive.value = false
                    _currentSessionId.value = null
                }
            } catch (e: Exception) {
                // Διαχείριση σφάλματος (π.χ., εμφάνιση Snackbar)
            }
        }
    }
}