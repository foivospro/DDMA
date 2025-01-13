package com.example.gymappdemo.ui.viewmodels

import android.os.Build
import android.util.Log
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
import java.time.LocalDate

class HomeViewModel(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    private val _currentSessionId = MutableStateFlow<Int?>(null)
    val currentSessionId: StateFlow<Int?> = _currentSessionId.asStateFlow()


    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> get() = _username


    private val _userSessions = MutableStateFlow<List<GymSession>>(emptyList())
    val userSessions: StateFlow<List<GymSession>> = _userSessions.asStateFlow()


    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            fetchUserData()
            val activeSession = _userId.value?.let { workoutRepository.getActiveSession(it) }
            if (activeSession != null) {
                _isWorkoutActive.value = true
                _currentSessionId.value = activeSession.id
            }
        }
    }

    private suspend fun fetchUserData() {

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


        _username.value = user?.name ?: "Guest"
        _userId.value = user?.id

        // Αν ο χρήστης δεν είναι "Guest", ανακτά τις συνεδρίες του
        if (user != null) {
            val sessions = withContext(Dispatchers.IO) {
                workoutRepository.getSessionsForUser(user.id)
            }
            _userSessions.value = sessions
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startNewWorkout(
        onSessionCreated: (GymSession) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (_isWorkoutActive.value) {
                    onError("Έχει ήδη ενεργή προπόνηση.")
                    return@launch
                }
                val currentUserId = _userId.value
                if (currentUserId == null) {
                    onError("Δεν βρέθηκε χρήστης.")
                    return@launch
                }
                val date = LocalDate.now().toString()

                val newSession = GymSession(
                    id = 0,
                    userId = currentUserId,
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


                    _userSessions.value = listOf(createdSession) + _userSessions.value
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


                    _userSessions.value = _userSessions.value.map {
                        if (it.id == sessionId) updatedSession else it
                    }
                }
            } catch (e: Exception) {

                Log.e("HomeViewModel", "Error terminating workout: ${e.message}")
            }
        }
    }
}
