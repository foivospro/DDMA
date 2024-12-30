package com.example.gymappdemo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String,
    val description: String,
    val muscleGroup: String
)