package com.example.gymappdemo.ui.viewmodels
import android.util.Log
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
        viewModelScope.launch {
            val exercisesList = workoutRepository.getAllExercises()
            Log.d("Debug", "Exercises loaded: ${exercisesList}")
            _exercises.value = exercisesList
        }
    }

    fun addExerciseToSession(
        sessionId: Int,
        exercise: Exercise,
        onSuccess: (Int) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Υπολογισμός της σειράς (order) για το νέο SessionExercise
                val order = getNextExerciseOrder(sessionId)

                // Δημιουργία του SessionExercise
                val sessionExercise = com.example.gymappdemo.data.entities.SessionExercise(
                    sessionId = sessionId,
                    exerciseId = exercise.id,
                    order = order
                )

                // Εισαγωγή στη βάση δεδομένων και λήψη του ID
                val sessionExerciseId = workoutRepository.insertSessionExercise(sessionExercise)
                Log.d("Debug", "SessionExercise added: $sessionExercise")

                // Επιστροφή του ID μέσω callback
                onSuccess(sessionExerciseId.toInt())
            } catch (e: Exception) {
                // Καταγραφή σφάλματος
                Log.e("DatabaseError", "Error adding exercise to session: ${e.message}")
            }
        }
    }

    private suspend fun getNextExerciseOrder(sessionId: Int): Int {
        val sessionExercises = workoutRepository.getSessionExercisesBySessionId(sessionId)
        return (sessionExercises.maxOfOrNull { it.order } ?: 0) + 1
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