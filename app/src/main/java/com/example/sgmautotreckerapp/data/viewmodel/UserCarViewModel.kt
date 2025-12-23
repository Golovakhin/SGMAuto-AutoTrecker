package com.example.sgmautotreckerapp.data.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgmautotreckerapp.data.entity.UserCar
import com.example.sgmautotreckerapp.data.repository.ExpenseRepository
import com.example.sgmautotreckerapp.data.repository.UserCarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCarViewModel @Inject constructor(
    private val userCarRepository: UserCarRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _userCars = MutableStateFlow<List<UserCar>>(emptyList())
    val userCars: StateFlow<List<UserCar>> = _userCars.asStateFlow()

    private val _selectedUserCar = MutableStateFlow<UserCar?>(null)
    val selectedUserCar: StateFlow<UserCar?> = _selectedUserCar.asStateFlow()

    private val _state = MutableStateFlow<UserCarState>(UserCarState.Idle)
    val state: StateFlow<UserCarState> = _state.asStateFlow()

    fun loadUserCars(userId: Int) {
        viewModelScope.launch {
            _state.value = UserCarState.Loading
            try {
                userCarRepository.getUserCars(userId).collectLatest { cars ->
                    _userCars.value = cars
                    _state.value = UserCarState.Success
                }
            } catch (e: Exception) {
                _state.value = UserCarState.Error("Ошибка загрузки автомобилей: ${e.message}")
            }
        }
    }

    suspend fun addUserCar(userCar: UserCar): Long {
        return try {
            userCarRepository.addUserCar(userCar)
        } catch (e: Exception) {
            throw Exception("Ошибка добавления автомобиля: ${e.message}")
        }
    }

    suspend fun getUserCarById(userCarId: Int) {
        viewModelScope.launch {
            val userCar = userCarRepository.getUserCarById(userCarId)
            _selectedUserCar.value = userCar
        }
    }

    fun clearSelectedUserCar() {
        _selectedUserCar.value = null
    }

    fun clearUserCars() {
        _userCars.value = emptyList()
    }

    fun deleteUserCar(userCarId: Int, userId: Int) {
        viewModelScope.launch {
            _state.value = UserCarState.Loading
            try {
                expenseRepository.deleteExpensesByUserCarId(userCarId)
                userCarRepository.deleteUserCar(userCarId)
                loadUserCars(userId)
                _state.value = UserCarState.Success
            } catch (e: Exception) {
                _state.value = UserCarState.Error("Ошибка удаления автомобиля: ${e.message}")
            }
        }
    }

    sealed class UserCarState {
        object Idle : UserCarState()
        object Loading : UserCarState()
        object Success : UserCarState()
        data class Error(val message: String) : UserCarState()
    }
}
