package com.example.gymappdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrentStatusViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _timerState = MutableStateFlow(0) // Tracks elapsed time in seconds
    private val _caloriesState = MutableStateFlow(0)
    private val _currentExercises = MutableStateFlow<List<ExerciseWithSets>>(emptyList())

    val timerState: StateFlow<Int> = _timerState
    val caloriesState: StateFlow<Int> = _caloriesState
    val currentExercises: StateFlow<List<ExerciseWithSets>> = _currentExercises

    private var isTimerRunning = false

    fun loadExercises(sessionId: Int) {
        viewModelScope.launch {
            val exercisesWithSets = workoutRepository.getExercisesWithSets(sessionId)
            _currentExercises.value = exercisesWithSets
        }
    }

    fun startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            viewModelScope.launch {
                while (isTimerRunning) {
                    delay(1000L)
                    _timerState.value += 1
                    _caloriesState.value = _timerState.value / 6
                }
            }
        }
    }

    fun stopTimer() {
        isTimerRunning = false
    }


    fun removeSetAndSessionExercise(setId: Int) {
        viewModelScope.launch {
            try {
                // Βρίσκουμε το SessionExercise που συνδέεται με το Set
                val sessionExerciseId = workoutRepository.getSessionExerciseIdBySetId(setId)

                // Διαγραφή του Set
                workoutRepository.deleteSet(setId)

                // Αν δεν υπάρχουν άλλα Sets, διαγράφουμε και το SessionExercise
                val remainingSets = workoutRepository.getSetsBySessionExerciseId(sessionExerciseId)
                if (remainingSets.isEmpty()) {
                    workoutRepository.deleteSessionExercise(sessionExerciseId)
                }

                // Φόρτωση των ασκήσεων ξανά
                _currentExercises.value = workoutRepository.getExercisesWithSets(sessionExerciseId)
            } catch (e: Exception) {
                // Διαχείριση σφαλμάτων
                e.printStackTrace()
            }
        }
    }
}

