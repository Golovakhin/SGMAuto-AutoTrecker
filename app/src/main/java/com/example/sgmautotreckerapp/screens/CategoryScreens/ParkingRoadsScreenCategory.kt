package com.example.sgmautotreckerapp.screens.CategoryScreens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.CommonHeader
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight





@Composable
private fun ParkingRoadBlocks(){
    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().fillMaxHeight(0.1f)) {
        Column(Modifier.fillMaxWidth(0.1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Text(text = "gg", fontSize = 16.sp)
        }

        //Spacer(Modifier.width(10.dp))

        Column(Modifier.fillMaxWidth(0.9f).fillMaxHeight(), verticalArrangement = Arrangement.Top) {
            Box(Modifier.align(Alignment.CenterHorizontally)){
                Text(text = "Парковки и платные", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = fontLight)
            }
            Box(Modifier.align(Alignment.CenterHorizontally)){
                Text(text = "дороги", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = fontLight)
            }

        }
    }
}

@Composable
private fun ContentParkingRoad(){
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.9f).fillMaxHeight()) {

            val dateParkingRoad = remember { mutableStateOf("") }
            val sumParkingRoad = remember { mutableStateOf("") }

            //Дата траты
            Row(Modifier.padding(top = 10.dp)) {
                Text(
                    text = "Выберите дату",
                    fontSize = 26.sp,
                    color = fontLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(Modifier.padding(start = 15.dp, top = 5.dp).clip(RoundedCornerShape(10.dp))) {
                OutlinedTextField(
                    value = dateParkingRoad.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = { newText -> dateParkingRoad.value = newText },
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Выбрать",
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
                    value = sumParkingRoad.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = { newText -> sumParkingRoad.value = newText },
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

            Box(Modifier.fillMaxWidth().height(72.dp), contentAlignment = Alignment.Center) {

                Button(onClick = {}, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth(0.7f).height(72.dp), colors = ButtonDefaults.buttonColors(mainLight)) {
                    Text(text = "Записать расход", fontSize = 22.sp, color = textformLight, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}







@Composable
public fun ParkingRoadScreen(){
    Background()
    Column {
        CommonHeader(advanceLight, fontLight, "Expanses")
        ParkingRoadBlocks()
        ContentParkingRoad()
    }
}