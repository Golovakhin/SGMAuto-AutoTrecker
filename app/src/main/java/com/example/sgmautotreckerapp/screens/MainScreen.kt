package com.example.sgmautotreckerapp.screens

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sgmautotreckerapp.Segment
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.MainContent
import com.example.sgmautotreckerapp.data.viewmodel.CarViewModel
import com.example.sgmautotreckerapp.data.viewmodel.ExpenseViewModel
import com.example.sgmautotreckerapp.data.viewmodel.UserCarViewModel
import com.example.sgmautotreckerapp.navigation.AppRoutes
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.circleColor
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import kotlin.math.round





@Composable
private fun AppBar (){
    Column() {
        Row(Modifier.fillMaxHeight(0.1f).fillMaxWidth().background(advanceLight), horizontalArrangement = Arrangement.Absolute.Center, verticalAlignment = Alignment.Bottom) {
            Text(text = "SGM", fontSize = 64.sp, fontWeight = FontWeight.Bold, color = fontLight)
            Text(text = "-", Modifier.padding(bottom = 10.dp), fontSize = 32.sp, color = fontLight)
            Text(text = "Auto", Modifier.padding(bottom = 8.dp), fontSize = 36.sp, color = fontLight)
        }
    }
}

@Composable
private fun CarMain(
    userId: Int?,
    navController: androidx.navigation.NavController? = null,
    carViewModel: CarViewModel = hiltViewModel(),
    userCarViewModel: UserCarViewModel = hiltViewModel()
) {
    val userCars by userCarViewModel.userCars.collectAsState()
    val allCars by carViewModel.allCars.collectAsState()

    val carsById = remember(allCars) { allCars.associateBy { it.id } }

    LaunchedEffect(userId) {
        userId?.let { userCarViewModel.loadUserCars(it) }
        carViewModel.loadAllCars()
    }

    Column {
        Spacer(Modifier.fillMaxWidth().fillMaxHeight(0.05f))

        val userCar = userCars.firstOrNull()
        val car = userCar?.let { carsById[it.carId] }

        if (userCar == null) {
            // Если авто еще нет — показываем карточку-подсказку
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(0.9f)
                        .height(200.dp)
                        .background(fontLight, shape = RoundedCornerShape(25.dp))
                        .clickable(enabled = navController != null && userId != null) {
                            userId?.let { navController?.navigate(AppRoutes.garageRoute(it)) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Добавьте автомобиль",
                        color = backgroundLight,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            // Карточка автомобиля как в Garage, но без кнопки удаления
            GarageCar(
                mark = car?.mark ?: "Авто",
                model = car?.model ?: "",
                generation = car?.generation ?: "",
                year = userCar.year,
                gosNumber = userCar.gosNomer,
                imageUrl = car?.imageURl,
                userCarId = null,
                onDelete = null
            )
        }
    }
}


@Composable
private fun Analitika(
    userId: Int?,
    navController: androidx.navigation.NavController? = null,
    expenseViewModel: ExpenseViewModel = hiltViewModel()
){
    val expenses by expenseViewModel.userExpenses.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { expenseViewModel.loadUserExpenses(it) }
    }

    val totals = expenses.groupBy { it.expenseType }.mapValues { entry ->
        entry.value.sumOf { it.amount }
    }
    val totalAmount = totals.values.sum()
    Spacer(Modifier.fillMaxWidth().fillMaxHeight(0.05f))

    // Увеличили высоту блока аналитики, чтобы длинные категории помещались в легенду
    Row(Modifier.fillMaxWidth().fillMaxHeight(0.32f), horizontalArrangement = Arrangement.Center) {
        Box(
            Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight()
                .background(mainLight, shape = RoundedCornerShape(25.dp))
                .clickable(enabled = navController != null && userId != null) {
                    userId?.let { navController?.navigate(AppRoutes.analysisRoute(it)) }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Легенда слева
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Легенда должна отражать реальные категории (expenseType), которые есть у пользователя
                    val entries = totals.entries.toList()
                    val colors = listOf(
                        circleColor.firstColor,
                        circleColor.secondColor,
                        circleColor.thirdColor,
                        circleColor.fourthColor
                    )

                    if (entries.isEmpty()) {
                        Text(
                            text = "Расходов пока нет",
                            color = fontLight,
                            fontSize = 16.sp
                        )
                    } else {
                        entries.forEachIndexed { index, entry ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Canvas(
                                    Modifier
                                        .padding(end = 12.dp)
                                        .height(10.dp)
                                        .fillMaxWidth(0.05f)
                                ) {
                                    drawCircle(
                                        color = colors[index % colors.size],
                                        radius = 4.dp.toPx()
                                    )
                                }
                                Text(
                                    text = entry.key,
                                    color = fontLight,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }

                // Кольцо справа
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    // На главном экране: смещаем вправо, центруем по вертикали и уменьшаем подпись суммы
                    AnalysisRing(
                        totals = totals,
                        totalAmount = totalAmount,
                        modifier = Modifier.align(Alignment.End),
                        ringSize = 110.dp,
                        strokeWidth = 10.dp,
                        topPadding = 0.dp,
                        valueFontSize = 14.sp,
                        currencyFontSize = 10.sp
                    )
                }
            }
        }
    }
}



@Composable
private fun AnotherBlocks(){

    Spacer(Modifier.fillMaxWidth().fillMaxHeight(0.08f))

    Row(Modifier.fillMaxWidth().fillMaxHeight(0.4f).background(backgroundLight), horizontalArrangement = Arrangement.Center) {
        Box(Modifier.fillMaxWidth(0.8f).fillMaxHeight() ) {
            Row() {
                Box(Modifier.fillMaxWidth(0.45f).fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }

                Box(Modifier.fillMaxWidth(0.2f).fillMaxHeight().background(backgroundLight)) {

                }

                Box(Modifier.fillMaxWidth().fillMaxHeight().background(fontLight, shape = RoundedCornerShape(25.dp))) {

                }
            }
        }
    }
}












@Composable
private fun Legend(){

    val segments = listOf(
        Segment(circleColor.firstColor,90f),
        Segment(circleColor.secondColor,90f),
        Segment(circleColor.thirdColor,90f),
        Segment(circleColor.fourthColor,90f),
    )

    Box(
        Modifier.padding(30.dp)
            .border(width = 1.dp, color = advanceLight, shape = RoundedCornerShape(20.dp))
            .background(backgroundAdvanceLight, shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        contentAlignment = Alignment.TopStart
    ) {
        Row() {
            Column(Modifier.padding(top = 30.dp), verticalArrangement = Arrangement.Top) {
                for (segment in segments){

                    Canvas(Modifier.fillMaxWidth(0.15f)) {
                        drawCircle(color = segment.color, radius = 4.dp.toPx(),)

                    }
                    Spacer(Modifier.height(30.dp))
                }

            }

            Column(Modifier.padding(top = 22.dp)) {
                for (segment in segments){

                    Text(text = "GG", color = fontLight)
                    Spacer(Modifier.height(14.dp))
                }
            }
        }


    }
}















@Composable
public fun MainScreen(
    navController: androidx.navigation.NavController? = null,
    userId: Int? = null,
    expenseViewModel: ExpenseViewModel = hiltViewModel()
){
    MainContent(
        navController = navController,
        userId = userId,
        contentFunctions = listOf(
            { AppBar() },
            { CarMain(userId = userId, navController = navController) },
            { Analitika(userId = userId, navController = navController, expenseViewModel = expenseViewModel) },
            { AnotherBlocks() }
        )
    )
}