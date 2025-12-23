package com.example.sgmautotreckerapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgmautotreckerapp.data.entity.Car
import com.example.sgmautotreckerapp.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _allCars = MutableStateFlow<List<Car>>(emptyList())
    val allCars: StateFlow<List<Car>> = _allCars.asStateFlow()

    private val _allBrands = MutableStateFlow<List<String>>(emptyList())
    val allBrands: StateFlow<List<String>> = _allBrands.asStateFlow()

    private val _modelsByBrand = MutableStateFlow<List<Car>>(emptyList())
    val modelsByBrand: StateFlow<List<Car>> = _modelsByBrand.asStateFlow()

    private val _selectedCar = MutableStateFlow<Car?>(null)
    val selectedCar: StateFlow<Car?> = _selectedCar.asStateFlow()

    private val _state = MutableStateFlow<CarState>(CarState.Idle)
    val state: StateFlow<CarState> = _state.asStateFlow()

    init {
        loadAllCars()
        loadAllBrands()
    }

    fun loadAllCars() {
        viewModelScope.launch {
            _state.value = CarState.Loading
            try {
                carRepository.getAllCars().collectLatest { cars ->
                    _allCars.value = cars
                    _state.value = CarState.Success
                }
            } catch (e: Exception) {
                _state.value = CarState.Error("Ошибка загрузки автомобилей: ${e.message}")
            }
        }
    }

    fun loadAllBrands() {
        viewModelScope.launch {
            try {
                carRepository.getAllBrands().collectLatest { brands ->
                    _allBrands.value = brands
                }
            } catch (e: Exception) {
                kotlinx.coroutines.delay(500)
                carRepository.getAllBrands().collectLatest { brands ->
                    _allBrands.value = brands
                }
            }
        }
    }

    fun loadModelsByBrand(brand: String) {
        viewModelScope.launch {
            carRepository.getModelsByBrand(brand).collectLatest { models ->
                _modelsByBrand.value = models
            }
        }
    }

    suspend fun selectCar(carId: Int) {
        viewModelScope.launch {
            val car = carRepository.getCarById(carId)
            _selectedCar.value = car
        }
    }

    fun clearSelectedCar() {
        _selectedCar.value = null
    }

    fun clearModelsList() {
        _modelsByBrand.value = emptyList()
    }

    sealed class CarState {
        object Idle : CarState()
        object Loading : CarState()
        object Success : CarState()
        data class Error(val message: String) : CarState()
    }
}
