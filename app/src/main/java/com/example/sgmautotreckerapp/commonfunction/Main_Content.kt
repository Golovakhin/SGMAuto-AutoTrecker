package com.example.sgmautotreckerapp.commonfunction

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.R
import com.example.sgmautotreckerapp.navigation.AppRoutes
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MainContent(
    navController: NavController? = null,
    userId: Int? = null,
    onAddClick: (() -> Unit)? = null,
    contentFunctions: List<@Composable () -> Unit> = emptyList()
    ) {
    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = advanceLight) {
                Row(
                    Modifier.background(color = advanceLight).fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Первая иконка - Главная (Home) - ведет на MainScreen
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(enabled = navController != null && userId != null) {
                                navController?.navigate("main/$userId") {
                                    launchSingleTop = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                    
                    // Вторая иконка - Статистика (Analysis)
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(enabled = navController != null && userId != null) {
                                navController?.navigate("analysis/$userId") {
                                    launchSingleTop = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_static),
                            contentDescription = "Statistics",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                    
                    // Центральное место для кнопки "+"
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1.55f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Canvas(Modifier.fillMaxSize())
                        {
                            drawCircle(
                                color = backgroundLight,
                                radius = 60.dp.toPx(),
                                center = Offset(x = size.width / 2, y = -10f)
                            )
                        }
                    }
                    
                    // Третья иконка - Машинка (Garage)
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(enabled = navController != null && userId != null) {
                                navController?.navigate("garage/$userId") {
                                    launchSingleTop = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_car_1),
                            contentDescription = "Garage",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                    
                    // Четвертая иконка - Профиль (если залогинен) или Вход (если нет)
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(enabled = navController != null) {
                                if (userId != null) {
                                    // Если залогинен - переходим в профиль
                                    navController?.navigate("profile/$userId") {
                                        // Сохраняем историю навигации
                                        launchSingleTop = true
                                    }
                                } else {
                                    // Если не залогинен - переходим на экран входа
                                    navController?.navigate("login") {
                                        // Очищаем весь стек навигации
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar_user),
                            contentDescription = "Profile",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }

                }
            }
        },


        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    if (navController != null && userId != null) {
                        navController.navigate(AppRoutes.categoryRoute(userId))
                    } else {
                        onAddClick?.invoke()
                    }
                },
                Modifier.offset(y = 64.dp),
                shape = CircleShape,
                containerColor = fontLight,
                contentColor = backgroundLight
            ) {
                Text("+", fontSize = 60.sp)
            }

        },

        floatingActionButtonPosition = FabPosition.Center
    ) {
        Background()
        Column() {
            contentFunctions.forEach { contentFunction ->
                contentFunction()
            }
        }

    }
}








@Composable
public fun CommonHeader(headerColor: Color,fontHeaderColor: Color,textHeader: String){
    Column() {
        Row(Modifier.fillMaxWidth().fillMaxHeight(0.1f).background(color =headerColor),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom) {
            Text(text = textHeader, fontSize = 60.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic,color = fontHeaderColor)
        }
    }
}