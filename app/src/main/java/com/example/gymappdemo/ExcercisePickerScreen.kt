package com.example.gymappdemo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.Row
import com.example.gymappdemo.sampledata.Excercise

@Composable
fun ExcerciseCard(
    excercise : Excercise,
    modifier: Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .sizeIn(minHeight = 72.dp)
    ) {
        
    }
}