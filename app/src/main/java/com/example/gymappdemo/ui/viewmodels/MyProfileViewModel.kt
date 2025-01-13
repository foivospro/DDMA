package com.example.gymappdemo.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.preferences.ThemePreferences
import com.example.gymappdemo.data.entities.User

import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.ui.theme.AppThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MyProfileViewModel(
    private val userRepository: UserRepository,
    private val themePreferences: ThemePreferences
) : ViewModel() {

    private val _selectedAccentColor = MutableStateFlow(themePreferences.getTheme())
    val selectedAccentColor: StateFlow<AppThemeType> = _selectedAccentColor


    private val _isDarkModeEnabled = MutableStateFlow(false)
    val isDarkModeEnabled: StateFlow<Boolean> = _isDarkModeEnabled



    fun toggleDarkMode(enabled: Boolean) {
        _isDarkModeEnabled.value = enabled
    }

    fun updateTheme(themeType: AppThemeType) {
        _selectedAccentColor.value = themeType
    }
    fun saveThemeChanges() {
        viewModelScope.launch {
            themePreferences.saveTheme(_selectedAccentColor.value)
            themePreferences.saveDarkModeEnabled(_isDarkModeEnabled.value) // αν υλοποιήσεις κάτι αντίστοιχο
        }
    }

    fun getAvailableThemes(): List<AppThemeType> {
        return listOf(
            AppThemeType.DEFAULT,
            AppThemeType.BLUE,
            AppThemeType.GREEN,
            AppThemeType.RED
        )
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> = _username

    private val _profilePictureUri = MutableStateFlow<Uri?>(null)
    val profilePictureUri: StateFlow<Uri?> = _profilePictureUri

    fun updateViewModel() {
        viewModelScope.launch {
            fetchLoggedInUser()
            loadProfilePicture(_user.value?.id ?: 0)
        }
    }

    private suspend fun fetchLoggedInUser() {
        val email = userRepository.getLoggedInUserEmail()

        val user = if (email != null && email != "Guest") {
            userRepository.getUserByEmail(email)
        } else {
            null
        }
        _user.value = user
        _username.value = user?.name ?: "Guest"
    }

    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            userRepository.updateUser(updatedUser)
            _user.value = updatedUser
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.clearLoggedInUser()
            _user.value = null
            _username.value = "Guest"
            _profilePictureUri.value = null
        }
    }

    fun loadProfilePicture(userId: Int) {
        viewModelScope.launch {
            _profilePictureUri.value = userRepository.getUserProfilePictureUri(userId)
        }
    }

    fun changeUri(uri: Uri?) {
        _profilePictureUri.value = uri
    }
}
