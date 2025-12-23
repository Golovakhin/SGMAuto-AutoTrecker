package com.example.sgmautotreckerapp.screens.CategoryScreens

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
private fun FuelText(navController: NavController?) {
    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().height(60.dp)) {
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


        Column(Modifier.fillMaxWidth(0.9f).fillMaxHeight()) {
            Box(Modifier.align(Alignment.CenterHorizontally)){
                Text(text = "Топливо", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = fontLight)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FuelForms(
    userId: Int,
    userCars: List<com.example.sgmautotreckerapp.data.entity.UserCar>,
    expenseViewModel: ExpenseViewModel,
    navController: NavController?
){
    val coroutineScope = rememberCoroutineScope()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.9f).fillMaxHeight()) {

            val dateFuel = remember { mutableStateOf("") }
            val sumFuel = remember { mutableStateOf("") }
            var typeFuel by remember { mutableStateOf("") }
            var isExpandedFuelType by remember { mutableStateOf(false) }
            val amountFuel= remember { mutableStateOf("") }
            val  distFuel= remember { mutableStateOf("") }
            
            val fuelTypes = listOf("АИ-92", "АИ-95", "АИ-98", "Дизель", "Газ")
            
            val fuelPerLitr = remember(sumFuel.value, amountFuel.value) {
                try {
                    val sum = sumFuel.value.toDoubleOrNull() ?: 0.0
                    val amount = amountFuel.value.toDoubleOrNull() ?: 0.0
                    if (amount > 0) {
                        String.format("%.2f", sum / amount)
                    } else {
                        "0.00"
                    }
                } catch (e: Exception) {
                    "0.00"
                }
            }
            val receiptUri = remember { mutableStateOf<Uri?>(null) }
            val receiptPicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                receiptUri.value = uri
            }

            val datePickerDialog = remember {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(year, month, dayOfMonth)
                        dateFuel.value = dateFormat.format(selectedCalendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }

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
                        value = dateFuel.value,
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
                    value = sumFuel.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = { newText -> sumFuel.value = newText },
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

            Row(Modifier.padding(top = 20.dp)) {
                Text(
                    text = "Укажите тип бензина",
                    fontSize = 26.sp,
                    color = fontLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(Modifier.padding(start = 15.dp, top = 5.dp)) {
                ExposedDropdownMenuBox(
                    expanded = isExpandedFuelType,
                    onExpandedChange = { isExpandedFuelType = it }
                ) {
                    OutlinedTextField(
                        value = typeFuel,
                        onValueChange = { },
                        readOnly = true,
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
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedFuelType)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(0.6f)
                            .height(55.dp)
                            .background(textformLight)
                            .border(
                                width = 3.dp,
                                color = advanceLight,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                    )
                    ExposedDropdownMenu(
                        expanded = isExpandedFuelType,
                        onDismissRequest = { isExpandedFuelType = false },
                        modifier = Modifier.background(textformLight)
                    ) {
                        fuelTypes.forEach { fuelType ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = fuelType,
                                        color = fontLight
                                    )
                                },
                                onClick = {
                                    typeFuel = fuelType
                                    isExpandedFuelType = false
                                }
                            )
                        }
                    }
                }
            }
            val fieldShape = RoundedCornerShape(10.dp)

            Row(Modifier.padding(top = 20.dp)) {
                Text(
                    text = "Укажите кол-во литров",
                    fontSize = 20.sp,
                    color = fontLight,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(Modifier.padding(start = 15.dp, top = 5.dp)) {

                OutlinedTextField(
                    value = amountFuel.value,
                    onValueChange = { amountFuel.value = it },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 20.sp),
                    placeholder = {
                        Text(
                            text = "Литры",
                            fontSize = 15.sp,
                            color = fontLight,
                            fontStyle = FontStyle.Italic
                        )
                    },
                    shape = fieldShape,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(55.dp)
                        .background(textformLight)
                        .border(
                            width = 3.dp,
                            color = advanceLight,
                            shape = RoundedCornerShape(10.dp)
                        ).clip(RoundedCornerShape(10.dp))
                )


                Column(
                    Modifier.fillMaxWidth().height(55.dp).padding(start = 20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Цена за литр: $fuelPerLitr ₽",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        color = fontLight
                    )
                }
            }


            Row(Modifier.padding(top = 20.dp)) {
                Text(text = "Укажите пробег", fontSize = 20.sp, color = fontLight, fontWeight = FontWeight.SemiBold)
            }
            Row(Modifier.padding(start =15.dp, top = 5.dp ).clip(RoundedCornerShape(10.dp))) {
                OutlinedTextField(
                    value = distFuel.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = {newText -> distFuel.value = newText},
                    singleLine = true,
                    placeholder = {Text(text = "Км", fontSize = 15.sp, color = fontLight, fontStyle = FontStyle.Italic)},

                    modifier =  Modifier.fillMaxWidth(0.4f).height(55.dp).background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))


                )
            }
            Spacer(Modifier.fillMaxWidth().height(30.dp))
            Column(Modifier ,verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Row (
                    Modifier
                        .fillMaxWidth()
                        .clickable { receiptPicker.launch("image/*") },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(Modifier.fillMaxWidth(0.4f), horizontalAlignment = Alignment.End) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_loading),
                            contentDescription = "Прикрепить чек",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { receiptPicker.launch("image/*") },
                            colorFilter = ColorFilter.tint(fontLight)
                        )
                    }

                    Column(Modifier.fillMaxWidth()) {
                        Box(Modifier.align(Alignment.Start)){
                            Text(
                                text = "Загурзить чек",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontStyle = FontStyle.Italic,
                                color = fontLight
                            )
                        }
                    }
                }
                if (receiptUri.value != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Чек прикреплён",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = fontLight
                    )
                }
            }

            Spacer(Modifier.fillMaxWidth().height(40.dp))

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.fillMaxWidth().height(72.dp), contentAlignment = Alignment.Center) {
                    Button(
                    onClick = {
                        val sumValue = sumFuel.value.toDoubleOrNull()
                        val amountValue = amountFuel.value.toDoubleOrNull()
                        val selectedCar = userCars.firstOrNull()
                        
                        if (sumValue == null || sumValue <= 0) {
                            errorMessage = "Введите корректную сумму"
                            return@Button
                        }
                        if (amountValue == null || amountValue <= 0) {
                            errorMessage = "Введите корректное количество литров"
                            return@Button
                        }
                        if (dateFuel.value.isBlank()) {
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
                                    dateFormat.parse(dateFuel.value) ?: Date()
                                } catch (e: Exception) {
                                    Date()
                                }
                                
                                val expense = Expense(
                                    userId = userId,
                                    userCarId = selectedCar.carId,
                                    expenseType = "Топливо",
                                    amount = sumValue,
                                    date = expenseDate,
                                    mileage = 0,
                                    description = "Тип топлива: $typeFuel",
                                    fuelVolume = amountValue,
                                    fuelPricePerLiter = if (amountValue > 0) sumValue / amountValue else null
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
public fun FuelScreen(
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
    Column() {
        CommonHeader(advanceLight, fontLight, "Expanses")
        FuelText(navController)
        FuelForms(
            userId = userId,
            userCars = userCars,
            expenseViewModel = expenseViewModel,
            navController = navController
        )
    }
}
