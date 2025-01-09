package com.example.gymappdemo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymappdemo.data.entities.Set

@Dao
interface SetDao {

    // Εισαγωγή ενός νέου set
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(set: Set): Long

    // Ενημέρωση ενός υπάρχοντος set
    @Update
    suspend fun update(set: Set)

    // Λήψη ενός set με βάση το ID
    @Query("SELECT * FROM sets WHERE id = :setId")
    suspend fun getSet(setId: Int): Set

    // Λήψη όλων των sets που σχετίζονται με ένα συγκεκριμένο sessionExerciseId
    @Query("SELECT * FROM sets WHERE sessionExerciseId = :sessionExerciseId ORDER BY id ASC")
    suspend fun getSetsForExercise(sessionExerciseId: Int): List<Set>

    // Διαγραφή όλων των sets που σχετίζονται με ένα συγκεκριμένο sessionExerciseId
    @Query("DELETE FROM sets WHERE sessionExerciseId = :sessionExerciseId")
    suspend fun deleteSetsForExercise(sessionExerciseId: Int)

    // Λήψη όλων των sets με βάση το sessionExerciseId
    @Query("SELECT * FROM sets WHERE sessionExerciseId = :sessionExerciseId")
    suspend fun getSetsBySessionExerciseId(sessionExerciseId: Int): List<Set>

    // Λήψη του sessionExerciseId που σχετίζεται με ένα set
    @Query("SELECT sessionExerciseId FROM sets WHERE id = :setId")
    suspend fun getSessionExerciseIdBySetId(setId: Int): Int

    // Διαγραφή ενός set με βάση το ID του
    @Query("DELETE FROM sets WHERE id = :setId")
    suspend fun delete(setId: Int)
}