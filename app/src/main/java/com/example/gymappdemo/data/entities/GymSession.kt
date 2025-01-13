package com.example.gymappdemo.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gym_sessions")
data class GymSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Foreign key to User
    val date: String, // ISO 8601 format (e.g., "2023-12-31")
    val notes: String?,
    val duration: Int
)