package com.example.gymappdemo.data.repositories

import com.example.gymappdemo.data.dao.ExerciseDao
import com.example.gymappdemo.data.dao.GymSessionDao
import com.example.gymappdemo.data.dao.SessionExerciseDao
import com.example.gymappdemo.data.dao.SetDao
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.data.entities.GymSession
import com.example.gymappdemo.data.entities.SessionExercise

class WorkoutRepository private constructor(
    private val gymSessionDao: GymSessionDao,
    private val sessionExerciseDao: SessionExerciseDao,
    private val exerciseDao: ExerciseDao,
    private val setDao: SetDao
) {

    // GymSession Operations
    suspend fun getSessionByUser(userId: Int): List<GymSession> {
        return gymSessionDao.getSessionsForUser(userId)
    }

    suspend fun insertSession(session: GymSession) {
        gymSessionDao.insert(session)
    }

    // SessionExercise Operations
    suspend fun insertSessionExercise(sessionExercise: SessionExercise) {
        sessionExerciseDao.insert(sessionExercise)
    }

    suspend fun getExercisesForSession(sessionId: Int): List<SessionExercise> {
        return sessionExerciseDao.getExercisesForSession(sessionId)
    }

    // Exercise Operations
    suspend fun getAllExercises(): List<Exercise> {
        return exerciseDao.getAllExercises()
    }

    suspend fun insertExercise(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    // Set Operations
    suspend fun getSet(setId: Int) {
        setDao.getSet(setId)
    }

    companion object {
        @Volatile
        private var INSTANCE: WorkoutRepository? = null

        fun getInstance(
            gymSessionDao: GymSessionDao,
            sessionExerciseDao: SessionExerciseDao,
            exerciseDao: ExerciseDao,
            setDao: SetDao
        ): WorkoutRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = WorkoutRepository(
                    gymSessionDao = gymSessionDao,
                    sessionExerciseDao = sessionExerciseDao,
                    exerciseDao = exerciseDao,
                    setDao = setDao
                )
                INSTANCE = instance
                instance
            }
        }
    }
}
