package com.example.gymappdemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.data.entities.SessionExercise

@Dao
interface SessionExerciseDao {

    @Query("DELETE FROM session_exercises WHERE id = :sessionExerciseId")
    suspend fun deleteSessionExercise(sessionExerciseId: Int)

    @Insert
    suspend fun insert(sessionExercise: SessionExercise): Long

    @Query("SELECT * FROM session_exercises WHERE sessionId = :sessionId")
    suspend fun getSessionExercisesBySessionId(sessionId: Int): List<SessionExercise>

    @Transaction
    @Query("SELECT * FROM session_exercises WHERE sessionId = :sessionId")
    suspend fun getExercisesWithSetsBySession(sessionId: Int): List<ExerciseWithSets>
}