package com.example.sgmautotreckerapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_cars",
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
            childColumns = ["carId"],
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["gosNomer"], unique = true)
    ]
    )
data class UserCar(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val userId: Int,
    val carId : Int,

    val gosNomer : String,
    val vin: String,
    val year: Int


)
