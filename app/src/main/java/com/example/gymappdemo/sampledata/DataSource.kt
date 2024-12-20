package com.example.gymappdemo.sampledata

import com.example.gymappdemo.R

class DataSource {
    val excercisesList: List<Excercise> =
        listOf(
            Excercise("Snatch", R.drawable.weightlifter,R.string.snatch),
            Excercise("Bicep Curls", R.drawable.weightlifter,R.string.biceps),
            Excercise("Bench Press", R.drawable.weightlifter,R.string.bench)
    )
}