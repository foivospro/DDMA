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
    @Query("""
        SELECT 
            exercises.*,
            session_exercises.id AS session_id,
            session_exercises.sessionId AS session_sessionId,
            session_exercises.exerciseId AS session_exerciseId,
            session_exercises.[order] AS session_order
        FROM session_exercises
        INNER JOIN exercises ON session_exercises.exerciseId = exercises.id
        WHERE session_exercises.sessionId = :sessionId
    """)
    suspend fun getExercisesWithSetsBySession(sessionId: Int): List<ExerciseWithSets>
}