package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.R
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.MainContent
import com.example.sgmautotreckerapp.data.viewmodel.CarViewModel
import com.example.sgmautotreckerapp.data.viewmodel.UserCarViewModel
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.circleColor
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight
import kotlinx.coroutines.launch
import coil.compose.AsyncImage

@Composable
fun GarageScreen(
    userId: Int,
    navController: NavController,
    carViewModel: CarViewModel = hiltViewModel(),
    userCarViewModel: UserCarViewModel = hiltViewModel()
) {
    val userCars by userCarViewModel.userCars.collectAsState()
    val allCars by carViewModel.allCars.collectAsState()

    val carsById = remember(allCars) { allCars.associateBy { it.id } }

    LaunchedEffect(userId) {
        userCarViewModel.loadUserCars(userId)
        carViewModel.loadAllCars()
    }

    MainContent(
        navController = navController,
        userId = userId,
        onAddClick = { navController.navigate("addCar/$userId") },
        contentFunctions = listOf(
            { Header() },
            {
                if (userCars.isEmpty()) {
                    Spacer(Modifier.height(24.dp))
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Нет сохранённых автомобилей",
                            color = fontLight
                        )
                    }
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
                                gosNumber = userCar.gosNomer,
                                imageUrl = car?.imageURl,
                                userCarId = userCar.id,
                                onDelete = { userCar.id?.let { userCarViewModel.deleteUserCar(it, userId) } }
                            )
                        }
                    }
                }
            },
            { NewCar(onAdd = { navController.navigate("addCar/$userId") }) }
        )
    )
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
    gosNumber: String,
    imageUrl: String? = null,
    userCarId: Int? = null,
    onDelete: (() -> Unit)? = null
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
                // Верхняя часть: название машины по центру и кнопка удаления
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = mark, fontSize = 30.sp, color = backgroundLight)
                            Text(text = model, fontSize = 26.sp, color = backgroundLight)
                        }
                    }
                    // Кнопка удаления в правом верхнем углу
                    if (userCarId != null && onDelete != null) {
                        Button(
                            onClick = onDelete,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = circleColor.fourthColor
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "×",
                                color = textformLight,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Нижняя часть: характеристики слева, картинка в правом нижнем углу
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp, end = 10.dp)
                ) {
                    // Характеристики слева
                    Column(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 15.dp),
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

                    // Иконка машины в правом нижнем углу
                    Box(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .size(120.dp)
                    ) {
                        if (!imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "$mark $model",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                                error = painterResource(id = R.drawable.ic_launcher_foreground)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "$mark $model",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
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
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onAdd,
                        modifier = Modifier.size(85.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundLight
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "+",
                            color = fontLight,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


