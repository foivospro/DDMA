package com.example.gymappdemo.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gymappdemo.data.entities.User
import com.example.gymappdemo.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    suspend fun registerUser(username: String, email: String, password: String, confirmedPassword: String, context: Context): Boolean {
        val language = context.resources.configuration.locales[0].language
        // Check if any field is blank
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            if(language == "el")
                _errorMessage.value = "Συμπληρώστε όλα τα πεδία"
            else
                _errorMessage.value = "Fill in all fields"
            return false
        }
        //Check if passwords match
        if (password != confirmedPassword) {
            if(language == "el")
                _errorMessage.value = "Οι κωδικοί δεν ταιριάζουν"
            else
                _errorMessage.value = "Passwords do not match"
            return false
        }

        // Check if email is valid using a regex
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        if (!email.matches(emailRegex)) {
            if (language == "el") {
                _errorMessage.value = "Εισάγετε έγκυρη διεύθυνση email"
            } else
                _errorMessage.value = "Enter a valid email address"
            return false
        }
        // Check if username is too short
        if (username.length < 3) {
            if(language == "el") {
                _errorMessage.value = "Το όνομα χρήστη πρέπει να έχει τουλάχιστον 3 χαρακτήρες"
            } else {
                _errorMessage.value = "Username must be at least 3 characters long"
            }
            return false
        }

        // Check if password meets complexity requirements
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d).{8,}\$".toRegex()
        if (!password.matches(passwordRegex)) {
            if(language == "el") {
                _errorMessage.value =
                    "Ο κωδικός πρέπει να περιέχει τουλάχιστον 8 χαρακτήρες, ένα γράμμα και ένα αριθμό"
            }else
                _errorMessage.value = "Password must be minimum eight characters, at least one letter and one number"
            return false
        }

        // Create user object
        val user = User(name = username, email = email, passwordHash = password, age = null, weight = null, height = null)

        // Attempt to register the user
        val isRegistered = withContext(Dispatchers.IO) {
            userRepository.registerUser(user = user)
        }
        // Check registration result
        if (!isRegistered) {
            if (language == "el")
                _errorMessage.value = "Το email χρησιμοποιείται ήδη"
            else
                _errorMessage.value = "Email is already in use"
        }

        return isRegistered
    }
     fun saveLoggedInUser(email: String) {
        userRepository.saveLoggedInUserEmail(email)
    }


}