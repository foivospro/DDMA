package com.example.gymappdemo.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseWithSets(
    @Embedded val sessionExercise: SessionExercise,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id"
    )
    val exercise: Exercise,

    @Relation(
        parentColumn = "id",
        entityColumn = "sessionExerciseId"
    )
    val sets: List<Set>
)