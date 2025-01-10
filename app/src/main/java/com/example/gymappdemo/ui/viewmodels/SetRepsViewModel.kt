package com.example.gymappdemo.ui.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun addTemporarySet(sessionExerciseId: Int) {
        val newSet = Set(
            id = (temporarySets.value.maxOfOrNull { it.id } ?: 0) + 1,
            sessionExerciseId = sessionExerciseId,
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
                _temporarySets.value.forEach { set ->
                    Log.d("SetRepsViewModel", "Saving set: $set with sessionExerciseId: $sessionExerciseId")
                    repository.addSet(set.copy(sessionExerciseId = sessionExerciseId))
                    Log.d("SetRepsViewModel", "Set saved successfully for sessionExerciseId: $sessionExerciseId")
                }
                _temporarySets.value = emptyList()

                Log.d("SetRepsViewModel", "All sets saved successfully.")
            } catch (e: Exception) {
                Log.e("SetRepsViewModel", "Error saving sets: ${e.message}")

            }
        }
    }
}