package com.example.sgmautotreckerapp.screens.CategoryScreens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.R
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.CommonHeader
import com.example.sgmautotreckerapp.data.entity.Expense
import com.example.sgmautotreckerapp.data.viewmodel.ExpenseViewModel
import com.example.sgmautotreckerapp.data.viewmodel.UserCarViewModel
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
private fun CarWashBlocks(navController: NavController?) {
    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().fillMaxHeight(0.05f)) {
        Column(
            Modifier
                .fillMaxWidth(0.1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_arrow_category),
                contentDescription = "Назад",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(32.dp)
                    .clickable { navController?.popBackStack() },
                colorFilter = ColorFilter.tint(fontLight)
            )
        }
        Spacer(Modifier.fillMaxWidth(0.25f).height(30.dp))

        Column(Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
            Text(text = "Мойка", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = fontLight)
        }
    }
}

@Composable
private fun ContentCarWash(
    userId: Int,
    userCars: List<com.example.sgmautotreckerapp.data.entity.UserCar>,
    expenseViewModel: ExpenseViewModel,
    navController: NavController?
){
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.9f).fillMaxHeight()) {

            val dateCarwash = remember { mutableStateOf("") }
            val sumCarWash = remember { mutableStateOf("") }

            val datePickerDialog = remember {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(year, month, dayOfMonth)
                        dateCarwash.value = dateFormat.format(selectedCalendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }

            //Дата траты
            Row(Modifier.padding(top = 10.dp)) {
                Text(
                    text = "Выберите дату",
                    fontSize = 26.sp,
                    color = fontLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(Modifier.padding(start = 15.dp, top = 5.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(55.dp)
                        .clickable { datePickerDialog.show() }
                ) {
                    OutlinedTextField(
                        value = dateCarwash.value,
                        onValueChange = { },
                        readOnly = true,
                        enabled = false,
                        textStyle = TextStyle(fontSize = 20.sp, color = fontLight),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Выбрать",
                                fontSize = 15.sp,
                                color = fontLight,
                                fontStyle = FontStyle.Italic
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .background(textformLight)
                            .border(
                                width = 3.dp,
                                color = advanceLight,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }


            //Сумма траты
            Row(Modifier.padding(top = 20.dp)) {
                Text(
                    text = "Сумма траты",
                    fontSize = 26.sp,
                    color = fontLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(Modifier.padding(start = 15.dp, top = 5.dp).clip(RoundedCornerShape(10.dp))) {
                OutlinedTextField(
                    value = sumCarWash.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = { newText -> sumCarWash.value = newText },
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Сумма, ₽",
                            fontSize = 15.sp,
                            color = fontLight,
                            fontStyle = FontStyle.Italic
                        )
                    },

                    modifier = Modifier.fillMaxWidth(0.6f).height(55.dp).background(textformLight)
                        .border(
                            width = 3.dp,
                            color = advanceLight,
                            shape = RoundedCornerShape(10.dp)
                        ).clip(RoundedCornerShape(10.dp))


                )
            }

            Spacer(Modifier.fillMaxWidth().fillMaxHeight(0.4f))

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.fillMaxWidth().height(72.dp), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            val sumValue = sumCarWash.value.toDoubleOrNull()
                            val selectedCar = userCars.firstOrNull()

                            if (sumValue == null || sumValue <= 0) {
                                errorMessage = "Введите корректную сумму"
                                return@Button
                            }
                            if (dateCarwash.value.isBlank()) {
                                errorMessage = "Выберите дату"
                                return@Button
                            }
                            if (selectedCar == null) {
                                errorMessage = "У вас нет автомобилей. Добавьте автомобиль в гараже."
                                return@Button
                            }

                            errorMessage = null

                            coroutineScope.launch {
                                try {
                                    val expenseDate = try {
                                        dateFormat.parse(dateCarwash.value) ?: Date()
                                    } catch (e: Exception) {
                                        Date()
                                    }

                                    val expense = Expense(
                                        userId = userId,
                                        userCarId = selectedCar.carId,
                                        expenseType = "Мойка",
                                        amount = sumValue,
                                        date = expenseDate,
                                        mileage = 0,
                                        description = "Мойка автомобиля"
                                    )

                                    expenseViewModel.addExpense(expense)
                                    expenseViewModel.loadUserExpenses(userId)
                                    navController?.popBackStack()
                                } catch (e: Exception) {
                                    errorMessage = "Ошибка сохранения: ${e.message}"
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(0.7f).height(72.dp),
                        colors = ButtonDefaults.buttonColors(mainLight)
                    ) {
                        Text(
                            text = "Записать расход",
                            fontSize = 22.sp,
                            color = textformLight,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = fontLight,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}







@Composable
public fun CarWashScreen(
    navController: NavController? = null,
    userId: Int,
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    userCarViewModel: UserCarViewModel = hiltViewModel()
) {
    val userCars by userCarViewModel.userCars.collectAsState()

    LaunchedEffect(userId) {
        userCarViewModel.loadUserCars(userId)
    }

    Background()
    Column {
        CommonHeader(advanceLight, fontLight, "Expanses")
        CarWashBlocks(navController)
        ContentCarWash(
            userId = userId,
            userCars = userCars,
            expenseViewModel = expenseViewModel,
            navController = navController
        )
    }
}