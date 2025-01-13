package com.example.gymappdemo.ui.screens


import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gymappdemo.R
import com.example.gymappdemo.data.entities.User
import com.example.gymappdemo.ui.theme.AppThemeType
import com.example.gymappdemo.ui.theme.primaryLight_Orange
import com.example.gymappdemo.ui.theme.primaryLight_Default
import com.example.gymappdemo.ui.theme.primaryLight_Yellow
import com.example.gymappdemo.ui.theme.primaryLight_Purple
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackPressed: () -> Unit,
    viewModel: MyProfileViewModel
) {
    val context = LocalContext.current

    // Παρακολουθώ user, accent color & dark mode από το ViewModel
    val user by viewModel.user.collectAsState()
    val currentAccent by viewModel.selectedAccentColor.collectAsState()
    val currentDarkMode by viewModel.isDarkModeEnabled.collectAsState()

    var localAccentColor by remember { mutableStateOf(currentAccent) }
    var localDarkMode by remember { mutableStateOf(currentDarkMode) }

    var username by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var age by remember { mutableIntStateOf(user?.age ?: 20) }
    var height by remember { mutableIntStateOf(user?.height ?: 170) }
    var weight by remember { mutableIntStateOf(user?.weight ?: 75) }
    var password by remember { mutableStateOf(user?.passwordHash ?: "") }
    val profilePicture by viewModel.profilePictureUri.collectAsState()


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
            password != initialUserState?.passwordHash ||
            profilePicture != initialUserState?.profilePicture ||
            localAccentColor != currentAccent ||
            localDarkMode != currentDarkMode



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


            // User Picture Placeholder
            var showImageOptions by remember { mutableStateOf(false) }

            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    if (uri != null) {
                        viewModel.changeUri(uri)
                    }
                }
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    .clickable {
                        // Show a dialog with options
                        showImageOptions = true
                    }
            ) {
                if (profilePicture != null) {
                    AsyncImage(
                        model = profilePicture,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (showImageOptions) {
                AlertDialog(
                    onDismissRequest = { showImageOptions = false },
                    title = { Text("Profile Picture Options") },
                    text = { Text("What would you like to do?") },
                    confirmButton = {
                        TextButton(onClick = {
                            // Logic to pick a new image
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            showImageOptions = false
                        }) {
                            Text("Change Picture")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            // Logic to remove the image
                            viewModel.changeUri(null)
                            showImageOptions = false
                        }) {
                            Text("Remove Picture")
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Editable Fields
            UserProfileField(
                title = "Username",
                value =  username,
                onValueChange = { username = it }
            )

            UserProfileField(
                title = "Email",
                value = email,
                onValueChange = { email = it }
            )

            var passwordError by remember { mutableStateOf<String?>(null) }
            var passwordVisible by remember { mutableStateOf(false) }

            UserProfileField(
                title = "Password",
                value = password,
                onValueChange = {
                    password = it
                    passwordError = validatePassword(password)
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
                    label = "Height(cm)",
                    value = height,
                    onValueChange = { height = it },
                    range = 50..250
                )

                StyledNumberPickerField(
                    label = "Weight(kg)",
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

            Spacer(modifier = Modifier.width(8.dp))

            // Dark Mode Switch
            // Row with the icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sun icon for Light Mode
                    Icon(
                        imageVector = Icons.Filled.WbSunny,
                        contentDescription = "Light Mode",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                localDarkMode = false
                            }
                    )
                    Switch(
                        checked = localDarkMode,
                        onCheckedChange = { localDarkMode = it }
                    )
                    // Moon icon
                    Icon(
                        imageVector = Icons.Filled.NightsStay,
                        contentDescription = "Dark Mode",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                localDarkMode = true
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Selection Section
            ThemeSelectionSection(
                availableThemes = viewModel.getAvailableThemes(),
                selectedTheme = localAccentColor,
                onThemeSelected = { chosen ->
                    localAccentColor = chosen
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    profilePicture?.let {
                        context.contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }

                    val updatedUser = User(
                        id = user?.id ?: 0,
                        name = username,
                        email = email,
                        age = age,
                        height = height,
                        weight = weight,
                        passwordHash = password,
                        profilePicture = profilePicture
                    )
                    viewModel.updateUser(updatedUser)
                    viewModel.updateTheme(localAccentColor)
                    viewModel.toggleDarkMode(localDarkMode)
                    viewModel.saveThemeChanges()
                    onBackPressed()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                enabled = hasChanges && passwordError == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text("Save Changes")
            }
        }
    }
}


@Composable
fun ThemeCircle(
    themeType: AppThemeType,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    // Define the primary color based on the themeType
    val color = when (themeType) {
        AppThemeType.DEFAULT -> primaryLight_Default
        AppThemeType.ORANGE -> primaryLight_Orange
        AppThemeType.PURPLE -> primaryLight_Purple
        AppThemeType.YELLOW -> primaryLight_Yellow
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable { onSelect() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null,
    errorMessage: String? = null,
    isNumber: Boolean = false
) {
    var textValue by remember { mutableStateOf(value) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = textValue,
            onValueChange = {
                if (isNumber) {
                    // Επιτρέπουμε μόνο αριθμούς
                    if (it.all { char -> char.isDigit() }) {
                        textValue = it
                        onValueChange(it)
                    }
                } else {
                    textValue = it
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            isError = errorMessage != null,
            //shape = RoundedCornerShape(4.dp),
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword && onPasswordToggle != null) {
                    IconButton(onClick = { onPasswordToggle() }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            },
            leadingIcon = {
                val icon = when (title) {
                    "Username" -> Icons.Filled.Person
                    "Email" -> Icons.Filled.Mail
                    else -> Icons.Filled.Key
                }

                Icon(
                    imageVector = icon,
                    contentDescription = when (title) {
                        "Username" -> "Profile Icon"
                        "Email" -> "Mail Icon"
                        else -> "Password Icon"
                    },
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                errorIndicatorColor = MaterialTheme.colorScheme.error
            )
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Validation Function
fun validatePassword(password: String): String? {
    val lengthValid = password.length >= 6
    val capitalValid = password.any { it.isUpperCase() }
    val numberValid = password.any { it.isDigit() }
    val symbolValid = password.any { !it.isLetterOrDigit() }

    return when {
        !lengthValid -> "Password must be at least 6 characters long."
        !capitalValid -> "Password must contain at least one uppercase letter."
        !numberValid -> "Password must contain at least one number."
        !symbolValid -> "Password must contain at least one special character."
        else -> null // No errors
    }
}

@Composable
fun ThemeSelectionSection(
    availableThemes: List<AppThemeType>,
    selectedTheme: AppThemeType,
    onThemeSelected: (AppThemeType) -> Unit
) {
    Text(
        text = "Choose App Color",
        style = MaterialTheme.typography.labelLarge,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(
            items = availableThemes,
            key = { it.ordinal }
        ) { themeType ->
            ThemeCircle(
                themeType = themeType,
                isSelected = (themeType == selectedTheme),
                onSelect = { onThemeSelected(themeType) }
            )
        }
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
                    contentDescription = "Increase",
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
                    contentDescription = "Decrease",
                    tint = if (value > range.first) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

