package com.example.sgmautotreckerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sgmautotreckerapp.data.dao.CarDao
import com.example.sgmautotreckerapp.data.dao.ExpenseDao
import com.example.sgmautotreckerapp.data.dao.UserCarDao
import com.example.sgmautotreckerapp.data.dao.UserDao
import com.example.sgmautotreckerapp.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Car::class, User::class, UserCar::class, Expense::class],
    version = 1,
    exportSchema = false)
@TypeConverters(DateConvertor::class)
abstract class CarDatabase: RoomDatabase(){
    abstract fun carDao(): CarDao
    abstract fun userDao(): UserDao
    abstract fun userCarDao(): UserCarDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getDatabase(context: Context): CarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "car_app.db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            getDatabase(context).also { database ->
                                insertInitialData(database)
                            }
                        }
                    }
                })
                    .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                    instance
            }
        }

        private suspend fun insertInitialData(database: CarDatabase) {
            insertInitialCars(database.carDao())
            insertInitialUser(database.userDao())
        }

        private suspend fun insertInitialCars(carDao: CarDao){
            val initialCars = listOf(
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "F",
                    startYear = 1991,
                    endYear = 2002,
                    imageURl = "https://drive.google.com/file/d/1aZb0NjIYxCixM8v1Nz_IscvWXYBeTaJ8/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "G",
                    startYear = 1998,
                    endYear = 2009,
                    imageURl = "https://drive.google.com/file/d/1m9PYnz7sByZzJoQYgOFzEZrJ850-2j7W/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "G",
                    startYear = 1998,
                    endYear = 2009,
                    imageURl = "https://drive.google.com/file/d/1m9PYnz7sByZzJoQYgOFzEZrJ850-2j7W/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "H",
                    startYear = 2004,
                    endYear = 2007,
                    imageURl = "https://drive.google.com/file/d/1oSdIjpSbcg54M8bb_g10V-T5wt-dcL6S/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "H Рестайлинг",
                    startYear = 2006,
                    endYear = 2014,
                    imageURl = "https://drive.google.com/file/d/11VTZeSAfH-2wD6JVuP1bQfIzXuQV8Akg/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "J",
                    startYear = 2009,
                    endYear = 2012,
                    imageURl = "https://drive.google.com/file/d/1mWViRvw00qAU6ntAclxUQ2TrM60FgfM-/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "J Рестайлинг",
                    startYear = 2011,
                    endYear = 2018,
                    imageURl = "https://drive.google.com/file/d/1WPQ4_8pJbDuyuqn_fvZSSZ926TDg22KN/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "K",
                    startYear = 2015,
                    endYear = 2019,
                    imageURl = "https://drive.google.com/file/d/11PuCzh7t5jUCBkfD-_pk5TAxDKPl7fO6/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "K Рестайлинг",
                    startYear = 2019,
                    endYear = 2021,
                    imageURl = "https://drive.google.com/file/d/1vpNy7gUqQYZiJnQr9O8vHmPADRQkQW5D/view?usp=drive_link"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "L",
                    startYear = 2021,
                    endYear = 2025,
                    imageURl = "https://drive.google.com/file/d/1je0yuqme2Rsr_rnUdxaUEMoQRDkxQ7l2/view?usp=drive_link"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "I",
                    startYear = 1972,
                    endYear = 1979,imageURl = "https://drive.google.com/file/d/1RnP6U3xY7uaUXGlzOYqoVFjTIbtyJ85w/view?usp=drive_link"
                ),


                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "II",
                    startYear = 1979,
                    endYear = 1983,
                    imageURl = "https://drive.google.com/file/d/1oYqGPzdkfF_x8HCy0GUWLTt6fd1Y_jkI/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "III",
                    startYear = 1983,
                    endYear = 1987,
                    imageURl = "https://drive.google.com/file/d/1YKGuJ-gEON16yvNeWUzbFXapQyM5eV_Q/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "IV",
                    startYear = 1987,
                    endYear = 1996,
                    imageURl = "https://drive.google.com/file/d/1jDvztQbfGDCEihOxJliIRM6QW30d3bQi/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "V",
                    startYear = 1991,
                    endYear = 1997,
                    imageURl = "https://drive.google.com/file/d/1mp07vlff5MQx5gzqNR0FUyRpu46TzN79/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VI",
                    startYear = 1995,
                    endYear = 1998,
                    imageURl = "https://drive.google.com/file/d/1vGSvf8SndPo-pXVVEGYXfjhYaseK1OaH/view?usp=drive_link"
                ),


                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VI рестайлинг",
                    startYear = 1998,
                    endYear = 2002,
                    imageURl = "https://drive.google.com/file/d/1vXs8ydppygl7XNw7QuOvfWQBiGPT0xYz/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VII",
                    startYear = 2000,
                    endYear = 2003,
                    imageURl = "https://drive.google.com/file/d/1JDThlmOMs6vcTdGRDWFoDBiThZwJLfuG/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VII рестайлинг",
                    startYear = 2003,
                    endYear = 2006,
                    imageURl = "https://drive.google.com/file/d/1629MSS4KylsfJcBnvFYHsfENGq2VpDbv/view?usp=drive_link"
                ),


                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VIII",
                    startYear = 2005,
                    endYear = 2009,
                    imageURl = "https://drive.google.com/file/d/1KC08I2LmbvdvCpD397Y6M7v_LHK-zKSD/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VIII рестайлинг",
                    startYear = 2008,
                    endYear = 2012,
                    imageURl = "https://drive.google.com/file/d/1UjnKKvW6b3kr3__0JNO5ado8qamPuKIU/view?usp=drive_link"
                ),Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "IX",
                    startYear = 2011,
                    endYear = 2015,
                    imageURl = "https://drive.google.com/file/d/1SKcUvZMZt8qMCAKG5l-NlCw8qqlSPu79/view?usp=drive_link"
                ),


                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "IX рестайлинг",
                    startYear = 2013,
                    endYear = 2017,
                    imageURl = "https://drive.google.com/file/d/1VkIfnhDuEBvm-MPYF9rB5ePMNgz07-Ru/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "X",
                    startYear = 2015,
                    endYear = 2022,
                    imageURl = "https://drive.google.com/file/d/1pXmD8inT742pVr9Bi1FvQ_3EZlazdLem/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "XI",
                    startYear = 2021,
                    endYear = 2025,
                    imageURl = "https://drive.google.com/file/d/1MsERGOFy2h0MbT5Yne7_wCQ4beAD6wu-/view?usp=drive_link"
                ),

                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "XI рестайлинг",
                    startYear = 2024,
                    endYear = 2025,
                    imageURl = "https://drive.google.com/file/d/1WJfx8_M66BJmflfcKIQdkz_w1DhW3IEz/view?usp=drive_link"
                )
            )
            carDao.insertAll(initialCars)
        }
        private suspend fun insertInitialUser(userDao: UserDao){
            val testUser = User(
                email = "test@example.com",
                password = "password123", // В реальном приложении хэшируйте пароль!
                userName = "Тест",
                phone = "+79991234567",
                tgID = "@teftelh"
            )
            userDao.insert(testUser)
        }
    }
}