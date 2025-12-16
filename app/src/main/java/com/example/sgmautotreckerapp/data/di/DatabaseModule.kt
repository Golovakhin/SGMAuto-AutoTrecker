package com.example.sgmautotreckerapp.data.di

import android.content.Context
import com.example.sgmautotreckerapp.data.database.CarDatabase
import com.example.sgmautotreckerapp.data.dao.*
import com.example.sgmautotreckerapp.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Provides
    @Singleton
    fun provideCarDatabase(@ApplicationContext context: Context): CarDatabase{
        return CarDatabase.getDatabase(context)
    }


    @Provides
    fun provideCarDao(database: CarDatabase): CarDao {
        return database.carDao()
    }

    @Provides
    fun provideUserDao(database: CarDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideUserCarDao(database: CarDatabase): UserCarDao {
        return database.userCarDao()
    }

    @Provides
    fun provideExpenseDao(database: CarDatabase): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    fun provideCarRepository(carDao: CarDao): CarRepository {
        return CarRepository(carDao)
    }

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    fun provideUserCarRepository(userCarDao: UserCarDao): UserCarRepository {
        return UserCarRepository(userCarDao)
    }

    @Provides
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepository(expenseDao)
    }

}