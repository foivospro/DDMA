package com.example.gymappdemo.ui.viewmodels
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.data.entities.SessionExercise
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExercisePickerViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

    // StateFlow to hold the list of exercises
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    private val _selectedExercises = MutableStateFlow<Set<Exercise>>(emptySet())
    val selectedExercises: StateFlow<Set<Exercise>> = _selectedExercises

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        fetchExercises()
    }


    private fun fetchExercises() {
        viewModelScope.launch {
            try {
                val allExercises = workoutRepository.getAllExercises()
                _exercises.value = allExercises
            } catch (e: Exception) {
                Log.e("ExercisePickerViewModel", "Error fetching exercises: ${e.message}")
                _errorMessage.value = "Error fetching exercises: ${e.message}"
            }
        }
    }

    fun toggleExerciseSelection(exercise: Exercise) {
        _selectedExercises.value = if (_selectedExercises.value.contains(exercise)) {
            _selectedExercises.value - exercise
        } else {
            _selectedExercises.value + exercise
        }
    }

    // Δημόσια συνάρτηση για την προσθήκη ασκήσεων στη συνεδρία
    fun addExerciseToSession(
        sessionId: Int,
        exercise: Exercise,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Υπολογισμός της σειράς (order) για το νέο SessionExercise
                val order = getNextExerciseOrder(sessionId)

                // Δημιουργία του SessionExercise
                val sessionExercise = SessionExercise(
                    sessionId = sessionId,
                    exerciseId = exercise.id,
                    order = order
                )

                // Εισαγωγή στη βάση δεδομένων και λήψη του ID
                val sessionExerciseId = workoutRepository.insertSessionExercise(sessionExercise)
                Log.d("ExercisePickerViewModel", "SessionExercise added: $sessionExercise with ID: $sessionExerciseId")

                // Επιστροφή του ID μέσω callback
                onSuccess(sessionExerciseId.toInt())
            } catch (e: Exception) {
                // Καταγραφή σφάλματος και επιστροφή μέσω callback
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

    // Συνάρτηση για την ενημέρωση του μηνύματος σφάλματος
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}