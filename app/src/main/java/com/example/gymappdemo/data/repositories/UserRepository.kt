package com.example.gymappdemo.data.repositories

import com.example.gymappdemo.data.dao.UserDao
import com.example.gymappdemo.data.entities.User

class UserRepository(private val userDao: UserDao) {

    suspend fun getUser(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDao: UserDao): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(userDao).also { instance = it }
            }
        }

        fun insertUser(user: User) {

        }
    }
}
