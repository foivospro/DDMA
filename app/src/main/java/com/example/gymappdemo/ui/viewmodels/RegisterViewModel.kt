package com.example.gymappdemo.ui.viewmodels

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
    suspend fun registerUser(username: String, email: String, password: String): Boolean {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Συμπληρώστε όλα τα πεδία"
            return false

        }
        val user = User(name = username, email = email, passwordHash = password, age = null, weight = null, height = null)
        val isRegistered = withContext(Dispatchers.IO) {
            userRepository.registerUser(user = user)
        }
        if (!isRegistered) {
            _errorMessage.value = "Το email χρησιμοποιείται ήδη"
        }
        return isRegistered
    }
     fun saveLoggedInUser(email: String) {
        userRepository.saveLoggedInUserEmail(email)
    }

}