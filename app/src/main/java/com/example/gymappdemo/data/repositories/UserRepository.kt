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
}
