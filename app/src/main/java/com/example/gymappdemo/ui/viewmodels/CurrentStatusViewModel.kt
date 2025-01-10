package com.example.gymappdemo.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentStatusViewModel(
    private val workoutRepository: WorkoutRepository
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

    fun loadExercises(sessionId: Int) {
        viewModelScope.launch {
            val exercisesWithSets = workoutRepository.getExercisesWithSets(sessionId)
            _currentExercises.value = exercisesWithSets
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
        Log.d("CurrentViewModel", "SessionId updated to: $sessionId")
    }


    fun removeSetAndSessionExercise(setId: Int) {
        viewModelScope.launch {
            try {
                val sessionExerciseId = workoutRepository.getSessionExerciseIdBySetId(setId)
                workoutRepository.deleteSet(setId)
                val remainingSets = workoutRepository.getSetsBySessionExerciseId(sessionExerciseId)
                if (remainingSets.isEmpty()) {
                    workoutRepository.deleteSessionExercise(sessionExerciseId)
                }
                _currentExercises.value = workoutRepository.getExercisesWithSets(sessionExerciseId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

