package com.example.gymappdemo.utils

import com.example.gymappdemo.R

object IconResourceMapper {
    fun getIconResource(icon: String): Int {
        return when (icon.lowercase()) {
            "bench" -> R.drawable.bench
            "biceps" -> R.drawable.biceps
            "cycling" -> R.drawable.cycling
            "deadlift" -> R.drawable.deadlift
            "dips" -> R.drawable.dips
            "pullups" -> R.drawable.pullups
            "rdl" -> R.drawable.rdl
            "rows" -> R.drawable.rows
            "snatch" -> R.drawable.snatch
            "squat" -> R.drawable.squat
            "triceps" -> R.drawable.triceps
            "triceps2" -> R.drawable.triceps2
            else -> R.drawable.weightlifter
        }
    }
}