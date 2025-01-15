package com.example.gymappdemo.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmdk.gymapp.data.network.NewsApiService
import com.pmdk.gymapp.data.preferences.ThemePreferences
import com.pmdk.gymapp.data.repositories.UserRepository
import com.pmdk.gymapp.data.repositories.WorkoutRepository
import com.pmdk.gymapp.ui.viewmodels.CurrentStatusViewModel
import com.pmdk.gymapp.ui.viewmodels.ExercisePickerViewModel
import com.pmdk.gymapp.ui.viewmodels.HomeViewModel
import com.pmdk.gymapp.ui.viewmodels.LoginViewModel
import com.pmdk.gymapp.ui.viewmodels.MyProfileViewModel
import com.pmdk.gymapp.ui.viewmodels.NewsViewModel
import com.pmdk.gymapp.ui.viewmodels.RegisterViewModel
import com.pmdk.gymapp.ui.viewmodels.SetRepsViewModel

class AppViewModelFactory(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository,
    private val service: NewsApiService,
    private val themePreferences: ThemePreferences
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
                val profileViewModel = MyProfileViewModel(userRepository, themePreferences)
                HomeViewModel(workoutRepository, userRepository, profileViewModel) as T
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

            modelClass.isAssignableFrom(MyProfileViewModel::class.java) -> {
                MyProfileViewModel(userRepository, themePreferences) as T
            }
            (modelClass.isAssignableFrom(NewsViewModel::class.java)) -> {
                NewsViewModel(service) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
