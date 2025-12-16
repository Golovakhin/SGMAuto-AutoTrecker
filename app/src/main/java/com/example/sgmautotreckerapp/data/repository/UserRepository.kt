package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.UserDao
import com.example.sgmautotreckerapp.data.entity.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    // Регистрация нового пользователя
    suspend fun register(user: User): Long = userDao.insert(user)

    // Авторизация - получение пользователя по email
    suspend fun login(email: String): User? = userDao.getUserByEmail(email)

    // Проверка существования email
    suspend fun isEmailExists(email: String): Boolean = userDao.isEmailExists(email) > 0

    // Получение пользователя по ID
    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)

    // Обновление пользователя (если добавите update в DAO)
    // suspend fun updateUser(user: User) = userDao.update(user)
}