package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.CarDao
import com.example.sgmautotreckerapp.data.entity.Car
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carDao: CarDao
) {
    // Заполнение базы данных автомобилями (при первом запуске)
    suspend fun insertAllCars(cars: List<Car>) = carDao.insertAll(cars)

    // Получение всех автомобилей
    fun getAllCars(): Flow<List<Car>> = carDao.getAllCars()

    // Получение всех брендов (уникальных)
    fun getAllBrands(): Flow<List<String>> = carDao.getAllBrands()

    // Получение моделей по бренду
    fun getModelsByBrand(mark: String): Flow<List<Car>> = carDao.getModelsByBrand(mark)

    // Получение автомобиля по ID
    suspend fun getCarById(carId: Int): Car? = carDao.getCarById(carId)

    // Проверка наличия данных (для начального заполнения)
    // Если в DAO нет метода getCount, добавьте:
    // @Query("SELECT COUNT(*) FROM cars")
    // suspend fun getCount(): Int
    // suspend fun isEmpty(): Boolean = carDao.getCount() == 0
}