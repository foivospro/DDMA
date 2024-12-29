package com.example.gymappdemo.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_exercises")
data class SessionExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int, // Foreign key to GymSession
    val exerciseId: Int, // Foreign key to Exercise
    val order: Int // Order of exercise in the session
)

