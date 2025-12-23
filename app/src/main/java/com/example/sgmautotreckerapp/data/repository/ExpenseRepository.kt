package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.ExpenseDao
import com.example.sgmautotreckerapp.data.entity.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    suspend fun addExpense(expense: Expense): Long = expenseDao.insert(expense)

    fun getUserExpenses(userId: Int): Flow<List<Expense>> = expenseDao.getUserExpenses(userId)

    fun getUserExpensesBetween(
        userId: Int,
        fromMillis: Long,
        toMillis: Long
    ): Flow<List<Expense>> = expenseDao.getUserExpensesBetween(userId, fromMillis, toMillis)

    fun getCarExpenses(userCarId: Int): Flow<List<Expense>> = expenseDao.getCarExpenses(userCarId)

    suspend fun deleteExpensesByUserCarId(userCarId: Int) = expenseDao.deleteExpensesByUserCarId(userCarId)

}
