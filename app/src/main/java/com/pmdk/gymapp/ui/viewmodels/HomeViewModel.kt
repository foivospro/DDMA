package com.pmdk.gymapp.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdk.gymapp.data.entities.GymSession
import com.pmdk.gymapp.data.repositories.UserRepository
import com.pmdk.gymapp.data.repositories.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeViewModel(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository,
    private val profileViewModel: MyProfileViewModel
) : ViewModel() {
    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int?> = _userId.asStateFlow()


    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    private val _currentSessionId = MutableStateFlow<Int?>(null)
    val currentSessionId: StateFlow<Int?> = _currentSessionId.asStateFlow()


    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> get() = _username


    private val _userSessions = MutableStateFlow<List<GymSession>>(emptyList())
    val userSessions: StateFlow<List<GymSession>> = _userSessions.asStateFlow()

    init {
        observeLogout()
        updateViewModel(asGuest = false)

    }
    private fun observeLogout() {
        viewModelScope.launch {
            profileViewModel.logoutEvent.collect { isLoggedOut ->
                if (isLoggedOut) {
                    updateViewModel(asGuest = true) // Reset HomeViewModel state
                }
            }
        }
    }

    fun updateViewModel(asGuest: Boolean) {
        viewModelScope.launch {
            if (!asGuest) {
                fetchUserData()
                _userId.value = fetchUserId()

                // Πάρε τον userId που μόλις έβαλες στο _userId.value
                val localUserId = _userId.value

                // Κάλεσε το getActiveSession περνώντας userId
                val activeSession = workoutRepository.getActiveSession(localUserId)
                if (activeSession != null) {
                    _isWorkoutActive.value = true
                    _currentSessionId.value = activeSession.id
                }
            } else{
                terminateWorkout(_currentSessionId.value ?: 0, 0, 0)
                _username.value = "Guest"
                _userId.value = 1
                _isWorkoutActive.value = false
                _currentSessionId.value = null
                val sessions = withContext(Dispatchers.IO) {
                    workoutRepository.getSessionsForUser(_userId.value)
                }
                _userSessions.value = sessions
            }
        }
    }


    private suspend fun fetchUserData() {

        val email = withContext(Dispatchers.IO) {
            userRepository.getLoggedInUserEmail()
        }
        Log.d("HomeViewModel", "Email: $email")
        val user =
            if (email != "Guest") {
            withContext(Dispatchers.IO) {
                userRepository.getUserByEmail(email!!)
            }
        } else {
            null
        }


        _username.value = user?.name ?: "Guest"
        _userId.value = user?.id ?:0

        // Αν ο χρήστης δεν είναι "Guest", ανακτά τις συνεδρίες του
            val sessions = withContext(Dispatchers.IO) {
                workoutRepository.getSessionsForUser(_userId.value)
            }
            _userSessions.value = sessions

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
        return  user?.id ?: 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startNewWorkout(
        userId: Int,
        onSessionCreated: (GymSession) -> Unit,
        onError: (String) -> Unit,
        language: String
    ) {
        viewModelScope.launch {
            try {
                if (_isWorkoutActive.value) {
                    if (language == "el") {
                        onError("Έχεις ήδη ενεργή προπόνηση.")
                    } else {
                        onError("There is already an active workout.")
                    }
                    return@launch
                }
                val currentUserId = _userId.value
                val date = LocalDate.now().toString()
                Log.d("HomeViewModel", "Current user ID: $currentUserId")
                val newSession = GymSession(
                    id = 0,
                    userId = currentUserId,
                    date = date,
                    notes = null,
                    duration = 0,
                    caloriesBurned = 0

                )

                val sessionId = workoutRepository.insertSession(newSession).toInt()
                val createdSession = workoutRepository.getSessionById(sessionId)
                if (createdSession != null) {
                    _isWorkoutActive.value = true
                    _currentSessionId.value = sessionId
                    onSessionCreated(createdSession)


                    _userSessions.value = listOf(createdSession) + _userSessions.value
                } else {
                    if (language == "el") {
                        onError("Αποτυχία ανάκτησης προπόνησης με ID: $sessionId")
                    } else {
                        onError("Failed to retrieve workout with ID: $sessionId")
                    }
                }
            } catch (e: Exception) {
                if (language == "el") {
                    onError("Σφάλμα κατά την έναρξη προπόνησης: ${e.message}")
                } else {
                    onError("Error starting workout: ${e.message}")
                }
            }
        }
    }

    fun terminateWorkout(sessionId: Int, duration: Int, caloriesBurned: Int) {
        viewModelScope.launch {
            try {
                val session = workoutRepository.getSessionById(sessionId)
                if (session != null) {
                    val updatedSession = session.copy(duration = duration,caloriesBurned = caloriesBurned)
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
