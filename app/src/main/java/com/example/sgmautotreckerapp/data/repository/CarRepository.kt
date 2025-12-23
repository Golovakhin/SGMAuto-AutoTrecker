package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.CarDao
import com.example.sgmautotreckerapp.data.entity.Car
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carDao: CarDao
) {
    suspend fun insertAllCars(cars: List<Car>) = carDao.insertAll(cars)

    fun getAllCars(): Flow<List<Car>> = carDao.getAllCars()

    fun getAllBrands(): Flow<List<String>> = carDao.getAllBrands()

    fun getModelsByBrand(mark: String): Flow<List<Car>> = carDao.getModelsByBrand(mark)

    suspend fun getCarById(carId: Int): Car? = carDao.getCarById(carId)

}
