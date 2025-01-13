package com.example.gymappdemo.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymappdemo.data.network.NewsApiService
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import com.example.gymappdemo.ui.viewmodels.CurrentStatusViewModel
import com.example.gymappdemo.ui.viewmodels.ExercisePickerViewModel
import com.example.gymappdemo.ui.viewmodels.HomeViewModel
import com.example.gymappdemo.ui.viewmodels.LoginViewModel
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel
import com.example.gymappdemo.ui.viewmodels.NewsViewModel
import com.example.gymappdemo.ui.viewmodels.RegisterViewModel
import com.example.gymappdemo.ui.viewmodels.SetRepsViewModel

class AppViewModelFactory(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository,
    private val service: NewsApiService
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ExercisePickerViewModel::class.java) -> {
                ExercisePickerViewModel(workoutRepository) as T
            }

            modelClass.isAssignableFrom(CurrentStatusViewModel::class.java) -> {
                CurrentStatusViewModel(workoutRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(workoutRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(SetRepsViewModel::class.java) -> {
                SetRepsViewModel(workoutRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }

            (modelClass.isAssignableFrom(MyProfileViewModel::class.java)) -> {
                MyProfileViewModel(userRepository) as T
            }
            (modelClass.isAssignableFrom(NewsViewModel::class.java)) -> {
                NewsViewModel(service) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
