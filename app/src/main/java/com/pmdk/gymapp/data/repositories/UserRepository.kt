package com.pmdk.gymapp.data.repositories

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.pmdk.gymapp.data.dao.UserDao
import com.pmdk.gymapp.data.entities.User

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

    suspend fun getUserProfilePicture(userId: Int): Uri? {
        val user = userDao.getUserById(userId)
        return user?.profilePicture
    }

    // Save the logged-in user's email to preferences
    fun saveLoggedInUserEmail(email: String) {
        sharedPreferences.edit().putString("logged_in_user_email", email).apply()
    }

    // Retrieve the logged-in user's ID from preferences
    suspend fun getLoggedInUserId(): Int? {
        val email = getLoggedInUserEmail()
        return email?.let {
            userDao.getUserByEmail(it)?.id
        }
    }

    suspend fun getUserProfilePictureUri(userId: Int): Uri? {
        // Replace with actual logic to get the profile picture URI from the database
        val user = userDao.getUserById(userId)
        return user?.profilePicture // Assuming you have the URI field in the User entity
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
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDao: UserDao, context: Context): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(userDao, context).also { instance = it }
            }
        }
    }
}
