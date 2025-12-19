package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.UserCarDao
import com.example.sgmautotreckerapp.data.entity.UserCar
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserCarRepository @Inject constructor(
    private val userCarDao: UserCarDao
) {
    // Добавление автомобиля пользователя
    suspend fun addUserCar(userCar: UserCar): Long = userCarDao.insert(userCar)

    // Получение всех автомобилей пользователя (Flow для наблюдения)
    fun getUserCars(userId: Int): Flow<List<UserCar>> = userCarDao.getUserCars(userId)

    // Получение конкретного автомобиля пользователя по ID
    suspend fun getUserCarById(userCarId: Int): UserCar? = userCarDao.getUserCarById(userCarId)

    // Удаление автомобиля пользователя по ID
    suspend fun deleteUserCar(userCarId: Int) = userCarDao.deleteUserCar(userCarId)

    // Методы для обновления (если добавите в DAO)
    // suspend fun updateUserCar(userCar: UserCar) = userCarDao.update(userCar)
}