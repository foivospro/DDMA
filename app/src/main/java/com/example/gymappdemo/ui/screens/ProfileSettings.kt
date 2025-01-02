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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.material3.Icon

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.gymappdemo.ui.theme.GymAppDemoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .verticalScroll(rememberScrollState())
        ) {
            // User Picture Placeholder (Optional)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Dark Mode Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp, // Set a specific font size for the label
                    fontWeight = FontWeight.Bold // Make the label bolder
                )
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onDarkModeToggle(it) }
                )
            }

            // Editable Fields
            // Username Field
            UserProfileField(
                title = "Username",
                value = "JohnDoe",
                onValueChange = { /* Handle input */ }
            )

            // Email Field
            UserProfileField(
                title = "Email",
                value = "johndoe@example.com",
                onValueChange = { /* Handle input */ }
            )

            // Password Field with Toggle Visibility
            var passwordVisible by remember { mutableStateOf(false) }
            UserProfileField(
                title = "Password",
                value = "password123",
                onValueChange = { /* Handle password input */ },
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Get Age, Height, Weight
            var age by remember { mutableStateOf(20) }
            var height by remember { mutableStateOf(170) }
            var weight by remember { mutableStateOf(70) }

            // Row for Age, Height, Weight fields
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Spread the fields equally
                verticalAlignment = Alignment.CenterVertically
            ){
                StyledNumberPickerField(
                    label = "Height (cm)",
                    value = height,
                    onValueChange = { height = it },
                    range = 50..250
                )

                StyledNumberPickerField(
                    label = "Weight (kg)",
                    value = weight,
                    onValueChange = { weight = it },
                    range = 10..200
                )

                StyledNumberPickerField(
                    label = "Age",
                    value = age,
                    onValueChange = { age = it },
                    range = 1..100
                )
            }

            // Save Button
            Button(
                onClick = { /* Save changes logic */ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),

            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun UserProfileField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),  // Added padding for better spacing
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Enhanced TextField with weight to avoid overlapping the button
        Column(modifier = Modifier.fillMaxSize()) { // Ensure the TextField doesn't overlap
            EnhancedTextField(
                title = title,
                value = value,
                onValueChange = onValueChange,
                isPassword = isPassword,
                isPasswordVisible = isPasswordVisible,
                onPasswordToggle = onPasswordToggle
            )
        }

        /*  .weight(1f)  Button(
            onClick = { /* Change button logic */ },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("Change")
        }*/
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(isPasswordVisible) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 18.sp, // Set a specific font size for the label
            fontWeight = FontWeight.Bold // Make the label bolder
            //modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            singleLine = true,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword && onPasswordToggle != null) {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                        onPasswordToggle()
                    }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,  // Hides the focused indicator line
                unfocusedIndicatorColor = Color.Transparent, // Hides the unfocused indicator line
            )
        )
    }
}

@Composable
fun StyledNumberPickerField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp, // Set a specific font size for the label
                    fontWeight = FontWeight.Bold // Make the label bolder
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Increase Button
            IconButton(
                onClick = {
                    if (value < range.last) onValueChange(value + 1)
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Current value text
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp, // Larger font size for better visibility
                    fontWeight = FontWeight.Medium // Medium weight for value text
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Decrease Button
            IconButton(
                onClick = {
                    if (value > range.first) onValueChange(value - 1)
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditProfileScreen() {
    GymAppDemoTheme {
        EditProfileScreen(
            isDarkMode = false,
            onDarkModeToggle = {},
            onBackPressed = {})
    }
}