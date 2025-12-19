package com.example.sgmautotreckerapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sgmautotreckerapp.data.entity.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense): Long

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    fun getUserExpenses(userId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE userCarId = :userCarId ORDER BY date DESC")
    fun getCarExpenses(userCarId: Int): Flow<List<Expense>>

    @Query("DELETE FROM expenses WHERE userCarId = :userCarId")
    suspend fun deleteExpensesByUserCarId(userCarId: Int)
}