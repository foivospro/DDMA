package com.example.gymappdemo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gymappdemo.data.entities.GymSession

@Dao
interface GymSessionDao {
    @Insert
    suspend fun insert(session: GymSession): Long

    @Query("SELECT * FROM gym_sessions WHERE id = :id")
    suspend fun getSessionById(id: Int): GymSession?

    @Query("SELECT * FROM gym_sessions WHERE userId = :userId ORDER BY date DESC")
    suspend fun getSessionsByUser(userId: Int): List<GymSession>
    @Delete
    suspend fun delete(session: GymSession)

    @Update
    suspend fun update(session: GymSession)

}
