package com.example.sgmautotreckerapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgmautotreckerapp.data.entity.User
import com.example.sgmautotreckerapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun register(
        email: String,
        password: String,
        name: String,
        phone: String,
        tgId: String
    ) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading

            try {
                // Проверяем, существует ли email
                if (userRepository.isEmailExists(email)) {
                    _registrationState.value = RegistrationState.Error("Email уже зарегистрирован")
                    return@launch
                }

                // Создаем нового пользователя
                val user = User(
                    email = email,
                    password = password, // В реальном приложении нужно хэшировать!
                    userName = name,
                    phone = phone,
                    tgID = tgId
                )

                val userId = userRepository.register(user)
                _registrationState.value = RegistrationState.Success(userId.toInt())

            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error("Ошибка регистрации: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                val user = userRepository.login(email)

                if (user == null) {
                    _loginState.value = LoginState.Error("Пользователь не найден")
                    return@launch
                }

                // В реальном приложении используйте безопасное сравнение хэшей!
                if (user.password != password) {
                    _loginState.value = LoginState.Error("Неверный пароль")
                    return@launch
                }

                _currentUser.value = user
                _loginState.value = LoginState.Success(user)

            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Ошибка входа: ${e.message}")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun getCurrentUser(userId: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _currentUser.value = user
        }
    }

    fun clearRegistrationState() {
        _registrationState.value = RegistrationState.Idle
    }

    fun clearLoginState() {
        _loginState.value = LoginState.Idle
    }

    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        data class Success(val userId: Int) : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: User) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}