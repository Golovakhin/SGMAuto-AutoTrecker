package com.example.sgmautotreckerapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sgmautotreckerapp.data.dao.*
import com.example.sgmautotreckerapp.data.entity.*
import com.example.sgmautotreckerapp.data.DateConvertor
import com.example.sgmautotreckerapp.data.DatabaseInitializer
@Database(
    entities = [Car::class, User::class, UserCar::class, Expense::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConvertor::class)
abstract class CarDatabase : RoomDatabase() {

    // DAO интерфейсы
    abstract fun carDao(): CarDao
    abstract fun userDao(): UserDao
    abstract fun userCarDao(): UserCarDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null


        fun getDatabase(context: Context): CarDatabase {
            return INSTANCE ?: synchronized(this) {
                // Создаем базу
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "car_app.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                // Заполняем начальными данными
                initializeDatabase(instance)

                INSTANCE = instance
                instance
            }
        }


        private fun initializeDatabase(database: CarDatabase) {
            DatabaseInitializer.initialize(
                carDao = database.carDao(),
                userDao = database.userDao()
            )
        }
    }
}