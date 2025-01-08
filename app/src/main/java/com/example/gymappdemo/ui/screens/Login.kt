// LoginScreen.kt
package com.example.gymappdemo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappdemo.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymappdemo.ui.theme.GymAppDemoTheme



@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    // States for email, password, and password visibility
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center // Center the content vertically and horizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Center the content vertically within the Column
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .offset(y = (-40).dp)
        ) {
            // Logo or Image
            Image(
                painter = painterResource(id = R.drawable.start_new_workout_icon),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            // Title
            Text(
                text = "Καλωσήρθες!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 24.dp) // Increased bottom padding for better spacing
            )

// Email Input Field without KeyboardOptions
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = showError && email.isEmpty()
            )

// Password Input Field without KeyboardOptions
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Κωδικός") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        val icon = if (isPasswordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                isError = showError && password.isEmpty()
            )

            // Error Message
            if (showError) {
                Text(
                    text = "Παρακαλώ συμπληρώστε όλα τα πεδία.",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing before buttons

            // Login Button
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        onLoginSuccess() // Invoke login success callback
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp )
            ) {
                Text(text = "Σύνδεση", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp)) // Spacing between buttons

            // Google Login Button
            Button(
                onClick = {
                    // Add logic for Google sign-in
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)), // Google Blue
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person, // Replace with Google icon if available
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Σύνδεση μέσω Google",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Spacing before the divider

            // Divider with "Ή"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = " Ή ",
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.weight(1f)
                )
            }

            // Continue as Guest Button
            OutlinedButton(
                onClick = { onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Συνέχεια ως Επισκέπτης",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacing before the registration prompt

            // Registration Prompt
            Row {
                Text(
                    text = "Δεν έχεις λογαριασμό; ",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Εγγραφή",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* Navigate to registration screen */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    GymAppDemoTheme {
        LoginScreen(onLoginSuccess = { /* Mock action */ })
    }
}
