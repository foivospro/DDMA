package com.example.gymappdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.Exercise
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

    private val _currentExercises = MutableStateFlow(emptyList<Exercise>())

    val timerState: StateFlow<Int> = _timerState
    val caloriesState: StateFlow<Int> = _caloriesState
    val currentExercises: StateFlow<List<Exercise>> = _currentExercises

    private var isTimerRunning = false

    fun startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            viewModelScope.launch {
                while (isTimerRunning) {
                    delay(1000L) // 1 second delay
                    _timerState.value += 1
                    _caloriesState.value = _timerState.value / 6
                }
            }
        }
    }

    fun stopTimer() {
        isTimerRunning = false
    }
}
