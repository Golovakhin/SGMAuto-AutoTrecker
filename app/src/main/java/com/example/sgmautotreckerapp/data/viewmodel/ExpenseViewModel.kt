package com.example.sgmautotreckerapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgmautotreckerapp.data.entity.Expense
import com.example.sgmautotreckerapp.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _userExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val userExpenses: StateFlow<List<Expense>> = _userExpenses.asStateFlow()

    private val _carExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val carExpenses: StateFlow<List<Expense>> = _carExpenses.asStateFlow()

    private val _selectedExpense = MutableStateFlow<Expense?>(null)
    val selectedExpense: StateFlow<Expense?> = _selectedExpense.asStateFlow()

    private val _state = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val state: StateFlow<ExpenseState> = _state.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    private val _selectedYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    // 0..11
    private val _selectedMonth = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH))
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    private var loadUserExpensesJob: Job? = null

    fun loadUserExpenses(userId: Int) {
        loadUserExpensesJob?.cancel()
        loadUserExpensesJob = viewModelScope.launch {
            _state.value = ExpenseState.Loading
            try {
                expenseRepository.getUserExpenses(userId).collectLatest { expenses ->
                    _userExpenses.value = expenses
                    calculateTotalAmount(expenses)
                    _state.value = ExpenseState.Success
                }
            } catch (e: Exception) {
                _state.value = ExpenseState.Error("Ошибка загрузки расходов: ${e.message}")
            }
        }
    }

    fun loadUserExpensesForMonth(userId: Int, year: Int, month: Int) {
        _selectedYear.value = year
        _selectedMonth.value = month

        val (fromMillis, toMillis) = monthRangeMillis(year, month)

        loadUserExpensesJob?.cancel()
        loadUserExpensesJob = viewModelScope.launch {
            _state.value = ExpenseState.Loading
            try {
                expenseRepository.getUserExpensesBetween(userId, fromMillis, toMillis)
                    .collectLatest { expenses ->
                        _userExpenses.value = expenses
                        calculateTotalAmount(expenses)
                        _state.value = ExpenseState.Success
                    }
            } catch (e: Exception) {
                _state.value = ExpenseState.Error("Ошибка загрузки расходов: ${e.message}")
            }
        }
    }

    fun shiftMonth(userId: Int, delta: Int) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, _selectedYear.value)
            set(Calendar.MONTH, _selectedMonth.value)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        cal.add(Calendar.MONTH, delta)
        loadUserExpensesForMonth(userId, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
    }

    private fun monthRangeMillis(year: Int, month: Int): Pair<Long, Long> {
        val from = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val to = (from.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
        return from.timeInMillis to to.timeInMillis
    }

    fun loadCarExpenses(userCarId: Int) {
        viewModelScope.launch {
            _state.value = ExpenseState.Loading
            try {
                expenseRepository.getCarExpenses(userCarId).collectLatest { expenses ->
                    _carExpenses.value = expenses
                    calculateTotalAmount(expenses)
                    _state.value = ExpenseState.Success
                }
            } catch (e: Exception) {
                _state.value = ExpenseState.Error("Ошибка загрузки расходов: ${e.message}")
            }
        }
    }

    suspend fun addExpense(expense: Expense): Long {
        return try {
            val id = expenseRepository.addExpense(expense)

            if (expense.userCarId != 0) {
                loadCarExpenses(expense.userCarId)
            } else {
                loadUserExpenses(expense.userId)
            }

            id
        } catch (e: Exception) {
            throw Exception("Ошибка добавления расхода: ${e.message}")
        }
    }

    private fun calculateTotalAmount(expenses: List<Expense>) {
        val total = expenses.sumOf { it.amount }
        _totalAmount.value = total
    }

    fun getExpensesByType(expenseType: String): List<Expense> {
        return when {
            _carExpenses.value.isNotEmpty() -> _carExpenses.value.filter { it.expenseType == expenseType }
            _userExpenses.value.isNotEmpty() -> _userExpenses.value.filter { it.expenseType == expenseType }
            else -> emptyList()
        }
    }

    fun getExpensesForPeriod(startDate: String, endDate: String): List<Expense> {
        // Здесь можно реализовать фильтрацию по дате
        // Для упрощения возвращаем все расходы
        return when {
            _carExpenses.value.isNotEmpty() -> _carExpenses.value
            _userExpenses.value.isNotEmpty() -> _userExpenses.value
            else -> emptyList()
        }
    }

    fun clearExpenses() {
        _userExpenses.value = emptyList()
        _carExpenses.value = emptyList()
        _totalAmount.value = 0.0
    }

    sealed class ExpenseState {
        object Idle : ExpenseState()
        object Loading : ExpenseState()
        object Success : ExpenseState()
        data class Error(val message: String) : ExpenseState()
    }
}