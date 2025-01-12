package com.example.gymappdemo.ui.screens

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.User
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    onBackPressed: () -> Unit,
    //userId: Int,
    viewModel: MyProfileViewModel
) {
    // Observe user data from the ViewModel
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current
    // Fetch user data when the composable is loaded


    // State variables for editable fields (initialize with existing data or default values)
    var username by remember { mutableStateOf(user?.name ?: "sex") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var age by remember { mutableIntStateOf(user?.age ?: 20) }
    var height by remember { mutableIntStateOf(user?.height ?: 170) }
    var weight by remember { mutableIntStateOf(user?.weight ?: 75) }
    var password by remember { mutableStateOf(user?.passwordHash ?: "") }

    // State variables for profile picture
    var profilePicture by remember { mutableStateOf<Bitmap?>(null) }

    // Update local state variables when userState changes
    LaunchedEffect(user) {
        username = user?.name ?: ""
        email = user?.email ?: ""
        age = user?.age ?: 20
        height = user?.height ?: 170
        weight = user?.weight ?: 75
        password = user?.passwordHash ?: ""
    }

    // State to track the initial user data
    val initialUserState = user

    // Derived state to check if any changes have been made
    val hasChanges = username != initialUserState?.name ||
            email != initialUserState?.email ||
            age != initialUserState?.age ||
            height != initialUserState?.height ||
            weight != initialUserState?.weight ||
            password != initialUserState?.passwordHash


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text= stringResource(id = R.string.edit_profile)
                ) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                            id = R.string.back
                        ))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // User Picture Placeholder
            var showImageOptions by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(2.dp, Gray, CircleShape)
                    .clickable {
                        // Show a dialog with options
                        showImageOptions = true
                    }
            ) {
                if (profilePicture != null) {
                    Image(
                        bitmap = profilePicture!!.asImageBitmap(),
                        contentDescription = stringResource(id =R.string.profile_picture),
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = stringResource(id = R.string.profile_picture),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (showImageOptions) {
                AlertDialog(
                    onDismissRequest = { showImageOptions = false },
                    title = { Text(
                        text = stringResource(id = R.string.default_profile_picture)
                    )},
                    text = { Text(
                        text = stringResource(id = R.string.profile_picture_question)
                    ) },
                    confirmButton = {
                        TextButton(onClick = {
                            // Logic to pick a new image
                            pickImageFromGallery() // Create this function
                            showImageOptions = false
                        }) {
                            Text(
                                text = stringResource(id = R.string.change_picture)
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            // Logic to remove the image
                            profilePicture = null
                            showImageOptions = false
                        }) {
                            Text(
                                text = stringResource(id =R.string.remove_picture)
                            )
                        }
                    }
                )
            }

            // Editable Fields
            UserProfileField(
                title = stringResource(id =R.string.username),
                value =  username,
                onValueChange = { username = it }
            )

            UserProfileField(
                title = stringResource(id =R.string.email),
                value = email,
                onValueChange = { email = it }
            )

            var passwordError by remember { mutableStateOf<String?>(null) }
            var passwordVisible by remember { mutableStateOf(false) }

            UserProfileField(
                title = stringResource(id =R.string.password),
                value = password,
                onValueChange = {
                    password = it
                    passwordError = validatePassword(context,password)
                },
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                errorMessage = passwordError
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Row for Age, Height, Weight fields
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Spread the fields equally
                verticalAlignment = Alignment.CenterVertically
            ){
                StyledNumberPickerField(
                    label = stringResource(id =R.string.height_cm),
                    value = height,
                    onValueChange = { height = it },
                    range = 50..250
                )

                StyledNumberPickerField(
                    label = stringResource(id =R.string.weight_kg_2),
                    value = weight,
                    onValueChange = { weight = it },
                    range = 10..200
                )

                StyledNumberPickerField(
                    label = stringResource(id =R.string.age),
                    value = age,
                    onValueChange = { age = it },
                    range = 1..100
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Dark Mode Switch
            // Row with the icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id =R.string.dark_mode),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sun icon for Light Mode
                    Icon(
                        imageVector = Icons.Filled.WbSunny,
                        contentDescription = stringResource(id =R.string.light_mode),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { if (!isDarkMode) onDarkModeToggle(true) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Switch to toggle between Light/Dark mode
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onDarkModeToggle(it) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Moon icon for Dark Mode
                    Icon(
                        imageVector = Icons.Filled.NightsStay,
                        contentDescription = stringResource(id =R.string.dark_mode),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { if (isDarkMode) onDarkModeToggle(false) }
                    )
                }
            }

            // Save Button
            Button(
                onClick = {
                    val updatedUser = User(
                        id = user?.id ?: 0, // Use the existing user ID
                        name = username,
                        email = email,
                        age = age,
                        height = height,
                        weight = weight,
                        passwordHash = password
                    )
                    viewModel.updateUser(updatedUser)
                    onBackPressed()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                enabled = passwordError == null && hasChanges // Enable only if no password error and changes are made
            ) {
                Text(
                    text = stringResource(id =R.string.save_changes)
                )
            }
        }
    }
}

fun pickImageFromGallery() {
   // pickImageLauncher.launch("image/*")
}

@Composable
fun UserProfileField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null,
    errorMessage: String? = null
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
                onPasswordToggle = onPasswordToggle,
                errorMessage = errorMessage
            )
        }
    }
}

@Composable
fun EnhancedTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(isPasswordVisible) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        // TextField
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
            isError = errorMessage != null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword && onPasswordToggle != null) {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                        onPasswordToggle()
                    }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)
                        )
                    }
                }
            },
            leadingIcon = {
                val icon = when (title) {
                    stringResource(id = R.string.username) -> Icons.Default.Person
                    stringResource(id = R.string.email) -> Icons.Default.Mail
                    else -> Icons.Default.Key
                }

                Icon(
                    imageVector = icon,
                    contentDescription = when (title) {
                        stringResource(id = R.string.username) -> stringResource(id = R.string.profile_icon)
                        stringResource(id = R.string.email) -> stringResource(id = R.string.mail_icon)
                        else -> stringResource(id = R.string.password_icon)
                    },
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = if (errorMessage != null) Color.Red else Color.Transparent,
                unfocusedIndicatorColor = if (errorMessage != null) Color.Red else Color.Transparent,
                errorIndicatorColor = Color.Red
            )
        )
        // Error Message
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


// Validation Function
fun validatePassword(context:Context,password: String): String? {
    val lengthValid = password.length >= 6
    val capitalValid = password.any { it.isUpperCase() }
    val numberValid = password.any { it.isDigit() }
    val symbolValid = password.any { !it.isLetterOrDigit() }

    return when {
        !lengthValid -> context.getString(R.string.password_must_be_six)
        !capitalValid -> context.getString(R.string.password_must_contain_uppercase)
        !numberValid -> context.getString(R.string.password_must_contain_number)
        !symbolValid -> context.getString(R.string.password_must_contain_special)
        else -> null // No errors
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
                ),
                maxLines = 1, // Limit text to a single line
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Increase Button
            IconButton(
                onClick = {
                    if (value < range.last) onValueChange(value + 1)
                },
                enabled = value < range.last,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (value < range.last) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id =R.string.increase),
                    tint = if (value < range.last) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
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
                enabled = value > range.first,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (value > range.first) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = stringResource(id = R.string.decrease),
                    tint = if (value > range.first) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

