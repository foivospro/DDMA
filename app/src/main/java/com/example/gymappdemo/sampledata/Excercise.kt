package com.example.gymappdemo.sampledata

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Excercise(
    @StringRes val name: Int,
    @DrawableRes val image: Int,
    @StringRes val description: Int
)
