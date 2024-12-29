package com.example.gymappdemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymappdemo.data.entities.Set

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(set: Set): Long
    @Update
    suspend fun update(set: Set)

    @Query("SELECT * FROM sets WHERE id = :setId")
    fun getSet(setId: Int): Set
}