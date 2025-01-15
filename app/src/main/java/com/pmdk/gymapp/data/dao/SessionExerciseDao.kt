package com.pmdk.gymapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.pmdk.gymapp.data.entities.ExerciseWithSets
import com.pmdk.gymapp.data.entities.GymSession
import com.pmdk.gymapp.data.entities.SessionExercise

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

    @Query("SELECT * FROM gym_sessions WHERE duration = 0 LIMIT 1")
    suspend fun getActiveSession(): GymSession?
}