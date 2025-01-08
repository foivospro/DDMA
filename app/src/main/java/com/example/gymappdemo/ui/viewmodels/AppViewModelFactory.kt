package com.example.gymappdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymappdemo.data.repositories.WorkoutRepository
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.ui.viewmodels.ExercisePickerViewModel

class AppViewModelFactory(
    private val workoutRepository: WorkoutRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisePickerViewModel::class.java)) {
            return ExercisePickerViewModel(workoutRepository) as T
        }
        else if (modelClass.isAssignableFrom(CurrentStatusViewModel::class.java)) {
            return CurrentStatusViewModel(workoutRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}