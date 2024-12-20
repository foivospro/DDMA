package com.example.gymappdemo.sampledata

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Excercise(
    val name: String,
    @DrawableRes val image: Int,
    @StringRes val description: Int
)
