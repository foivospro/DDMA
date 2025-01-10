package com.example.gymappdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.entities.User
import com.example.gymappdemo.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyProfileViewModel(private val userDao: UserRepository) : ViewModel() {

    // StateFlow to hold the user data
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // Fetch user profile data
    fun fetchUserProfile(userId: Int) {
        viewModelScope.launch {
            val userData = userDao.getUser(userId)
            _user.value = userData
        }
    }

    // Update user profile (if needed)
    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            userDao.updateUser(updatedUser)
            _user.value = updatedUser // Update the state
        }
    }
}
