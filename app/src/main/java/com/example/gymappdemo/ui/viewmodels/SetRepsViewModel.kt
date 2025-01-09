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

    // SnackbarHostState για εμφάνιση μηνυμάτων
    val snackbarHostState = SnackbarHostState()

    fun loadSets(sessionExerciseId: Int) {
        viewModelScope.launch {
            try {
                // Φόρτωση των sets από τη βάση δεδομένων
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

    fun removeTemporarySet(setId: Int) {
        val updatedSets = temporarySets.value.toMutableList() // Δημιουργούμε αντιγραφή της λίστας
        updatedSets.removeAll { it.id == setId } // Αφαιρούμε το στοιχείο με το συγκεκριμένο ID
        _temporarySets.value = updatedSets // Ενημερώνουμε το StateFlow
    }

    fun saveSetsToDb(sessionExerciseId: Int) {
        viewModelScope.launch {
            try {
                _temporarySets.value.forEach { set ->
                    repository.addSet(set.copy(sessionExerciseId = sessionExerciseId))
                }
                _temporarySets.value = emptyList() // Καθαρισμός της λίστας μετά την αποθήκευση
                snackbarHostState.showSnackbar("Sets saved successfully!")
            } catch (e: Exception) {
                Log.e("SetRepsViewModel", "Error saving sets: ${e.message}")
                snackbarHostState.showSnackbar("Error saving sets: ${e.message}")
            }
        }
    }
}