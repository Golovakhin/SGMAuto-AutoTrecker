package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.UserDao
import com.example.sgmautotreckerapp.data.entity.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun register(user: User): Long = userDao.insert(user)

    suspend fun login(email: String): User? = userDao.getUserByEmail(email)

    suspend fun isEmailExists(email: String): Boolean = userDao.isEmailExists(email) > 0

    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)

    suspend fun updateUserName(userId: Int, userName: String) = userDao.updateUserName(userId, userName)

    suspend fun updateUserPassword(userId: Int, newPassword: String) = userDao.updateUserPassword(userId, newPassword)

    suspend fun updateUser(userId: Int, userName: String, phone: String, tgId: String) = 
        userDao.updateUser(userId, userName, phone, tgId)
}
