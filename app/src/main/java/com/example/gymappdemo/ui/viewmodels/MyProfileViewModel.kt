package com.example.gymappdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.User
import com.example.gymappdemo.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    // StateFlow to hold the user data
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // StateFlow to hold the username (optional if needed for separate use cases)
    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> = _username

    init {
        viewModelScope.launch {
            fetchLoggedInUser()
        }
    }

    private suspend fun fetchLoggedInUser() {
        // Fetch email from SharedPreferences using a background thread
        val email = withContext(Dispatchers.IO) {
            userRepository.getLoggedInUserEmail()
        }

        // Fetch user data based on email (or handle guest user)
        val user = if (email != null && email != "Guest") {
            withContext(Dispatchers.IO) {
                userRepository.getUserByEmail(email)
            }
        } else {
            null
        }

        // Update state on the main thread
        _user.value = user
        _username.value = user?.name ?: "Guest"
    }

    // Update user profile if needed
    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.updateUser(updatedUser)
            }
            _user.value = updatedUser // Update the state
        }
    }

    fun logout() {
        // Call UserRepository's method to clear the logged-in user data
        userRepository.clearLoggedInUser()
    }
}