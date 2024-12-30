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
            "ic_bench_press" -> com.example.gymappdemo.R.drawable.weightlifter
            else -> com.example.gymappdemo.R.drawable.weightlifter
        }
    }
}