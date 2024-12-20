package com.example.gymappdemo

import android.graphics.drawable.Icon
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappdemo.ui.theme.GymAppDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymAppDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(){
        ExerciseCard(iconResource = R.drawable.weightlifter, descriptionRes = R.string.biceps)
    }
}

@Composable
fun ExerciseCard(@DrawableRes iconResource: Int, @StringRes descriptionRes: Int) {
    Card(
        modifier = Modifier.size(72.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = stringResource(id = descriptionRes) // Use descriptionRes
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GymAppDemoTheme {
        Greeting("Android")
    }
}