package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.MainContent
import com.example.sgmautotreckerapp.data.entity.Expense
import com.example.sgmautotreckerapp.data.entity.UserCar
import com.example.sgmautotreckerapp.data.viewmodel.ExpenseViewModel
import com.example.sgmautotreckerapp.data.viewmodel.UserCarViewModel
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.circleColor
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

@Composable
fun AnalysisScreen(
    userId: Int,
    navController: NavController,
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    userCarViewModel: UserCarViewModel = hiltViewModel()
) {
    val expenses by expenseViewModel.userExpenses.collectAsState()
    val userCars by userCarViewModel.userCars.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        val now = Calendar.getInstance()
        expenseViewModel.loadUserExpensesForMonth(userId, now.get(Calendar.YEAR), now.get(Calendar.MONTH))
        userCarViewModel.loadUserCars(userId)
    }

    val totals = remember(expenses) {
        expenses.groupBy { it.expenseType }.mapValues { entry ->
            entry.value.sumOf { it.amount }
        }
    }
    val totalAmount = totals.values.sum()

    val selectedYear by expenseViewModel.selectedYear.collectAsState()
    val selectedMonth by expenseViewModel.selectedMonth.collectAsState()

    MainContent(
        navController = navController,
        userId = userId,
        contentFunctions = listOf(
            { Info() },
            {
                MonthPager(
                    year = selectedYear,
                    month = selectedMonth,
                    onPrev = { expenseViewModel.shiftMonth(userId, -1) },
                    onNext = { expenseViewModel.shiftMonth(userId, 1) }
                )
            },
            { AnalysisRing(totals = totals, totalAmount = totalAmount) },
            { Legend(totals = totals) }
        )
    )
}

@Composable
private fun Info() {
    Box(
        Modifier
            .padding(top = 75.dp, start = 50.dp, end = 50.dp, bottom = 16.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = advanceLight)
            .fillMaxWidth()
            .fillMaxHeight(0.1f),
        contentAlignment = Alignment.Center
    ) {
        Text("Все машины", color = Color.White, fontSize = 20.sp)
    }
}

@Composable
private fun MonthPager(
    year: Int,
    month: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    val monthName = remember(year, month) {
        val name = DateFormatSymbols(Locale("ru")).months[month]
        name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "‹",
            fontSize = 28.sp,
            color = fontLight,
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onPrev)
                .padding(top = 4.dp)
        )

        Text(
            text = "$monthName $year",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = fontLight
        )

        Text(
            text = "›",
            fontSize = 28.sp,
            color = fontLight,
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onNext)
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun AnalysisRing(
    totals: Map<String, Double>,
    totalAmount: Double,
    modifier: Modifier = Modifier.fillMaxWidth(),
    ringSize: androidx.compose.ui.unit.Dp = 260.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 15.dp,
    topPadding: androidx.compose.ui.unit.Dp = 10.dp,
    valueFontSize: androidx.compose.ui.unit.TextUnit = 32.sp,
    currencyFontSize: androidx.compose.ui.unit.TextUnit = 24.sp,
    textColor: Color = fontLight
) {
    val colors = listOf(
        circleColor.firstColor,
        circleColor.secondColor,
        circleColor.thirdColor,
        circleColor.fourthColor
    )
    val displayAmount = totalAmount
    val calculationAmount = totalAmount.takeIf { it > 0 } ?: 1.0

    Box(
        modifier = modifier.padding(top = topPadding),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(ringSize)) {
            if (totals.isEmpty()) {
                return@Canvas
            }
            var currentStartAngle = 0f
            totals.entries.forEachIndexed { index, entry ->
                val sweep = ((entry.value / calculationAmount) * 360f).toFloat()
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = currentStartAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx()),
                    size = Size(size.minDimension, size.minDimension)
                )
                currentStartAngle += sweep
            }
        }
        if (totals.isEmpty()) {
            Text(text = "Расходов пока нет", color = textColor)
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = displayAmount.toLong().toString(),
                    color = textColor,
                    fontSize = valueFontSize,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₽",
                    color = textColor,
                    fontSize = currencyFontSize
                )
            }
        }
    }
}

@Composable
private fun Legend(totals: Map<String, Double>) {
    val items = if (totals.isEmpty()) emptyList() else totals.entries.toList()
    val colors = listOf(
        circleColor.firstColor,
        circleColor.secondColor,
        circleColor.thirdColor,
        circleColor.fourthColor
    )

    Box(
        Modifier
            .padding(30.dp)
            .border(width = 1.dp, color = advanceLight, shape = RoundedCornerShape(20.dp))
            .background(backgroundAdvanceLight, shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        contentAlignment = Alignment.TopStart
    ) {
        if (items.isEmpty()) {
            Text(
                text = "Добавьте расходы, чтобы увидеть легенду",
                color = fontLight,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(Modifier.padding(top = 30.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                items.forEachIndexed { index, entry ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Canvas(
                            Modifier
                                .size(8.dp)
                                .padding(end = 12.dp),
                            onDraw = {
                                drawCircle(
                                    color = colors[index % colors.size],
                                    radius = 4.dp.toPx(),
                                    center = center
                                )
                            }
                        )
                        Text(
                            text = "${entry.key}: ${"%.2f ₽".format(entry.value)}",
                            color = fontLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddExpenseDialog(
    userId: Int,
    userCars: List<UserCar>,
    onDismiss: () -> Unit,
    onSave: (Expense) -> Unit
) {
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCar by remember { mutableStateOf<UserCar?>(userCars.firstOrNull()) }
    var carExpanded by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый расход") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Категория") },
                    placeholder = { Text("Топливо, ТО, Шины...") }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Сумма") },
                    placeholder = { Text("0.00") }
                )
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it.filter { ch -> ch.isDigit() } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Пробег") },
                    placeholder = { Text("0") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Описание") },
                    placeholder = { Text("Комментарий") }
                )

                Column {
                    OutlinedTextField(
                        value = selectedCar?.gosNomer ?: "Выберите автомобиль",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { carExpanded = true },
                        readOnly = true,
                        label = { Text("Автомобиль") },
                        trailingIcon = { Text("▼") }
                    )
                    androidx.compose.material3.DropdownMenu(
                        expanded = carExpanded,
                        onDismissRequest = { carExpanded = false }
                    ) {
                        userCars.forEach { car ->
                            Text(
                                text = car.gosNomer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCar = car
                                        carExpanded = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                if (localError != null) {
                    Text(text = localError!!, color = mainLight)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                localError = null
                val amountValue = amount.toDoubleOrNull()
                val mileageValue = mileage.toIntOrNull() ?: 0
                val carId = selectedCar?.carId
                if (category.isBlank() || amountValue == null || carId == null) {
                    localError = "Заполните категорию, сумму и выберите авто"
                    return@Button
                }
                onSave(
                    Expense(
                        userId = userId,
                        userCarId = carId,
                        expenseType = category,
                        amount = amountValue,
                        date = Date(),
                        mileage = mileageValue,
                        description = description
                    )
                )
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
