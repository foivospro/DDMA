package com.example.gymappdemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gymappdemo.data.entities.GymSession

@Dao
interface GymSessionDao {
    @Insert
    suspend fun insert(session: GymSession): Long
    @Query("SELECT * FROM gym_sessions WHERE id = :id")
    suspend fun getSessionById(id: Int): GymSession

    @Query("SELECT * FROM gym_sessions where userId = :userId")
    suspend fun getSessionsForUser(userId: Int): List<GymSession>
    }
