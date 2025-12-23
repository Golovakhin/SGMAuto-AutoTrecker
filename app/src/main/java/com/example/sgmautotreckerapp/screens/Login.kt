package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.data.viewmodel.AuthViewModel
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val loginState by authViewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is AuthViewModel.LoginState.Success -> {
                val userId = state.user.id
                if (userId != null) {
                    authViewModel.clearLoginState()
                    navController.navigate("garage/$userId") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    localError = "Не удалось определить пользователя"
                }
            }

            is AuthViewModel.LoginState.Error -> localError = state.message
            else -> Unit
        }
    }

    Column(
        Modifier
            .background(backgroundLight)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.padding(45.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = "SGM", fontSize = 64.sp, fontWeight = FontWeight.Bold, color = fontLight)
            Text(text = "-", Modifier.padding(bottom = 10.dp), fontSize = 32.sp, color = fontLight)
            Text(text = "Auto", Modifier.padding(bottom = 8.dp), fontSize = 36.sp, color = fontLight)
        }

        Spacer(Modifier.fillMaxHeight(0.1f))
        Row {
            Text(text = "Вход в профиль", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = fontLight)
        }

        Spacer(Modifier.fillMaxHeight(0.02f))

        Row(Modifier.fillMaxHeight(0.35f).fillMaxWidth(0.8f)) {
            Column {
                Spacer(Modifier.fillMaxWidth().weight(1f))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(10.dp))
                ) {
                    OutlinedTextField(
                        value = email,
                        textStyle = TextStyle(fontSize = 20.sp),
                        onValueChange = { email = it.trim() },
                        singleLine = true,
                        placeholder = { Text(text = "Почта", fontSize = 20.sp, color = fontLight) },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(textformLight)
                            .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
                    )
                }

                Spacer(Modifier.fillMaxWidth().weight(1f))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(10.dp))
                ) {
                    OutlinedTextField(
                        value = password,
                        textStyle = TextStyle(fontSize = 20.sp),
                        onValueChange = { password = it },
                        singleLine = true,
                        placeholder = { Text(text = "Пароль", fontSize = 20.sp, color = fontLight) },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(textformLight)
                            .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
                    )
                }
            }
        }

        if (localError != null) {
            Text(
                text = localError!!,
                color = mainLight,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.fillMaxHeight(0.1f))

        Column(Modifier.fillMaxHeight(0.45f), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    localError = null
                    if (email.isBlank() || password.isBlank()) {
                        localError = "Заполните почту и пароль"
                        return@Button
                    }
                    authViewModel.login(email, password)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.7f).weight(1f),
                colors = ButtonDefaults.buttonColors(mainLight)
            ) {
                if (loginState is AuthViewModel.LoginState.Loading) {
                    CircularProgressIndicator(
                        color = textformLight,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Войти",
                        fontSize = 22.sp,
                        color = textformLight,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.weight(0.5f))

            Button(
                onClick = { navController.navigate("register") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.7f).weight(1f),
                colors = ButtonDefaults.buttonColors(fontLight)
            ) {
                Text(
                    text = "Зарегистрироваться",
                    fontSize = 22.sp,
                    color = textformLight,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
