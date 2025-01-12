package com.example.gymappdemo.ui.screens


import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gymappdemo.Navigation.GymAppScreen
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: MyProfileViewModel // Inject the ViewModel
) {

    // Observe user data
    val user = viewModel.user.collectAsState().value



    // If the user is guest or log in
    if (user == null) {
        GuestUserPrompt(navController)
    } else {
        LaunchedEffect(user) {
            viewModel.loadProfilePicture(user.id)
        }
        var showLogoutDialog by remember { mutableStateOf(false) }

        // Handle logout confirmation
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false }, // Close the dialog on dismiss
                title = { Text("Log Out") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Handle logout logic (clear session, user data, etc.)
                            // Navigate to login screen
                            navController.navigate("login") // Change to your login screen route
                            viewModel.logout()
                            showLogoutDialog = false // Close the dialog
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(), // Ensure the row takes the full width
                            horizontalArrangement = Arrangement.SpaceBetween, // Spread items across the row (title at start, icon at end)
                            verticalAlignment = Alignment.CenterVertically // Vertically center items in the row
                        ) {
                            Text(
                                "My Profile",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(start = 8.dp)
                                    .clickable {
                                        showLogoutDialog = true
                                    }
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())

            ) {
                // User Picture
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        if (user.profilePicture != null) {
                            rememberAsyncImagePainter(user.profilePicture.toUri())
                        } else {
                            painterResource(com.example.gymappdemo.R.drawable.default_profile)
                        },
                        contentDescription = "User Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    TextField(
                        value = user.name,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
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

                // Email
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
                        value = user.email,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Mail,
                                contentDescription = "Mail Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                val height = user?.height?.toString() ?: ""
                val weight = user?.weight?.toString() ?: ""
                val age = user?.age?.toString() ?: ""

                var messenger = "Enter your "
                if (height.isNullOrEmpty() || height == "0") {
                    messenger += "Height, "
                }
                if (weight.isNullOrEmpty() || weight == "0") {
                    messenger += "Weight, "
                }
                if (age.isNullOrEmpty() || age == "0") {
                    messenger += "Age"
                }

                messenger += " in settings"


                if (height.isNullOrEmpty() || height == "0"
                    || weight.isNullOrEmpty() || weight == "0"
                    || age.isNullOrEmpty() || age == "0") {
                    Text(
                        text = messenger,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Height, Weight, Age
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(20.dp))



                    UserDetailCard(label = "Height", value = height)
                    UserDetailCard(label = "Weight", value = weight)
                    UserDetailCard(label = "Age", value = age)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Settings Button
                Button(
                    onClick = {
                        navController.navigate(GymAppScreen.ProfileSettings.name)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium, // Customize the button shape
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
fun UserDetailCard(label: String, value: String) {
    if (value.isNullOrEmpty() || value == "0") {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            if (label == "Height"){
                Icon(
                    painter = painterResource(com.example.gymappdemo.R.drawable.height),
                    contentDescription = "Icon for Height",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp), // Optional padding around the icon
                    tint = MaterialTheme.colorScheme.primary // Set the icon color to primary
                )

            } else if (label == "Weight"){
                Icon(
                    painter = painterResource(com.example.gymappdemo.R.drawable.weight2),
                    contentDescription = "Icon for Weight",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp), // Optional padding around the icon
                    tint = MaterialTheme.colorScheme.primary // Set the icon color to primary
                )

            } else {
                Icon(
                    painter = painterResource(com.example.gymappdemo.R.drawable.calendar),
                    contentDescription = "Icon for Age",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp), // Optional padding around the icon
                    tint = MaterialTheme.colorScheme.primary // Set the icon color to primary
                )
            }
        }


    } else {
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
                    style = MaterialTheme.typography.headlineMedium, // Use Body Large style
                    color = MaterialTheme.colorScheme.primary //MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge, // Use Body Small style
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun GuestUserPrompt(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "You are logged in as a Guest",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "To access full features, please log in to your account.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { navController.navigate("login") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Log In",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}






