package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sgmautotreckerapp.R
import com.example.sgmautotreckerapp.commonfunction.MainContent
import com.example.sgmautotreckerapp.data.viewmodel.AuthViewModel
import com.example.sgmautotreckerapp.navigation.AppRoutes
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight


@Composable
public fun Header_Profile() {
    Box(
        modifier = Modifier
            .background(color = mainLight)
            .fillMaxWidth()
            .fillMaxHeight(0.10f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "Profile",
            fontSize = 60.sp,
            color = backgroundAdvanceLight,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
public fun Info_Profile(
    userName: String?,
    email: String?,
    onLogout: () -> Unit
) {
    Spacer(Modifier.fillMaxHeight(0.05f))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(color = fontLight, shape = RoundedCornerShape(25.dp))
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_out_profile),
                        contentDescription = "Logout",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onLogout() }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(
                                color = backgroundAdvanceLight,
                                shape = RoundedCornerShape(25.dp)
                            ),
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

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userName ?: "Имя пользователя",
                        fontSize = 20.sp,
                        color = backgroundAdvanceLight,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = email ?: "email@example.com",
                        fontSize = 14.sp,
                        color = backgroundAdvanceLight,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}


@Composable
public fun Text_Profile() {
    Spacer(Modifier.fillMaxHeight(0.05f))
    Text(
        "Главная",
        Modifier.padding(start = 20.dp),
        color = fontLight,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        fontStyle = FontStyle.Italic
    )
}

@Composable
public fun Button_Profile1(
    onClick: () -> Unit = {}
) {
    Spacer(Modifier.fillMaxHeight(0.05f))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f)
                .background(color = mainLight, shape = RoundedCornerShape(25.dp))
                .clickable(onClick = onClick),
        ) {
            Column(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
                    .background(color = mainLight, shape = RoundedCornerShape(25.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(0.8f)
                        .background(
                            color = fontLight,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_setting),
                        contentDescription = "Profile settings",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        color = mainLight,
                        shape = RoundedCornerShape(25.dp)
                    ),
            ) {
                Text(
                    "Настройка профиля",
                    Modifier.padding(top = 20.dp, start = 20.dp),
                    color = backgroundAdvanceLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    "Обнови или модифицируй свой профиль",
                    Modifier.padding(top = 8.dp, start = 20.dp),
                    color = backgroundAdvanceLight,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
public fun Button_Profile2() {
    Spacer(Modifier.fillMaxHeight(0.05f))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f)
                .background(color = mainLight, shape = RoundedCornerShape(25.dp)),
        ) {
            Column(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
                    .background(color = mainLight, shape = RoundedCornerShape(25.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(0.8f)
                        .background(
                            color = fontLight,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_safety),
                        contentDescription = "Security settings",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        color = mainLight,
                        shape = RoundedCornerShape(25.dp)
                    ),
            ) {
                Text(
                    "Безопасность",
                    Modifier.padding(top = 20.dp, start = 20.dp),
                    color = backgroundAdvanceLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    "Изменить пароль",
                    Modifier.padding(top = 8.dp, start = 20.dp),
                    color = backgroundAdvanceLight,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}


@Composable
public fun Profile(
    navController: androidx.navigation.NavController? = null,
    userId: Int? = null,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { authViewModel.getCurrentUser(it) }
    }

    MainContent(
        navController = navController,
        userId = userId,
        contentFunctions = listOf(
            { Header_Profile() },
            {
                Info_Profile(
                    userName = currentUser?.userName,
                    email = currentUser?.email,
                    onLogout = {
                        authViewModel.logout()
                        navController?.navigate(AppRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            },
            { Text_Profile() },
            { 
                Button_Profile1(
                    onClick = {
                        userId?.let {
                            navController?.navigate("settings/$it")
                        }
                    }
                )
            },
            { Button_Profile2() }
        )
    )
}
