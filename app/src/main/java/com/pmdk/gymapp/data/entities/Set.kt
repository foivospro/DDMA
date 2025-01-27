package com.pmdk.gymapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = SessionExercise::class,
            parentColumns = ["id"],
            childColumns = ["sessionExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Set(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionExerciseId: Int,
    val reps: Int,
    val weight: Double,
    val notes: String?
)
