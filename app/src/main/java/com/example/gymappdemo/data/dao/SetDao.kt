package com.example.gymappdemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymappdemo.data.entities.Set

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set: Set)

    @Update
    suspend fun update(set: Set)

    @Query("SELECT * FROM sets WHERE id = :setId")
    suspend fun getSet(setId: Int): Set

    @Query("SELECT * FROM sets WHERE sessionExerciseId = :sessionExerciseId ORDER BY id ASC")
    suspend fun getSetsForExercise(sessionExerciseId: Int): List<Set>

    @Query("DELETE FROM sets WHERE sessionExerciseId = :sessionExerciseId")
    suspend fun deleteSetsForExercise(sessionExerciseId: Int)

    @Query("SELECT * FROM sets WHERE sessionExerciseId = :sessionExerciseId")
    suspend fun getSetsBySessionExerciseId(sessionExerciseId: Int): List<Set>

    @Query("SELECT sessionExerciseId FROM sets WHERE id = :setId")
    suspend fun getSessionExerciseIdBySetId(setId: Int): Int

    @Query("DELETE FROM sets WHERE id = :setId")
    suspend fun delete(setId: Int)

}