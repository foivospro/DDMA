package com.example.gymappdemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.material3.Icon

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.ui.draw.clip
import com.example.gymappdemo.ui.theme.GymAppDemoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettings() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Personal Data",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                },
                navigationIcon = {
                    IconButton(onClick = { /* Κενή ενέργεια για τώρα */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Picture
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera), // Dummy image
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }
            // Dark Mode Switch
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark Mode")
                Switch(checked = false, onCheckedChange = {})
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Username Field
            UserProfileField(label = "Username")

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            UserProfileField(label = "Email")

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            UserProfileField(label = "Password")

            // Age Field
            UserProfileField(label = "Age")

            Spacer(modifier = Modifier.height(16.dp))

            // Height Field
            UserProfileField(label = "Height")

            Spacer(modifier = Modifier.height(16.dp))

            // Weight Field
            UserProfileField(label = "Weight")

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}

@Composable
fun UserProfileField(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left part (label + text field)
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(text = label)
            TextField(
                value = "",
                label = { Text(label) },
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )
        }

        // Right part (change button)
        Button(onClick = { /* Handle change */ }) {
            Text("Change")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSettingsPreview() {
    GymAppDemoTheme {
        ProfileSettings()
    }
}