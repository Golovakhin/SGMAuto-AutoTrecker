package com.example.sgmautotreckerapp.data.sync

import com.example.sgmautotreckerapp.data.entity.Car
import com.example.sgmautotreckerapp.data.entity.Expense
import com.example.sgmautotreckerapp.data.entity.User
import com.example.sgmautotreckerapp.data.entity.UserCar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserDto(
    val id: Int?,
    val email: String,
    val password: String,
    val userName: String,
    val phone: String,
    val tgID: String
)

data class CarDto(
    val id: Int?,
    val mark: String,
    val model: String,
    val generation: String,
    val startYear: Int,
    val endYear: Int,
    val imageURl: String?
)

data class UserCarDto(
    val id: Int?,
    val userId: Int,
    val carId: Int,
    val gosNomer: String,
    val vin: String,
    val year: Int
)

data class ExpenseDto(
    val id: Int?,
    val userId: Int,
    val userCarId: Int,
    val expenseType: String,
    val amount: Double,
    val date: String,
    val mileage: Int,
    val description: String,
    val fuelVolume: Double?,
    val fuelPricePerLiter: Double?
)

data class FullDumpDto(
    val users: List<UserDto>,
    val cars: List<CarDto>,
    val user_cars: List<UserCarDto>,
    val expenses: List<ExpenseDto>
)

private val syncDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

private fun Date.toSyncString(): String = syncDateFormat.format(this)

private fun String.toSyncDate(): Date = syncDateFormat.parse(this) ?: Date()

fun User.toDto(): UserDto = UserDto(
    id = id,
    email = email,
    password = password,
    userName = userName,
    phone = phone,
    tgID = tgID
)

fun Car.toDto(): CarDto = CarDto(
    id = id,
    mark = mark,
    model = model,
    generation = generation,
    startYear = startYear,
    endYear = endYear,
    imageURl = imageURl
)

fun UserCar.toDto(): UserCarDto = UserCarDto(
    id = id,
    userId = userId,
    carId = carId,
    gosNomer = gosNomer,
    vin = vin,
    year = year
)

fun Expense.toDto(): ExpenseDto = ExpenseDto(
    id = id,
    userId = userId,
    userCarId = userCarId,
    expenseType = expenseType,
    amount = amount,
    date = date.toSyncString(),
    mileage = mileage,
    description = description,
    fuelVolume = fuelVolume,
    fuelPricePerLiter = fuelPricePerLiter
)

fun UserDto.toEntity(): User = User(
    id = id,
    email = email,
    password = password,
    userName = userName,
    phone = phone,
    tgID = tgID
)

fun CarDto.toEntity(): Car = Car(
    id = id,
    mark = mark,
    model = model,
    generation = generation,
    startYear = startYear,
    endYear = endYear,
    imageURl = imageURl
)

fun UserCarDto.toEntity(): UserCar = UserCar(
    id = id,
    userId = userId,
    carId = carId,
    gosNomer = gosNomer,
    vin = vin,
    year = year
)

fun ExpenseDto.toEntity(): Expense = Expense(
    id = id ?: 0,
    userId = userId,
    userCarId = userCarId,
    expenseType = expenseType,
    amount = amount,
    date = date.toSyncDate(),
    mileage = mileage,
    description = description,
    fuelVolume = fuelVolume,
    fuelPricePerLiter = fuelPricePerLiter
)
