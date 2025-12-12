package com.example.sgmautotreckerapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sgmautotreckerapp.data.entity.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cars: List<Car>)


    @Query("SELECT * FROM cars ORDER BY mark, model")
    fun getAllCars(): Flow<List<Car>>

    @Query("SELECT DISTINCT mark FROM cars ORDER BY mark")
    fun getAllBrands(): Flow<List<String>>

    @Query("SELECT * FROM cars WHERE mark = :mark ORDER BY model")
    fun getModelsByBrand(mark: String): Flow<List<Car>>

    @Query("SELECT * FROM cars WHERE id = :carId")
    suspend fun getCarById(carId: Int): Car?

    @Query("SELECT COUNT(*) FROM cars")
    suspend fun getCarsCount(): Int
}