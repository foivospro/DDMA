package com.example.gymappdemo.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gymappdemo.Navigation.GymAppScreen
import com.example.gymappdemo.R
import com.example.gymappdemo.ui.theme.GymAppDemoTheme
import com.example.gymappdemo.ui.viewmodels.HomeViewModel
import com.example.gymappdemo.ui.viewmodels.MyProfileViewModel
import com.example.gymappdemo.ui.viewmodels.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    homeViewModel: HomeViewModel,
    myProfileViewModel: MyProfileViewModel,
    navController: NavController
) {
    // States for email, password, confirm password, and visibility
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val errorMessage by registerViewModel.errorMessage.collectAsState()
    var showSuccessfulRegister by remember { mutableStateOf(false) }
    var showErrorRegister by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .offset(y = (-40).dp)
        ) {
            item {
                // Logo or Image
                Image(
                    painter = painterResource(id = R.drawable.start_new_workout_icon),
                    contentDescription = stringResource(id = R.string.app_logo),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            item {
                // Title
                Text(
                    text = stringResource(id = R.string.create_account),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            item {
                // Email Input Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(
                        text = stringResource(id = R.string.email)
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    isError = showError && email.isEmpty()
                )
            }

            item {
                // Username Input Field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(
                        stringResource(id = R.string.username)
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    isError = showError && username.isEmpty()
                )
            }

            item {
                // Password Input Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(
                        text = stringResource(id = R.string.password)
                    ) },
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
                                contentDescription = if (isPasswordVisible) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)
                            )
                        }
                    },
                    isError = showError && password.isEmpty()
                )
            }

            item {
                // Confirm Password Input Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(
                        text = stringResource(id = R.string.confirm_password)
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            val icon = if (isConfirmPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = if (isConfirmPasswordVisible) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)
                            )
                        }
                    },
                    isError = showError && confirmPassword.isEmpty()
                )
            }

            item {
                // Error Message
                if (showErrorRegister) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            item{
                if (showSuccessfulRegister) {
                showErrorRegister = false
                showError = false
                Text(
                    text = stringResource(id = R.string.successful_registration),
                    color = Color.Green,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Register Button
                Button(
                    onClick = {
                        registerViewModel.viewModelScope.launch {
                            val isRegistered = registerViewModel.registerUser(username=username, password = password, email = email)
                            if (isRegistered) {
                                showSuccessfulRegister = true
                                delay(1000)
                                registerViewModel.saveLoggedInUser(email)
                                homeViewModel.updateViewModel()
                                myProfileViewModel.updateViewModel()
                                navController.navigate(GymAppScreen.Home.name)
                            } else {
                                showErrorRegister = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.register),
                        fontSize = 16.sp)
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Already have an account
                Row {
                    Text(
                        text = stringResource(id = R.string.no_account),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(id = R.string.login),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { navController.navigate(GymAppScreen.Login.name) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    GymAppDemoTheme {
    }
}
