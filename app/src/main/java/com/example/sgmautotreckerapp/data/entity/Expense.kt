package com.example.sgmautotreckerapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,


            ),
        ForeignKey(
            entity = Car::class,
            parentColumns = ["id"],
            childColumns = ["userCarId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["userCarId"]),
        Index(value = ["date"])
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: Int,
    val userCarId: Int,

    val expenseType: String,
    val amount: Double,
    val date: Date,
    val mileage: Int,

    val description: String,

    val fuelVolume: Double? = null,
    val fuelPricePerLiter: Double? = null
)
