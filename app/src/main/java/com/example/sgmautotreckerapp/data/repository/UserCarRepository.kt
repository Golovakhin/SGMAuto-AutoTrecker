package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.UserCarDao
import com.example.sgmautotreckerapp.data.entity.UserCar
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserCarRepository @Inject constructor(
    private val userCarDao: UserCarDao
) {
    suspend fun addUserCar(userCar: UserCar): Long = userCarDao.insert(userCar)

    fun getUserCars(userId: Int): Flow<List<UserCar>> = userCarDao.getUserCars(userId)

    suspend fun getUserCarById(userCarId: Int): UserCar? = userCarDao.getUserCarById(userCarId)

    suspend fun deleteUserCar(userCarId: Int) = userCarDao.deleteUserCar(userCarId)

}
