package com.example.gymappdemo.ui.viewmodels

import android.util.Log
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

    fun loadSets(sessionExerciseId: Int) {
        viewModelScope.launch {
            // Φόρτωση των sets από τη βάση δεδομένων
            val sets = repository.getSetsForExercise(sessionExerciseId)
            _temporarySets.value = sets
        }
    }

    fun addTemporarySet(sessionExerciseId: Int) {
        val newSet = Set(
            id = (temporarySets.value.maxOfOrNull { it.id } ?: 0) + 1, // Εύρεση του μέγιστου ID και αύξηση κατά 1
            sessionExerciseId = sessionExerciseId,
            reps = 1, // Προεπιλογή επαναλήψεων
            weight = 0.0, // Προεπιλογή βάρους
            notes = "" // Προεπιλογή σημειώσεων
        )
        _temporarySets.value = temporarySets.value + newSet // Προσθήκη στη λίστα
    }

    fun updateTemporarySet(id: Int, reps: Int, weight: Double) {
        _temporarySets.value = _temporarySets.value.map { set ->
            if (set.id == id) set.copy(reps = reps, weight = weight) else set
        }
    }

    fun addNewSet(sessionExerciseId: Int) {
        viewModelScope.launch {
            Log.d("Debug", "addNewSet called with sessionExerciseId: $sessionExerciseId")

            try {
                val sessionExercise = repository.getSessionById(sessionExerciseId)
                if (sessionExercise != null) {
                    Log.d("Debug", "SessionExercise found: $sessionExercise")
                    val newSet = Set(
                        sessionExerciseId = sessionExerciseId,
                        reps = 8,
                        weight = 0.0,
                        notes = ""
                    )
                    repository.addSet(newSet)
                    Log.d("Debug", "New Set added: $newSet")
                } else {
                    Log.e("DatabaseError", "No SessionExercise found with id $sessionExerciseId")
                }
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error occurred: ${e.message}")
            }
        }
    }

    fun removeTemporarySet(setId: Int) {
        val updatedSets = temporarySets.value.toMutableList() // Δημιουργούμε αντιγραφή της λίστας
        updatedSets.removeAll { it.id == setId } // Αφαιρούμε το στοιχείο με το συγκεκριμένο ID
        _temporarySets.value = updatedSets // Ενημερώνουμε το StateFlow
    }

    fun updateWeight(setId: Int, newWeight: Double) {
        viewModelScope.launch {
            val updatedSet = _sets.value.first { it.id == setId }.copy(weight = newWeight)
            repository.updateSet(updatedSet)
            _sets.value = repository.getSetsForExercise(updatedSet.sessionExerciseId)
        }
    }

    fun updateReps(setId: Int, newReps: Int) {
        viewModelScope.launch {
            val updatedSet = _sets.value.first { it.id == setId }.copy(reps = newReps)
            repository.updateSet(updatedSet)
            _sets.value = repository.getSetsForExercise(updatedSet.sessionExerciseId)
        }
    }

    fun removeSet(setId: Int) {
        viewModelScope.launch {
            try {
                // Διαγραφή του set με το ID
                repository.deleteSet(setId)

                // Ενημέρωση της κατάστασης με τα υπόλοιπα sets
                val sessionExerciseId = repository.getSessionExerciseIdBySetId(setId)
                _sets.value = repository.getSetsForExercise(sessionExerciseId)
            } catch (e: Exception) {
                // Αντιμετώπιση σφαλμάτων
                e.printStackTrace()
            }
        }
    }

    fun saveSetsToDb(sessionExerciseId: Int) {
        viewModelScope.launch {
            _temporarySets.value.forEach { set ->
                repository.addSet(set.copy(sessionExerciseId = sessionExerciseId))
            }
            _temporarySets.value = emptyList() // Καθαρισμός της λίστας μετά την αποθήκευση
        }
    }
}