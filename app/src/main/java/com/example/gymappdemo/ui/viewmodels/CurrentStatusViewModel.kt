package com.example.gymappdemo.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.data.entities.Set
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentStatusViewModel(
    private val workoutRepository: WorkoutRepository,
) : ViewModel() {

    private val _currentSessionId = MutableStateFlow<Int?>(null)
    val currentSessionId: StateFlow<Int?> = _currentSessionId.asStateFlow()

    private val _currentExercises = MutableStateFlow<List<ExerciseWithSets>>(emptyList())
    val currentExercises: StateFlow<List<ExerciseWithSets>> = _currentExercises.asStateFlow()

    private val _timerState = MutableStateFlow(0) // Δευτερόλεπτα
    val timerState: StateFlow<Int> = _timerState.asStateFlow()

    private val _caloriesState = MutableStateFlow(0)
    val caloriesState: StateFlow<Int> = _caloriesState.asStateFlow()

    private var isTimerRunning = false
    private var timerJob: Job? = null

    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    fun loadExercises(sessionId: Int) {
        viewModelScope.launch {
            try {
                val exercisesWithSets = workoutRepository.getExercisesWithSets(sessionId)
                Log.d("CurrentStatusViewModel", "Exercises with sets for session $sessionId: $exercisesWithSets")
                _currentExercises.value = exercisesWithSets
            } catch (e: Exception) {
                Log.e("CurrentStatusViewModel", "Error loading exercises: ${e.message}")
            }
        }
    }

    fun startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            timerJob = viewModelScope.launch {
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
        timerJob?.cancel()
    }

    fun resetTimer() {
        stopTimer()
        _timerState.value = 0
        _caloriesState.value = 0
    }

    fun setSessionId(sessionId: Int) {
        _currentSessionId.value = sessionId
        loadExercises(sessionId)
    }

    fun addSetToExercise(sessionExerciseId: Int, repetitions: Int, weight: Double, context: Context) {
        val language = context.resources.configuration.locales[0].language
        viewModelScope.launch {
            try {
                val newSet = Set(
                    id = 0,
                    sessionExerciseId = sessionExerciseId,
                    reps = repetitions,
                    weight = weight,
                    notes = ""
                )
                workoutRepository.insertSet(newSet)

                // Ενημέρωση της λίστας ασκήσεων
                loadExercises(_currentSessionId.value!!)
            } catch (e: Exception) {
                if (language == "el") {
                    _errorState.value = "Αποτυχία προσθήκης set: ${e.message}"
                } else
                    _errorState.value = "Failed to add set: ${e.message}"

            }
        }
    }

    fun removeSetFromExercise(setId: Int) {
        viewModelScope.launch {
            try {
                val sessionExerciseId = workoutRepository.getSessionExerciseIdBySetId(setId)
                workoutRepository.deleteSet(setId)
                _currentExercises.value = workoutRepository.getExercisesWithSets(_currentSessionId.value!!)
            } catch (e: Exception) {
                Log.e("CurrentStatusViewModel", "Error removing set: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun deleteExercise(sessionExerciseId: Int, language: String) {
        viewModelScope.launch {
            try {
                // Διαγραφή όλων των sets που σχετίζονται με την άσκηση
                val sets = workoutRepository.getSetsForExercise(sessionExerciseId)
                sets.forEach { set ->
                    workoutRepository.deleteSet(set.id)
                }
                // Διαγραφή της άσκησης
                workoutRepository.deleteSessionExercise(sessionExerciseId)
                Log.d("CurrentStatusViewModel", "Exercise $sessionExerciseId deleted successfully.")
                // Ενημέρωση της λίστας ασκήσεων
                loadExercises(_currentSessionId.value!!)
            } catch (e: Exception) {
                Log.e("CurrentStatusViewModel", "Error deleting exercise: ${e.message}")
                if (language == "el") {
                    _errorState.value = "Αποτυχία διαγραφής άσκησης: ${e.message}"
                } else
                    _errorState.value = "Failed to delete exercise: ${e.message}"
            }
        }
    }


    fun clearError() {
        _errorState.value = null
    }

    fun updateSet(updatedSet: Set) {
        viewModelScope.launch {
            try {
                workoutRepository.updateSet(updatedSet)
                loadExercises(_currentSessionId.value!!)
            } catch (e: Exception) {
                Log.e("CurrentStatusViewModel", "Error updating set: ${e.message}")
            }
        }
    }
}


