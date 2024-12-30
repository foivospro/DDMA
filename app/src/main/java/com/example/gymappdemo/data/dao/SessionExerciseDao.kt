package com.example.gymappdemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gymappdemo.data.entities.SessionExercise

@Dao
interface SessionExerciseDao {
    @Insert
    suspend fun insert(sessionExercise: SessionExercise): Long

    @Query("SELECT * FROM session_exercises WHERE sessionId = :sessionId")
    suspend fun getExercisesForSession(sessionId: Int): List<SessionExercise>
}