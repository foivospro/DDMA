package com.example.gymappdemo.data

import android.content.Context
import com.example.gymappdemo.data.database.AppDatabase
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository

interface AppContainer {
    val userRepository: UserRepository
    val workoutRepository: WorkoutRepository
}

class AppContainerImpl(private val context: Context) : AppContainer {
    override val userRepository: UserRepository by lazy {
        UserRepository(AppDatabase.getInstance(context).userDao())
    }
    override val workoutRepository: WorkoutRepository by lazy {
        WorkoutRepository.getInstance(
            AppDatabase.getInstance(context).gymSessionDao(),
            AppDatabase.getInstance(context).sessionExerciseDao(),
            AppDatabase.getInstance(context).exerciseDao(),
            AppDatabase.getInstance(context).setDao()
        )
    }
}