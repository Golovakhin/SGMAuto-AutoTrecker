package com.example.sgmautotreckerapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sgmautotreckerapp.data.DateConvertor
import com.example.sgmautotreckerapp.data.dao.CarDao
import com.example.sgmautotreckerapp.data.dao.ExpenseDao
import com.example.sgmautotreckerapp.data.dao.UserCarDao
import com.example.sgmautotreckerapp.data.dao.UserDao
import com.example.sgmautotreckerapp.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Car::class, User::class, UserCar::class, Expense::class],
    version = 5,
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
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Вставляем данные при создании базы
                        CoroutineScope(Dispatchers.IO).launch {
                            // Небольшая задержка для завершения создания базы
                            kotlinx.coroutines.delay(100)
                            INSTANCE?.let { database ->
                                insertInitialData(database)
                            }
                        }
                    }
                })
                    .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                    // Вставляем данные при первом получении базы (если их еще нет)
                    CoroutineScope(Dispatchers.IO).launch {
                        insertInitialData(instance)
                    }
                    instance
            }
        }

        private suspend fun insertInitialData(database: CarDatabase) {
            val carDao = database.carDao()
            // Проверяем, есть ли уже данные в базе
            val count = carDao.getCarsCount()
            if (count == 0) {
                insertInitialCars(carDao)
            }
            val userDao = database.userDao()
            // Проверяем, есть ли уже пользователи
            if (userDao.getUserByEmail("test@example.com") == null) {
                insertInitialUser(userDao)
            }
        }

        private suspend fun insertInitialCars(carDao: CarDao){
            val initialCars = listOf(
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "F",
                    startYear = 1991,
                    endYear = 2002,
                    imageURl = "https://drive.google.com/uc?export=view&id=1aZb0NjIYxCixM8v1Nz_IscvWXYBeTaJ8"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "G",
                    startYear = 1998,
                    endYear = 2009,
                    imageURl = "https://drive.google.com/uc?export=view&id=1m9PYnz7sByZzJoQYgOFzEZrJ850-2j7W"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "G",
                    startYear = 1998,
                    endYear = 2009,
                    imageURl = "https://drive.google.com/uc?export=view&id=1m9PYnz7sByZzJoQYgOFzEZrJ850-2j7W"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "H",
                    startYear = 2004,
                    endYear = 2007,
                    imageURl = "https://drive.google.com/uc?export=view&id=1oSdIjpSbcg54M8bb_g10V-T5wt-dcL6S"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "H Рестайлинг",
                    startYear = 2006,
                    endYear = 2014,
                    imageURl = "https://drive.google.com/uc?export=view&id=11VTZeSAfH-2wD6JVuP1bQfIzXuQV8Akg"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "J",
                    startYear = 2009,
                    endYear = 2012,
                    imageURl = "https://drive.google.com/uc?export=view&id=12V6WHwFChkfBeupzge5xb9Syesd7Hn9F6-"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "J Рестайлинг",
                    startYear = 2011,
                    endYear = 2018,
                    imageURl = "https://drive.google.com/uc?export=view&id=1WPQ4_8pJbDuyuqn_fvZSSZ926TDg22KN"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "K",
                    startYear = 2015,
                    endYear = 2019,
                    imageURl = "https://drive.google.com/uc?export=view&id=11PuCzh7t5jUCBkfD-_pk5TAxDKPl7fO6"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "K Рестайлинг",
                    startYear = 2019,
                    endYear = 2021,
                    imageURl = "https://drive.google.com/uc?export=view&id=1vpNy7gUqQYZiJnQr9O8vHmPADRQkQW5D"
                ),
                Car(
                    mark = "Opel",
                    model = "Astra",
                    generation = "L",
                    startYear = 2021,
                    endYear = 2025,
                    imageURl = "https://drive.google.com/uc?export=view&id=1je0yuqme2Rsr_rnUdxaUEMoQRDkxQ7l2"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "I",
                    startYear = 1972,
                    endYear = 1979,
                    imageURl = "https://drive.google.com/uc?export=view&id=1RnP6U3xY7uaUXGlzOYqoVFjTIbtyJ85w"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "II",
                    startYear = 1979,
                    endYear = 1983,
                    imageURl = "https://drive.google.com/uc?export=view&id=1oYqGPzdkfF_x8HCy0GUWLTt6fd1Y_jkI"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "III",
                    startYear = 1983,
                    endYear = 1987,
                    imageURl = "https://drive.google.com/uc?export=view&id=1YKGuJ-gEON16yvNeWUzbFXapQyM5eV_Q"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "IV",
                    startYear = 1987,
                    endYear = 1996,
                    imageURl = "https://drive.google.com/uc?export=view&id=1jDvztQbfGDCEihOxJliIRM6QW30d3bQi"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "V",
                    startYear = 1991,
                    endYear = 1997,
                    imageURl = "https://drive.google.com/uc?export=view&id=12V6WHwFChkfBeupzge5xb9Syesd7Hn9F6"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VI",
                    startYear = 1995,
                    endYear = 1998,
                    imageURl = "https://drive.google.com/uc?export=view&id=1vGSvf8SndPo-pXVVEGYXfjhYaseK1OaH"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VI рестайлинг",
                    startYear = 1998,
                    endYear = 2002,
                    imageURl = "https://drive.google.com/uc?export=view&id=1vXs8ydppygl7XNw7QuOvfWQBiGPT0xYz"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VII",
                    startYear = 2000,
                    endYear = 2003,
                    imageURl = "https://drive.google.com/uc?export=view&id=1JDThlmOMs6vcTdGRDWFoDBiThZwJLfuG"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VII рестайлинг",
                    startYear = 2003,
                    endYear = 2006,
                    imageURl = "https://drive.google.com/uc?export=view&id=12V6WHwFChkfBeupzge5xb9Syesd7Hn9F6"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VIII",
                    startYear = 2005,
                    endYear = 2009,
                    imageURl = "https://drive.google.com/uc?export=view&id=1KC08I2LmbvdvCpD397Y6M7v_LHK-zKSD"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "VIII рестайлинг",
                    startYear = 2008,
                    endYear = 2012,
                    imageURl = "https://drive.google.com/uc?export=view&id=1UjnKKvW6b3kr3__0JNO5ado8qamPuKIU"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "IX",
                    startYear = 2011,
                    endYear = 2015,
                    imageURl = "https://drive.google.com/uc?export=view&id=1SKcUvZMZt8qMCAKG5l-NlCw8qqlSPu79"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "IX рестайлинг",
                    startYear = 2013,
                    endYear = 2017,
                    imageURl = "https://drive.google.com/uc?export=view&id=1VkIfnhDuEBvm-MPYF9rB5ePMNgz07-Ru"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "X",
                    startYear = 2015,
                    endYear = 2022,
                    imageURl = "https://drive.google.com/uc?export=view&id=1pXmD8inT742pVr9Bi1FvQ_3EZlazdLem"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "XI",
                    startYear = 2021,
                    endYear = 2025,
                    imageURl = "https://drive.google.com/uc?export=view&id=1MsERGOFy2h0MbT5Yne7_wCQ4beAD6wu-"
                ),
                Car(
                    mark = "Honda",
                    model = "Civic",
                    generation = "XI рестайлинг",
                    startYear = 2024,
                    endYear = 2025,
                    imageURl = "https://drive.google.com/uc?export=view&id=1WJfx8_M66BJmflfcKIQdkz_w1DhW3IEz"
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