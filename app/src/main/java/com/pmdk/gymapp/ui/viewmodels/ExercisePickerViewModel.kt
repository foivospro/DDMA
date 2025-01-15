package com.pmdk.gymapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdk.gymapp.data.entities.Exercise
import com.pmdk.gymapp.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class ExercisePickerViewModel(workoutRepository: WorkoutRepository) : ViewModel() {

    val exercises: StateFlow<List<Exercise>> = workoutRepository.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedExercises = MutableStateFlow<Set<Exercise>>(emptySet())
    val selectedExercises: StateFlow<Set<Exercise>> = _selectedExercises

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}