package com.example.sgmautotreckerapp.data.sync

import com.example.sgmautotreckerapp.data.database.CarDatabase
import com.example.sgmautotreckerapp.data.sync.FullDumpDto
import com.example.sgmautotreckerapp.data.sync.toDto
import com.example.sgmautotreckerapp.data.sync.toEntity
import kotlinx.coroutines.flow.first
import androidx.room.withTransaction
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


private const val BASE_URL = "http://10.0.2.2:8001/"

interface SyncApi {

    @POST("sync/upload")
    suspend fun uploadDb(@Body dump: FullDumpDto)

    @GET("sync/download")
    suspend fun downloadDb(): FullDumpDto
}

object SyncNetwork {

    val api: SyncApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        retrofit.create(SyncApi::class.java)
    }
}

class SyncRepository(
    private val db: CarDatabase,
    private val api: SyncApi = SyncNetwork.api
) {




    suspend fun exportToServer() {
        val userDao = db.userDao()
        val carDao = db.carDao()
        val userCarDao = db.userCarDao()
        val expenseDao = db.expenseDao()

        val users = userDao.getAllUsers().map { it.toDto() }

        val cars = carDao.getAllCars().first().map { it.toDto() }

        val userCars = userCarDao.getAllUserCars().map { it.toDto() }

        val expenses = expenseDao.getAllExpenses().map { it.toDto() }

        val dump = FullDumpDto(
            users = users,
            cars = cars,
            user_cars = userCars,
            expenses = expenses
        )

        api.uploadDb(dump)
    }


    suspend fun importFromServer() {
        val dump = api.downloadDb()

        db.withTransaction {
            db.clearAllTables()

            val userDao = db.userDao()
            val carDao = db.carDao()
            val userCarDao = db.userCarDao()
            val expenseDao = db.expenseDao()

            dump.users.map { it.toEntity() }.forEach { userDao.insert(it) }
            carDao.insertAll(dump.cars.map { it.toEntity() })
            dump.user_cars.map { it.toEntity() }.forEach { userCarDao.insert(it) }
            dump.expenses.map { it.toEntity() }.forEach { expenseDao.insert(it) }
        }
    }
}
