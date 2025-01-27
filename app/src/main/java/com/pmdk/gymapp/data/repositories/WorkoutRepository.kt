package com.pmdk.gymapp.data.repositories

import com.pmdk.gymapp.data.dao.ExerciseDao
import com.pmdk.gymapp.data.dao.GymSessionDao
import com.pmdk.gymapp.data.dao.SessionExerciseDao
import com.pmdk.gymapp.data.dao.SetDao
import com.pmdk.gymapp.data.entities.Exercise
import com.pmdk.gymapp.data.entities.ExerciseWithSets
import com.pmdk.gymapp.data.entities.GymSession
import com.pmdk.gymapp.data.entities.SessionExercise
import com.pmdk.gymapp.data.entities.Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WorkoutRepository private constructor(
    private val gymSessionDao: GymSessionDao,
    private val sessionExerciseDao: SessionExerciseDao,
    private val exerciseDao: ExerciseDao,
    private val setDao: SetDao
) {

    // GymSession Operations

    suspend fun getSessionsForUser(userId: Int): List<GymSession> {
        return gymSessionDao.getSessionsByUserId(userId)
    }

    suspend fun getSessionById(sessionId: Int): GymSession? {
        return gymSessionDao.getSessionById(sessionId)
    }

    suspend fun insertSession(session: GymSession): Long {
        return gymSessionDao.insert(session)
    }

    suspend fun updateSession(session: GymSession) {
        gymSessionDao.update(session)
    }

    // SessionExercise Operations
    suspend fun getActiveSession(userId: Int): GymSession? {
        return gymSessionDao.getActiveSession(userId)
    }

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


    // Exercise Operations
    fun getAllExercises(): Flow<List<Exercise>> {
        return exerciseDao.getAllExercises()
    }

    suspend fun getExercisesWithSets(sessionId: Int): List<ExerciseWithSets> {
        return sessionExerciseDao.getExercisesWithSetsBySession(sessionId)
    }

    suspend fun getExerciseById(exerciseId: Int): Exercise? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    // Set Operations
    suspend fun getSetsForExercise(sessionExerciseId: Int): List<Set> {
        return setDao.getSetsForExercise(sessionExerciseId)
    }

    suspend fun addSet(set: Set) {
        withContext(Dispatchers.IO) {
            setDao.insert(set)
        }
    }

    suspend fun updateSet(set: Set) {
        setDao.update(set)
    }

    suspend fun insertSet(set: Set) {
        setDao.insert(set)
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
