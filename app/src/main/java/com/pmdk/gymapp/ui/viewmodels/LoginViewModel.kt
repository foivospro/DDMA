package com.pmdk.gymapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.pmdk.gymapp.data.entities.User
import com.pmdk.gymapp.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    // State to represent the login process
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    // Login function
    suspend fun login(email: String, password: String, language: String): User? {
        if (email.isBlank() || password.isBlank()) {
            if (language == "el")
                _loginState.value =
                    LoginState.Error("Το email και ο κωδικός δεν πρέπει να είναι κενά!")
            else
                _loginState.value = LoginState.Error("Email and password must not be empty!")
            return null
        }

        return try {
            _loginState.value = LoginState.Loading
            val user = withContext(Dispatchers.IO) {
                userRepository.loginUser(email, password)
            }
            if (user != null) {
                saveLoggedInUser(email) // Save user to session or shared preferences
                _loginState.value = LoginState.Success(user)
                user
            } else {
                if (language == "el")
                    _loginState.value = LoginState.Error("Λανθασμένο email ή κωδικός!")
                else
                    _loginState.value = LoginState.Error("Wrong email or password!")
                null
            }
        } catch (e: Exception) {
            _loginState.value = LoginState.Error("An error occurred: ${e.message}")
            null
        }
    }


    private fun saveLoggedInUser(email: String) {
        userRepository.saveLoggedInUserEmail(email)
    }

}

// State class to represent login status
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
