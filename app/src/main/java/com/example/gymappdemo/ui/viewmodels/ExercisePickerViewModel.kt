package com.example.gymappdemo.ui.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExercisePickerViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

    // StateFlow to hold the list of exercises
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    init {
        fetchAllExercises()
    }

    private fun fetchAllExercises() {
        viewModelScope.launch {
            val exerciseList = workoutRepository.getAllExercises()
            _exercises.value = exerciseList
        }
    }
    fun getIconResource(icon: String): Int {
        return when (icon) {
            "bench" -> com.example.gymappdemo.R.drawable.bench
            "biceps" -> com.example.gymappdemo.R.drawable.biceps
            "cycling" -> com.example.gymappdemo.R.drawable.cycling
            "deadlift" -> com.example.gymappdemo.R.drawable.deadlift
           "dips" -> com.example.gymappdemo.R.drawable.dips
            "pullups" -> com.example.gymappdemo.R.drawable.pullups
            "rdl" -> com.example.gymappdemo.R.drawable.rdl
            "rows" -> com.example.gymappdemo.R.drawable.rows
            "snatch" -> com.example.gymappdemo.R.drawable.snatch
            "squat" -> com.example.gymappdemo.R.drawable.squat
            "triceps" -> com.example.gymappdemo.R.drawable.triceps
            "triceps2" -> com.example.gymappdemo.R.drawable.triceps2
            else -> com.example.gymappdemo.R.drawable.weightlifter
        }
    }
}