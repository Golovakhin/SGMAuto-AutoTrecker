package com.example.sgmautotreckerapp.data.repository

import com.example.sgmautotreckerapp.data.dao.ExpenseDao
import com.example.sgmautotreckerapp.data.entity.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    // Добавление расхода
    suspend fun addExpense(expense: Expense): Long = expenseDao.insert(expense)

    // Получение всех расходов пользователя
    fun getUserExpenses(userId: Int): Flow<List<Expense>> = expenseDao.getUserExpenses(userId)

    // Получение расходов по конкретному автомобилю
    fun getCarExpenses(userCarId: Int): Flow<List<Expense>> = expenseDao.getCarExpenses(userCarId)

    // Удаление всех расходов по автомобилю пользователя
    suspend fun deleteExpensesByUserCarId(userCarId: Int) = expenseDao.deleteExpensesByUserCarId(userCarId)

    // Методы для обновления и удаления (если добавите в DAO)
    // suspend fun updateExpense(expense: Expense) = expenseDao.update(expense)
    // suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)
    // suspend fun getExpenseById(id: Int): Expense? = expenseDao.getById(id)
}