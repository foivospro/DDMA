package com.pmdk.gymapp.ui.screens


import android.content.Context
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pmdk.gymapp.R
import com.pmdk.gymapp.data.entities.User
import com.pmdk.gymapp.ui.theme.AppThemeType
import com.pmdk.gymapp.ui.theme.primaryLight_Default
import com.pmdk.gymapp.ui.theme.primaryLight_Orange
import com.pmdk.gymapp.ui.theme.primaryLight_Purple
import com.pmdk.gymapp.ui.theme.primaryLight_Yellow
import com.pmdk.gymapp.ui.viewmodels.MyProfileViewModel

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


    var passwordError by remember { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.edit_profile),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },

                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                            id = R.string.back
                        ))
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {

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
                    .fillMaxWidth(0.95f)
                    .height(45.dp),
                enabled = hasChanges && passwordError == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                    shape = RoundedCornerShape(12.dp)
            ) {
                    Text(stringResource(id = R.string.save_changes))
            }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(6.dp)
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
                    .size(80.dp)
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
                        contentDescription = stringResource(id =R.string.profile_picture),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = stringResource(id = R.string.default_profile_picture),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (showImageOptions) {
                AlertDialog(
                    onDismissRequest = { showImageOptions = false },
                    title = { Text(stringResource(id = R.string.profile_picture_options)) },
                    text = { Text(stringResource(id = R.string.profile_picture_question)) },
                    confirmButton = {
                        TextButton(onClick = {
                            // Logic to pick a new image
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            showImageOptions = false
                        }) {
                            Text(stringResource(id = R.string.change_picture))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            // Logic to remove the image
                            viewModel.changeUri(null)
                            showImageOptions = false
                        }) {
                            Text(stringResource(id = R.string.remove_picture))
                        }
                    }
                )
            }
            // Editable Fields
            UserProfileField(
                title = stringResource(id = R.string.username),
                value =  username,
                onValueChange = { username = it }
            )

            UserProfileField(
                title = stringResource(id = R.string.email),
                value = email,
                onValueChange = { email = it }
            )

            var passwordVisible by remember { mutableStateOf(false) }

            UserProfileField(
                title = stringResource(id = R.string.password),
                value = password,
                onValueChange = {
                    password = it
                    passwordError = validatePassword(password, context = context)
                },
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                errorMessage = passwordError
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Row for Age, Height, Weight fields
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Spread the fields equally
                verticalAlignment = Alignment.CenterVertically
            ){
                StyledNumberPickerField(
                    label = stringResource(id = R.string.height_cm),
                    value = height,
                    onValueChange = { height = it },
                    range = 50..250
                )

                StyledNumberPickerField(
                    label = stringResource(id = R.string.weight_kg_2),
                    value = weight,
                    onValueChange = { weight = it },
                    range = 10..200
                )

                StyledNumberPickerField(
                    label = stringResource(id = R.string.age),
                    value = age,
                    onValueChange = { age = it },
                    range = 1..100
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Dark Mode Switch
            // Row with the icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.dark_mode),
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
                        contentDescription = stringResource(id = R.string.light_mode),
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
                        contentDescription = stringResource(id = R.string.dark_mode),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                localDarkMode = true
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            // Theme Selection Section
            ThemeSelectionSection(
                availableThemes = viewModel.getAvailableThemes(),
                selectedTheme = localAccentColor,
                onThemeSelected = { chosen ->
                    localAccentColor = chosen
                }
            )
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
                contentDescription = stringResource(id = R.string.selected),
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
        .padding(6.dp)
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
                            contentDescription =
                            if (isPasswordVisible)
                                stringResource(id = R.string.hide_password)
                            else
                                stringResource(id = R.string.show_password)
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
fun validatePassword(password: String, context: Context): String? {
    val lengthValid = password.length >= 6
    val capitalValid = password.any { it.isUpperCase() }
    val numberValid = password.any { it.isDigit() }
    val symbolValid = password.any { !it.isLetterOrDigit() }
    val language = context.resources.configuration.locales[0].language
    return when {
        !lengthValid ->
            if (language == "en")
                "Password must be at least 6 characters long."
            else
                "Ο κωδικός πρέπει να περιέχει τουλάχιστον 6 χαρακτήρες."
        !capitalValid ->
            if (language == "en")
                "Password must contain at least one uppercase letter."
            else
                "Ο κωδικός πρέπει να περιέχει τουλάχιστον ένα κεφαλαίο γράμμα."
        !numberValid ->
            if (language == "en")
                "Password must contain at least one number."
            else
                "Ο κωδικός πρέπει να περιέχει τουλάχιστον έναν αριθμό."
        !symbolValid ->
            if (language == "en")
                "Password must contain at least one special character."
            else
                "Ο κωδικός πρέπει να περιέχει τουλάχιστον έναν ειδικό χαρακτήρα."
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
        text = stringResource(id = R.string.choose_app_color),
        style = MaterialTheme.typography.labelLarge,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )



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
                    contentDescription = stringResource(id = R.string.increase),
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

