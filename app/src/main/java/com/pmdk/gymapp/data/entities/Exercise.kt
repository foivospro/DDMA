package com.pmdk.gymapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val descriptionEn: String,
    val descriptionEl: String,
    val icon: String,
    val muscleGroup: String
)