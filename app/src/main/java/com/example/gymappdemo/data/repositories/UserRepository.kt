package com.example.gymappdemo.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.gymappdemo.data.dao.UserDao
import com.example.gymappdemo.data.entities.User

class UserRepository(private val userDao: UserDao, private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUser(userId: Int): User? {
        return userDao.getUserById(userId)
    }
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun registerUser(user: User): Boolean {
        return if (userDao.doesUserExist(user.email)) {
            false // Email already exists
        } else {
            userDao.registerUser(user)
            saveLoggedInUserEmail(user.email)
            true
        }
    }

    suspend fun loginUser(email: String, password: String): User? {
        val user = userDao.loginUser(email, password)
        if (user != null) {
            saveLoggedInUserEmail(email) // Save logged-in user email for session
        }
        return user
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    // Save the logged-in user's email to preferences
    fun saveLoggedInUserEmail(email: String) {
        sharedPreferences.edit().putString("logged_in_user_email", email).apply()
    }

    // Retrieve the logged-in user's email from preferences
    fun getLoggedInUserEmail(): String? {
        return sharedPreferences.getString("logged_in_user_email", "Guest")
    }

    // Check if a user is logged in
    fun isUserLoggedIn(): Boolean {
        return getLoggedInUserEmail() != null
    }

    // Clear logged-in user preferences (e.g., on logout)
    fun clearLoggedInUser() {
        sharedPreferences.edit().remove("logged_in_user_email").apply()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDao: UserDao, context: Context): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(userDao, context).also { instance = it }
            }
        }

        fun insertUser(user: User) {

        }
    }
}
