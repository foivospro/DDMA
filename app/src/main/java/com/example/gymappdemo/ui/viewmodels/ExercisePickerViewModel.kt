package com.example.gymappdemo.ui.viewmodels
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.data.entities.SessionExercise
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExercisePickerViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

    val exercises: StateFlow<List<Exercise>> = workoutRepository.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedExercises = MutableStateFlow<Set<Exercise>>(emptySet())
    val selectedExercises: StateFlow<Set<Exercise>> = _selectedExercises

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun addExerciseToSession(
        sessionId: Int,
        exercise: Exercise,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val order = getNextExerciseOrder(sessionId)
                val sessionExercise = SessionExercise(
                    sessionId = sessionId,
                    exerciseId = exercise.id,
                    order = order
                )

                val sessionExerciseId = workoutRepository.insertSessionExercise(sessionExercise)
                Log.d("ExercisePickerViewModel", "SessionExercise added: $sessionExercise with ID: $sessionExerciseId")

                onSuccess(sessionExerciseId.toInt())
            } catch (e: Exception) {
                Log.e("ExercisePickerViewModel", "Error adding exercise to session: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun getNextExerciseOrder(sessionId: Int): Int {
        val sessionExercises = workoutRepository.getSessionExercisesBySessionId(sessionId)
        return (sessionExercises.maxOfOrNull { it.order } ?: 0) + 1
    }

    fun getIconResource(icon: String): Int {
        return when (icon.lowercase()) {
            "bench" -> R.drawable.bench
            "biceps" -> R.drawable.biceps
            "cycling" -> R.drawable.cycling
            "deadlift" -> R.drawable.deadlift
            "dips" -> R.drawable.dips
            "pullups" -> R.drawable.pullups
            "rdl" -> R.drawable.rdl
            "rows" -> R.drawable.rows
            "snatch" -> R.drawable.snatch
            "squat" -> R.drawable.squat
            "triceps" -> R.drawable.triceps
            "triceps2" -> R.drawable.triceps2
            else -> R.drawable.weightlifter
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}