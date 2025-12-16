package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.data.entity.Car
import com.example.sgmautotreckerapp.data.entity.UserCar
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.Navigation
import com.example.sgmautotreckerapp.data.viewmodel.CarViewModel
import com.example.sgmautotreckerapp.data.viewmodel.UserCarViewModel
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight
import kotlinx.coroutines.launch

@Composable
fun GarageScreen(
    userId: Int,
    navController: NavController,
    carViewModel: CarViewModel = hiltViewModel(),
    userCarViewModel: UserCarViewModel = hiltViewModel()
) {
    val userCars by userCarViewModel.userCars.collectAsState()
    val allCars by carViewModel.allCars.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showAddDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val carsById = remember(allCars) { allCars.associateBy { it.id } }

    LaunchedEffect(userId) {
        userCarViewModel.loadUserCars(userId)
    }

    Background()
    Column {
        Header()

        if (userCars.isEmpty()) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Нет сохранённых автомобилей",
                color = fontLight,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(userCars, key = { it.id ?: it.hashCode() }) { userCar ->
                    val car = carsById[userCar.carId]
                    GarageCar(
                        mark = car?.mark ?: "Авто",
                        model = car?.model ?: "",
                        generation = car?.generation ?: "",
                        year = userCar.year,
                        gosNumber = userCar.gosNomer
                    )
                }
            }
        }

        NewCar(onAdd = { showAddDialog = true })
        Spacer(Modifier.height(16.dp))
        Navigation()
    }

    if (showAddDialog) {
        AddCarDialog(
            carViewModel = carViewModel,
            onDismiss = { showAddDialog = false },
            onSave = { gos, vin, year, car ->
                if (car == null) {
                    errorMessage = "Выберите модель автомобиля"
                    return@AddCarDialog
                }
                coroutineScope.launch {
                    try {
                        userCarViewModel.addUserCar(
                            UserCar(
                                userId = userId,
                                carId = car.id ?: 0,
                                gosNomer = gos,
                                vin = vin,
                                year = year
                            )
                        )
                        userCarViewModel.loadUserCars(userId)
                        showAddDialog = false
                        errorMessage = null
                    } catch (e: Exception) {
                        errorMessage = e.message
                    }
                }
            }
        )
    }
}

@Composable
private fun Header() {
    Row(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.10f)
            .background(fontLight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "Garage",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = backgroundLight
        )
    }
}

@Composable
public fun GarageCar(
    mark: String,
    model: String,
    generation: String,
    year: Int,
    gosNumber: String
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight()
                .height(200.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(fontLight)
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = mark, fontSize = 30.sp, color = backgroundLight)
                        Text(text = model, fontSize = 30.sp, color = backgroundLight)
                    }
                }
                Row(Modifier.fillMaxSize()) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.5f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.padding(start = 15.dp, bottom = 10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Year: $year",
                                fontSize = 14.sp,
                                lineHeight = 23.sp,
                                color = backgroundLight
                            )
                            Text(
                                text = "Generation: $generation",
                                fontSize = 14.sp,
                                lineHeight = 23.sp,
                                color = backgroundLight
                            )
                            Text(
                                text = "Number: $gosNumber",
                                fontSize = 14.sp,
                                lineHeight = 23.sp,
                                color = backgroundLight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NewCar(onAdd: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight()
                .height(200.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(fontLight)
                .clickable { onAdd() }
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Add new car", color = backgroundLight, fontSize = 30.sp)
                }
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Canvas(
                        Modifier
                            .size(85.dp)
                            .clip(shape = CircleShape)
                    ) {
                        drawCircle(
                            color = backgroundLight,
                            radius = 42.5.dp.toPx()
                        )
                        drawLine(
                            color = fontLight,
                            start = Offset(x = size.width / 3, y = size.height / 2),
                            end = Offset(x = size.width - size.width / 3, y = size.height / 2),
                            strokeWidth = 20f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = fontLight,
                            start = Offset(x = size.width / 2, y = size.height / 3),
                            end = Offset(x = size.width / 2, y = size.height - size.height / 3),
                            strokeWidth = 20f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddCarDialog(
    carViewModel: CarViewModel,
    onDismiss: () -> Unit,
    onSave: (gosNomer: String, vin: String, year: Int, car: Car?) -> Unit
) {
    val brands by carViewModel.allBrands.collectAsState()
    val models by carViewModel.modelsByBrand.collectAsState()

    var selectedBrand by remember { mutableStateOf("") }
    var selectedCar by remember { mutableStateOf<Car?>(null) }
    var gosNomer by remember { mutableStateOf("") }
    var vin by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    var brandExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedBrand) {
        if (selectedBrand.isNotBlank()) {
            carViewModel.loadModelsByBrand(selectedBrand)
            selectedCar = null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Новый автомобиль") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Column {
                    OutlinedTextField(
                        value = selectedBrand,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { brandExpanded = true },
                        readOnly = true,
                        label = { Text("Марка") },
                        placeholder = { Text("Выберите марку") },
                        trailingIcon = { Text("▼") }
                    )
                    androidx.compose.material3.DropdownMenu(
                        expanded = brandExpanded,
                        onDismissRequest = { brandExpanded = false }
                    ) {
                        brands.forEach { brand ->
                            Text(
                                text = brand,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedBrand = brand
                                        brandExpanded = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                Column {
                    OutlinedTextField(
                        value = selectedCar?.let { "${it.model} ${it.generation}" } ?: "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { modelExpanded = true },
                        readOnly = true,
                        label = { Text("Модель") },
                        placeholder = { Text("Выберите модель") },
                        trailingIcon = { Text("▼") }
                    )
                    androidx.compose.material3.DropdownMenu(
                        expanded = modelExpanded,
                        onDismissRequest = { modelExpanded = false }
                    ) {
                        models.forEach { car ->
                            Text(
                                text = "${car.model} ${car.generation}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCar = car
                                        modelExpanded = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = gosNomer,
                    onValueChange = { gosNomer = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Госномер") },
                    placeholder = { Text("Р805ХР33") }
                )

                OutlinedTextField(
                    value = vin,
                    onValueChange = { vin = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("VIN") },
                    placeholder = { Text("17 символов") }
                )

                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it.filter { ch -> ch.isDigit() } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Год выпуска") },
                    placeholder = { Text("2024") }
                )

                if (localError != null) {
                    Text(text = localError!!, color = mainLight)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                localError = null
                val yearInt = year.toIntOrNull()
                if (gosNomer.isBlank() || vin.isBlank() || yearInt == null) {
                    localError = "Заполните все поля"
                    return@Button
                }
                onSave(gosNomer, vin, yearInt, selectedCar)
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

