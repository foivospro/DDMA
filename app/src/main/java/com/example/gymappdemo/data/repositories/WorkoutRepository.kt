package com.example.gymappdemo.data.repositories

import com.example.gymappdemo.data.dao.ExerciseDao
import com.example.gymappdemo.data.dao.GymSessionDao
import com.example.gymappdemo.data.dao.SessionExerciseDao
import com.example.gymappdemo.data.dao.SetDao
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.data.entities.ExerciseWithSets
import com.example.gymappdemo.data.entities.GymSession
import com.example.gymappdemo.data.entities.SessionExercise
import com.example.gymappdemo.data.entities.Set

class WorkoutRepository private constructor(
    private val gymSessionDao: GymSessionDao,
    private val sessionExerciseDao: SessionExerciseDao,
    private val exerciseDao: ExerciseDao,
    private val setDao: SetDao
) {

    // GymSession Operations
    suspend fun getSessionById(sessionId: Int): GymSession? {
        return gymSessionDao.getSessionById(sessionId)
    }

    suspend fun getSessionsByUser(userId: Int): List<GymSession> {
        return gymSessionDao.getSessionsByUser(userId)
    }

    suspend fun insertSession(session: GymSession): Long {
        return gymSessionDao.insert(session)
    }

    suspend fun updateSession(session: GymSession) {
        gymSessionDao.update(session)
    }

    suspend fun deleteSession(session: GymSession) {
        gymSessionDao.delete(session)
    }

    // SessionExercise Operations
    suspend fun insertSessionExercise(sessionExercise: SessionExercise): Long {
        return sessionExerciseDao.insert(sessionExercise)
    }

    suspend fun getSessionExercisesBySessionId(sessionId: Int): List<SessionExercise> {
        return sessionExerciseDao.getSessionExercisesBySessionId(sessionId)
    }

    suspend fun getSessionExerciseIdBySetId(setId: Int): Int {
        return setDao.getSessionExerciseIdBySetId(setId)
    }

    suspend fun deleteSessionExercise(sessionExerciseId: Int) {
        sessionExerciseDao.deleteSessionExercise(sessionExerciseId)
    }

    suspend fun getSetsBySessionExerciseId(sessionExerciseId: Int): List<Set> {
        return setDao.getSetsBySessionExerciseId(sessionExerciseId)
    }

    // Exercise Operations
    suspend fun getAllExercises(): List<Exercise> {
        return exerciseDao.getAllExercises()
    }

    suspend fun insertExercise(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    suspend fun getExercisesWithSets(sessionId: Int): List<ExerciseWithSets> {
        return sessionExerciseDao.getExercisesWithSetsBySession(sessionId)
    }

    // Set Operations
    suspend fun getSetsForExercise(sessionExerciseId: Int): List<Set> {
        return setDao.getSetsForExercise(sessionExerciseId)
    }

    suspend fun addSet(set: Set) {
        setDao.insert(set)
    }

    suspend fun updateSet(set: Set) {
        setDao.update(set)
    }

    suspend fun deleteSet(setId: Int) {
        setDao.delete(setId)
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
