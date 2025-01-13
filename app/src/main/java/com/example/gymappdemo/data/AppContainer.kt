package com.example.gymappdemo.data

import android.content.Context
import com.example.gymappdemo.data.database.AppDatabase
import com.example.gymappdemo.data.network.NewsApiService
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val userRepository: UserRepository
    val workoutRepository: WorkoutRepository
    val retrofitService: NewsApiService
}

class AppContainerImpl(private val context: Context) : AppContainer {

    private  val BASE_URL = "https://newsapi.org/v2/"
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(
            contentType = "application/json".toMediaType())
        ).build()
    override val retrofitService : NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java) }

    override val userRepository: UserRepository by lazy {
        UserRepository.getInstance(
        AppDatabase.getInstance(context).userDao(),
        context
        )
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