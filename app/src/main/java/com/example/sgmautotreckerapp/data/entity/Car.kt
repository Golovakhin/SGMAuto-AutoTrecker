package com.example.sgmautotreckerapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Year

@Entity(tableName = "users")
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val mark: String,
    val model: String,
    val generation: String,
    val startYear: Int,
    val endYear: Int,

    val imageURl: String? = null
)
