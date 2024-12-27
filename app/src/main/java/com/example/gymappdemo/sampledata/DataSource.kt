package com.example.gymappdemo.sampledata

import com.example.gymappdemo.R

class DataSource {
    object ExcercisesRepo {
        val excercisesList: List<Excercise> =
            listOf(
                Excercise(R.string.snatch, R.drawable.weightlifter, R.string.snatch),
                Excercise(R.string.biceps, R.drawable.weightlifter, R.string.biceps),
                Excercise(R.string.bench, R.drawable.weightlifter, R.string.bench),
                Excercise(R.string.snatch, R.drawable.weightlifter, R.string.snatch),
                Excercise(R.string.biceps, R.drawable.weightlifter, R.string.biceps),
                Excercise(R.string.bench, R.drawable.weightlifter, R.string.bench),
                Excercise(R.string.snatch, R.drawable.weightlifter, R.string.snatch),
                Excercise(R.string.biceps, R.drawable.weightlifter, R.string.biceps),
                Excercise(R.string.bench, R.drawable.weightlifter, R.string.bench),
                Excercise(R.string.snatch, R.drawable.weightlifter, R.string.snatch),
                Excercise(R.string.biceps, R.drawable.weightlifter, R.string.biceps),
                Excercise(R.string.bench, R.drawable.weightlifter, R.string.bench)
            )
    }
}