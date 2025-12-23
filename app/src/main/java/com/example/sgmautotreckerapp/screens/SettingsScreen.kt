package com.example.sgmautotreckerapp.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.R
import com.example.sgmautotreckerapp.commonfunction.MainContent
import com.example.sgmautotreckerapp.data.viewmodel.AuthViewModel
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight

@Composable
fun SettingsScreen(
    userId: Int,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    var userName by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { }

    LaunchedEffect(userId) {
        userId.let { authViewModel.getCurrentUser(it) }
    }

    LaunchedEffect(currentUser) {
        currentUser?.let {
            userName = it.userName
        }
    }

    MainContent(
        navController = navController,
        userId = userId,
        contentFunctions = listOf(
            { SettingsHeader() },
            {
                SettingsContent(
                    userName = userName,
                    onUserNameChange = { userName = it },
                    onAvatarClick = { avatarPicker.launch("image/*") },
                    onSaveName = {
                        if (userName.isNotBlank()) {
                            authViewModel.updateUserName(userId, userName)
                            localError = null
                        } else {
                            localError = "Имя не может быть пустым"
                        }
                    },
                    error = localError
                )
            }
        )
    )
}

@Composable
private fun SettingsHeader() {
    Box(
        modifier = Modifier
            .background(color = mainLight)
            .fillMaxWidth()
            .fillMaxHeight(0.10f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "Настройки",
            fontSize = 60.sp,
            color = backgroundAdvanceLight,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun SettingsContent(
    userName: String,
    onUserNameChange: (String) -> Unit,
    onAvatarClick: () -> Unit,
    onSaveName: () -> Unit,
    error: String?
) {
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
                    text = "Имя пользователя",
                    fontSize = 18.sp,
                    color = backgroundAdvanceLight,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = userName,
                    onValueChange = onUserNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Имя", color = backgroundAdvanceLight) },
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
                Button(
                    onClick = onSaveName,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Сохранить имя",
                        color = textformLight,
                        fontSize = 16.sp
                    )
                }
                if (error != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = com.example.sgmautotreckerapp.ui.theme.circleColor.fourthColor,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = fontLight, shape = RoundedCornerShape(25.dp))
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Аватарка",
                    fontSize = 18.sp,
                    color = backgroundAdvanceLight,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            color = backgroundAdvanceLight,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable {
                            onAvatarClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar_user),
                        contentDescription = "User avatar",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(25.dp))
                    )
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        onAvatarClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Изменить аватарку",
                        color = textformLight,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

