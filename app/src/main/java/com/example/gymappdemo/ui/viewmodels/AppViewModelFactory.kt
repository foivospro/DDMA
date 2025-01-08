package com.example.gymappdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import com.example.gymappdemo.ui.viewmodels.ExercisePickerViewModel
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel

class AppViewModelFactory(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisePickerViewModel::class.java)) {
            return ExercisePickerViewModel(workoutRepository) as T
        }
        if (modelClass.isAssignableFrom(MyProfileViewModel::class.java)) {
            return MyProfileViewModel(userRepository) as T // Provide MyProfileViewModel with userRepository
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}