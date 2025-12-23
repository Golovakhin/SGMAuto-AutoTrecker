package com.example.sgmautotreckerapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sgmautotreckerapp.data.entity.UserCar
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCarDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(userCar: UserCar): Long

    @Query("SELECT * FROM user_cars WHERE userId = :userId")
    fun getUserCars(userId: Int): Flow<List<UserCar>>

    @Query("SELECT * FROM user_cars WHERE id = :userCarId")
    suspend fun getUserCarById(userCarId: Int): UserCar?

    @Query("DELETE FROM user_cars WHERE id = :userCarId")
    suspend fun deleteUserCar(userCarId: Int)

    // Получить все машины пользователей (для синхронизации)
    @Query("SELECT * FROM user_cars")
    suspend fun getAllUserCars(): List<UserCar>
}
