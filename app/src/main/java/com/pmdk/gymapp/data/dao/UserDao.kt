package com.pmdk.gymapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pmdk.gymapp.data.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE Email=:email AND passwordHash=:password")
    suspend fun loginUser(email: String, password: String): User?

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    // Get user by email for persistent login
    @Query("SELECT * FROM users WHERE Email = :email")
    suspend fun getUserByEmail(email: String): User?

    // Check if a user with a specific email already exists (for registration checks)
    @Query("SELECT COUNT(*) > 0 FROM users WHERE Email = :email")
    suspend fun doesUserExist(email: String): Boolean

}