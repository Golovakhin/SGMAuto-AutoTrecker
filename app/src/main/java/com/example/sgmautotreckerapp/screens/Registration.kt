package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun RegistrationScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val registrationState by authViewModel.registrationState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var tg by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(registrationState) {
        when (val state = registrationState) {
            is AuthViewModel.RegistrationState.Success -> {
                authViewModel.clearRegistrationState()
                navController.navigate("garage/${state.userId}") {
                    popUpTo("login") { inclusive = true }
                }
            }

            is AuthViewModel.RegistrationState.Error -> localError = state.message
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

        Spacer(Modifier.fillMaxHeight(0.05f))
        Row {
            Text(text = "Регистрация", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = fontLight)
        }

        Spacer(Modifier.fillMaxHeight(0.02f))

        Column(
            Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.55f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = email,
                textStyle = TextStyle(fontSize = 18.sp),
                onValueChange = { email = it.trim() },
                singleLine = true,
                placeholder = { Text(text = "Почта", fontSize = 18.sp, color = fontLight) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(textformLight)
                    .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
            )

            OutlinedTextField(
                value = name,
                textStyle = TextStyle(fontSize = 18.sp),
                onValueChange = { name = it },
                singleLine = true,
                placeholder = { Text(text = "Имя", fontSize = 18.sp, color = fontLight) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(textformLight)
                    .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
            )

            OutlinedTextField(
                value = phone,
                textStyle = TextStyle(fontSize = 18.sp),
                onValueChange = { phone = it },
                singleLine = true,
                placeholder = { Text(text = "Телефон", fontSize = 18.sp, color = fontLight) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(textformLight)
                    .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
            )

            OutlinedTextField(
                value = tg,
                textStyle = TextStyle(fontSize = 18.sp),
                onValueChange = { tg = it },
                singleLine = true,
                placeholder = { Text(text = "Telegram", fontSize = 18.sp, color = fontLight) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(textformLight)
                    .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
            )

            OutlinedTextField(
                value = password,
                textStyle = TextStyle(fontSize = 18.sp),
                onValueChange = { password = it },
                singleLine = true,
                placeholder = { Text(text = "Пароль", fontSize = 18.sp, color = fontLight) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(textformLight)
                    .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
            )

            OutlinedTextField(
                value = confirmPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                onValueChange = { confirmPassword = it },
                singleLine = true,
                placeholder = { Text(text = "Подтвердите пароль", fontSize = 18.sp, color = fontLight) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(textformLight)
                    .border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))
            )
        }

        if (localError != null) {
            Text(
                text = localError!!,
                color = mainLight,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.fillMaxHeight(0.05f))

        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    localError = null
                    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || name.isBlank()) {
                        localError = "Заполните все поля"
                        return@Button
                    }
                    if (password != confirmPassword) {
                        localError = "Пароли не совпадают"
                        return@Button
                    }
                    authViewModel.register(email, password, name, phone, tg)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.12f),
                colors = ButtonDefaults.buttonColors(mainLight)
            ) {
                if (registrationState is AuthViewModel.RegistrationState.Loading) {
                    CircularProgressIndicator(color = textformLight, strokeWidth = 3.dp)
                } else {
                    Text(text = "Создать профиль", fontSize = 18.sp, color = textformLight)
                }
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = "Уже есть аккаунт? Войти",
                    fontSize = 16.sp,
                    color = fontLight,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}