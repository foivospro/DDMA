package com.example.gymappdemo.ui.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.SessionExercise
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.gymappdemo.data.entities.Set


class SetRepsViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _sets = MutableStateFlow<List<Set>>(emptyList())
    val sets: StateFlow<List<Set>> = _sets

    private val _temporarySets = MutableStateFlow<List<Set>>(emptyList())
    val temporarySets: StateFlow<List<Set>> = _temporarySets

    private val _exerciseName = MutableStateFlow<String>("Άσκηση")
    val exerciseName: StateFlow<String> = _exerciseName

    private val _exerciseImageUrl = MutableStateFlow<String>("")
    val exerciseImageUrl: StateFlow<String> = _exerciseImageUrl

    val snackbarHostState = SnackbarHostState()

    fun loadSets(sessionExerciseId: Int) {
        viewModelScope.launch {
            try {
                val sets = repository.getSetsForExercise(sessionExerciseId)
                _temporarySets.value = sets
            } catch (e: Exception) {
                Log.e("SetRepsViewModel", "Error loading sets: ${e.message}")
                snackbarHostState.showSnackbar("Error loading sets: ${e.message}")
            }
        }
    }

    fun loadExerciseDetails(exerciseId: Int) {
        viewModelScope.launch {
            try {
                val exercise = repository.getExerciseById(exerciseId)
                if (exercise != null) {
                    _exerciseName.value = exercise.name
                    _exerciseImageUrl.value = exercise.icon
                } else {
                    Log.e("SetRepsViewModel", "Άσκηση δεν βρέθηκε με ID: $exerciseId")
                    snackbarHostState.showSnackbar("Άσκηση δεν βρέθηκε.")
                }
            } catch (e: Exception) {
                Log.e("SetRepsViewModel", "Σφάλμα φόρτωσης λεπτομερειών άσκησης: ${e.message}")
                snackbarHostState.showSnackbar("Σφάλμα φόρτωσης λεπτομερειών άσκησης.")
            }
        }
    }

    fun addTemporarySet(dummySessionExerciseId: Int) {
        val newSet = Set(
            id = (temporarySets.value.maxOfOrNull { it.id } ?: 0) + 1,
            sessionExerciseId = dummySessionExerciseId, // Dummy ID
            reps = 1,
            weight = 0.0,
            notes = ""
        )
        _temporarySets.value = temporarySets.value + newSet
    }

    fun updateTemporarySet(id: Int, reps: Int, weight: Double) {
        _temporarySets.value = _temporarySets.value.map { set ->
            if (set.id == id) set.copy(reps = reps, weight = weight) else set
        }
    }

    fun removeTemporarySet(setId: Int) {
        val updatedSets = temporarySets.value.toMutableList()
        updatedSets.removeAll { it.id == setId }
        _temporarySets.value = updatedSets
    }

    fun saveSetsToDb(sessionExerciseId: Int) {
        Log.d("SetRepsViewModel", "saveSetsToDb called with sessionExerciseId: $sessionExerciseId")
        Log.d("SetRepsViewModel", "_temporarySets contains: ${_temporarySets.value}")
        viewModelScope.launch {
            try {
                // Ενημέρωση του sessionExerciseId και μηδενισμός του id
                val updatedSets = _temporarySets.value.map { set ->
                    set.copy(id = 0, sessionExerciseId = sessionExerciseId) // Μηδενισμός του id
                }
                Log.d("SetRepsViewModel", "Updated sets: $updatedSets")

                // Αποθήκευση στη βάση δεδομένων
                updatedSets.forEach { set ->
                    Log.d("SetRepsViewModel", "Saving set: $set with sessionExerciseId: $sessionExerciseId")
                    repository.addSet(set)
                    Log.d("SetRepsViewModel", "Set saved successfully for sessionExerciseId: $sessionExerciseId")
                }

                // Καθαρισμός της λίστας temporary sets
                _temporarySets.value = emptyList()

                Log.d("SetRepsViewModel", "All sets saved successfully.")
            } catch (e: Exception) {
                Log.e("SetRepsViewModel", "Error saving sets: ${e.message}")
            }
        }
    }


    fun addExerciseToSession(
        sessionId: Int,
        exerciseId: Int,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val sessionExercise = SessionExercise(
                    sessionId = sessionId,
                    exerciseId = exerciseId,
                    order = getNextExerciseOrder(sessionId)
                )
                val sessionExerciseId = repository.insertSessionExercise(sessionExercise).toInt()
                onSuccess(sessionExerciseId)
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun getNextExerciseOrder(sessionId: Int): Int {
        val sessionExercises = repository.getSessionExercisesBySessionId(sessionId)
        return (sessionExercises.maxOfOrNull { it.order } ?: 0) + 1
    }
}