package com.example.gymappdemo.ui.screens

//noinspection SuspiciousImport
import android.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gymappdemo.Navigation.GymAppScreen
import com.example.gymappdemo.ui.theme.GymAppDemoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navController: NavController) {
    //val navController = rememberNavController() // Access NavController in the composable
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Personal",
                        style = MaterialTheme.typography.titleLarge, // Apply Title Large style
                        color = MaterialTheme.colorScheme.onSurface // Use theme color
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Picture
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(com.example.gymappdemo.R.drawable.weightlifter),
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape) // Use theme color
                )
            }

            // User Information (Username & Email)
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Username",
                    style = MaterialTheme.typography.bodyMedium, // Use Body Medium style
                    color = MaterialTheme.colorScheme.onSurface // Use theme color
                )

                TextField(
                    value = "JohnDoe",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Email",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextField(
                    value = "john.doe@example.com",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_dialog_email),
                            contentDescription = "Mail Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Height, Weight, Age
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                UserDetailCard(label = "Height", value = "180cm")
                UserDetailCard(label = "Weight", value = "80kg")
                UserDetailCard(label = "Age", value = "22y")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Button
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleSmall, // Use Title Medium style
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.clickable {
                    navController.navigate(GymAppScreen.ProfileSettings.name) // Navigate to ProfileSettings
                }
            )
        }
    }
}

@Composable
fun UserDetailCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge, // Use Body Large style
                color = MaterialTheme.colorScheme.primary //MaterialTheme.colorScheme.onSurface

            )
            Text(
                text = label,

                style = MaterialTheme.typography.bodySmall, // Use Body Small style
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyProfileScreenPreview() {
    GymAppDemoTheme { // Wrap in your custom theme
        //UserProfileScreen()
    }
}

