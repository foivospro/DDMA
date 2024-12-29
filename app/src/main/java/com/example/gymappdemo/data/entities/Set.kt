package com.example.gymappdemo.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sets")
data class Set(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionExerciseId: Int, // Foreign key to SessionExercise
    val reps: Int,
    val weight: Double, // Weight lifted, with decimal precision
    val notes: String?
)