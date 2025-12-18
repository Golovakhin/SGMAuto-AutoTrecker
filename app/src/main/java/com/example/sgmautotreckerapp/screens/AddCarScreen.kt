package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.data.entity.Car
import com.example.sgmautotreckerapp.data.entity.UserCar
import com.example.sgmautotreckerapp.data.viewmodel.CarViewModel
import com.example.sgmautotreckerapp.data.viewmodel.UserCarViewModel
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    userId: Int,
    navController: NavController,
    carViewModel: CarViewModel = hiltViewModel(),
    userCarViewModel: UserCarViewModel = hiltViewModel()
) {
    val brands by carViewModel.allBrands.collectAsState()
    val models by carViewModel.modelsByBrand.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var selectedBrand by remember { mutableStateOf("") }
    var selectedCar by remember { mutableStateOf<Car?>(null) }
    var gosNomer by remember { mutableStateOf("") }
    var vin by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var brandExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }

    // Загружаем данные при открытии экрана
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200)
        carViewModel.loadAllBrands()
        carViewModel.loadAllCars()
    }

    LaunchedEffect(selectedBrand) {
        if (selectedBrand.isNotBlank()) {
            carViewModel.loadModelsByBrand(selectedBrand)
            selectedCar = null
        } else {
            carViewModel.clearModelsList()
        }
    }

    Background()
    Box(
        Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    brandExpanded = false
                    modelExpanded = false
                }
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* Предотвращаем закрытие при клике на форму */ }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(fontLight, RoundedCornerShape(20.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Добавить автомобиль",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = backgroundLight
            )
        }

        Spacer(Modifier.height(24.dp))

        // Форма
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Выбор марки
            Column {
                Text(
                    text = "Марка",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontLight,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = brandExpanded,
                    onExpandedChange = { brandExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedBrand.ifEmpty { "Выберите марку" },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            color = if (selectedBrand.isEmpty()) advanceLight else fontLight
                        ),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedTextColor = fontLight,
                            unfocusedTextColor = fontLight,
                            focusedBorderColor = mainLight,
                            unfocusedBorderColor = advanceLight,
                            focusedContainerColor = textformLight,
                            unfocusedContainerColor = textformLight,
                            disabledTextColor = if (selectedBrand.isEmpty()) advanceLight else fontLight,
                            disabledContainerColor = textformLight
                        ),
                        shape = RoundedCornerShape(15.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = brandExpanded,
                        onDismissRequest = { brandExpanded = false },
                        modifier = Modifier.background(textformLight)
                    ) {
                        if (brands.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Загрузка...", color = fontLight) },
                                onClick = {}
                            )
                        } else {
                            brands.forEach { brand ->
                                DropdownMenuItem(
                                    text = { Text(brand, fontSize = 18.sp, color = fontLight) },
                                    onClick = {
                                        selectedBrand = brand
                                        brandExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Выбор модели
            if (selectedBrand.isNotBlank()) {
                Column {
                    Text(
                        text = "Модель",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = fontLight,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = modelExpanded,
                        onExpandedChange = { modelExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCar?.let { "${it.model} ${it.generation}" } ?: "Выберите модель",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = if (selectedCar == null) advanceLight else fontLight
                            ),
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedTextColor = fontLight,
                                unfocusedTextColor = fontLight,
                                focusedBorderColor = mainLight,
                                unfocusedBorderColor = advanceLight,
                                focusedContainerColor = textformLight,
                                unfocusedContainerColor = textformLight,
                                disabledTextColor = if (selectedCar == null) advanceLight else fontLight,
                                disabledContainerColor = textformLight
                            ),
                            shape = RoundedCornerShape(15.dp),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = modelExpanded,
                            onDismissRequest = { modelExpanded = false },
                            modifier = Modifier.background(textformLight)
                        ) {
                            if (models.isEmpty() && selectedBrand.isNotBlank()) {
                                DropdownMenuItem(
                                    text = { Text("Загрузка...", color = fontLight) },
                                    onClick = {}
                                )
                            } else {
                                models.forEach { car ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(
                                                "${car.model} ${car.generation}",
                                                fontSize = 18.sp,
                                                color = fontLight
                                            ) 
                                        },
                                        onClick = {
                                            selectedCar = car
                                            modelExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Госномер
            OutlinedTextField(
                value = gosNomer,
                onValueChange = { gosNomer = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Госномер", color = fontLight) },
                placeholder = { Text("Р805ХР33", color = advanceLight) },
                textStyle = TextStyle(fontSize = 18.sp, color = fontLight),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = fontLight,
                    unfocusedTextColor = fontLight,
                    focusedBorderColor = mainLight,
                    unfocusedBorderColor = advanceLight,
                    focusedContainerColor = textformLight,
                    unfocusedContainerColor = textformLight,
                    focusedLabelColor = fontLight,
                    unfocusedLabelColor = fontLight
                ),
                shape = RoundedCornerShape(15.dp)
            )

            // VIN
            OutlinedTextField(
                value = vin,
                onValueChange = { vin = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("VIN", color = fontLight) },
                placeholder = { Text("17 символов", color = advanceLight) },
                textStyle = TextStyle(fontSize = 18.sp, color = fontLight),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = fontLight,
                    unfocusedTextColor = fontLight,
                    focusedBorderColor = mainLight,
                    unfocusedBorderColor = advanceLight,
                    focusedContainerColor = textformLight,
                    unfocusedContainerColor = textformLight,
                    focusedLabelColor = fontLight,
                    unfocusedLabelColor = fontLight
                ),
                shape = RoundedCornerShape(15.dp)
            )

            // Год выпуска
            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter { ch -> ch.isDigit() } },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Год выпуска", color = fontLight) },
                placeholder = { Text("2024", color = advanceLight) },
                textStyle = TextStyle(fontSize = 18.sp, color = fontLight),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = fontLight,
                    unfocusedTextColor = fontLight,
                    focusedBorderColor = mainLight,
                    unfocusedBorderColor = advanceLight,
                    focusedContainerColor = textformLight,
                    unfocusedContainerColor = textformLight,
                    focusedLabelColor = fontLight,
                    unfocusedLabelColor = fontLight
                ),
                shape = RoundedCornerShape(15.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = mainLight,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Кнопки
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = advanceLight
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = "Отмена",
                    fontSize = 18.sp,
                    color = backgroundLight
                )
            }

            Button(
                onClick = {
                    errorMessage = null
                    if (selectedCar == null) {
                        errorMessage = "Выберите модель автомобиля"
                        return@Button
                    }
                    val yearInt = year.toIntOrNull()
                    if (gosNomer.isBlank() || vin.isBlank() || yearInt == null) {
                        errorMessage = "Заполните все поля"
                        return@Button
                    }
                    coroutineScope.launch {
                        try {
                            userCarViewModel.addUserCar(
                                UserCar(
                                    userId = userId,
                                    carId = selectedCar!!.id ?: 0,
                                    gosNomer = gosNomer,
                                    vin = vin,
                                    year = yearInt
                                )
                            )
                            userCarViewModel.loadUserCars(userId)
                            navController.popBackStack()
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Ошибка при сохранении"
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainLight
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = "Сохранить",
                    fontSize = 18.sp,
                    color = backgroundLight
                )
            }
        }
        }
    }
}

