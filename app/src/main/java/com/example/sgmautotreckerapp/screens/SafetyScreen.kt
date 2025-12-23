package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.commonfunction.MainContent
import com.example.sgmautotreckerapp.data.viewmodel.AuthViewModel
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight

@Composable
fun SafetyScreen(
    userId: Int,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    var localError by remember { mutableStateOf<String?>(null) }
    var localSuccess by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        authViewModel.getCurrentUser(userId)
    }

    MainContent(
        navController = navController,
        userId = userId,
        contentFunctions = listOf(
            { SafetyHeader() },
            {
                SafetyContent(
                    onChangePassword = { oldPassword, newPassword, repeatPassword ->
                        localError = null
                        localSuccess = null

                        val user = currentUser
                        if (user == null) {
                            localError = "Пользователь не найден"
                            return@SafetyContent
                        }

                        if (oldPassword.isBlank() || newPassword.isBlank() || repeatPassword.isBlank()) {
                            localError = "Все поля должны быть заполнены"
                            return@SafetyContent
                        }

                        if (newPassword == oldPassword) {
                            localError = "Новый пароль должен отличаться от текущего"
                            return@SafetyContent
                        }

                        if (newPassword != repeatPassword) {
                            localError = "Пароли не совпадают"
                            return@SafetyContent
                        }

                        if (user.password != oldPassword) {
                            localError = "Текущий пароль указан неверно"
                            return@SafetyContent
                        }

                        authViewModel.updateUserPassword(userId, newPassword) { ok, message ->
                            if (ok) {
                                localSuccess = "Пароль успешно изменён"
                            } else {
                                localError = message ?: "Ошибка изменения пароля"
                            }
                        }
                    },
                    error = localError,
                    success = localSuccess
                )
            }
        )
    )
}

@Composable
private fun SafetyHeader() {
    Box(
        modifier = Modifier
            .background(color = mainLight)
            .fillMaxWidth()
            .fillMaxHeight(0.10f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "Безопасность",
            fontSize = 40.sp,
            color = backgroundAdvanceLight,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun SafetyContent(
    onChangePassword: (String, String, String) -> Unit,
    error: String?,
    success: String?
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    Spacer(Modifier.fillMaxHeight(0.05f))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = fontLight, shape = RoundedCornerShape(25.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Изменение пароля",
                    fontSize = 18.sp,
                    color = backgroundAdvanceLight,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Текущий пароль", color = backgroundAdvanceLight) },
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedTextColor = backgroundAdvanceLight,
                        unfocusedTextColor = backgroundAdvanceLight,
                        focusedLabelColor = backgroundAdvanceLight,
                        unfocusedLabelColor = backgroundAdvanceLight,
                        focusedBorderColor = mainLight,
                        unfocusedBorderColor = mainLight
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Новый пароль", color = backgroundAdvanceLight) },
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedTextColor = backgroundAdvanceLight,
                        unfocusedTextColor = backgroundAdvanceLight,
                        focusedLabelColor = backgroundAdvanceLight,
                        unfocusedLabelColor = backgroundAdvanceLight,
                        focusedBorderColor = mainLight,
                        unfocusedBorderColor = mainLight
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Повторите новый пароль", color = backgroundAdvanceLight) },
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedTextColor = backgroundAdvanceLight,
                        unfocusedTextColor = backgroundAdvanceLight,
                        focusedLabelColor = backgroundAdvanceLight,
                        unfocusedLabelColor = backgroundAdvanceLight,
                        focusedBorderColor = mainLight,
                        unfocusedBorderColor = mainLight
                    )
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { onChangePassword(oldPassword, newPassword, repeatPassword) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Сохранить пароль",
                        color = textformLight,
                        fontSize = 16.sp
                    )
                }

                if (error != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = backgroundAdvanceLight,
                        fontSize = 14.sp
                    )
                }

                if (success != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = success,
                        color = backgroundAdvanceLight,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
