package com.example.sgmautotreckerapp.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.CommonHeader
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight

@Composable

private fun ServiceText(){
    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().fillMaxHeight(0.05f)) {
        Column(Modifier.fillMaxWidth(0.1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "gg", fontSize = 16.sp)
        }
        Spacer(Modifier.fillMaxWidth(0.25f).height(30.dp))

        Column(Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
            Text(text = "Ремонт", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = fontLight)
        }
    }
}



@Composable
private fun ContentService(){
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.9f).fillMaxHeight()) {

            val dateService = remember{mutableStateOf("")}
            val sumService = remember{mutableStateOf("")}
            val distService = remember{mutableStateOf("")}
            val commentService = remember{mutableStateOf("")}

            //Дата траты
            Row(Modifier.padding(top = 10.dp)) {
                Text(text = "Выберите дату", fontSize = 20.sp, color = fontLight, fontWeight = FontWeight.SemiBold)
            }
            Row(Modifier.padding(start =15.dp, top = 5.dp ).clip(RoundedCornerShape(10.dp))) {
                OutlinedTextField(
                    value = dateService.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = {newText -> dateService.value = newText},
                    singleLine = true,
                    placeholder = {Text(text = "Выбрать", fontSize = 15.sp, color = fontLight, fontStyle = FontStyle.Italic)},

                    modifier =  Modifier.fillMaxWidth(0.6f).height(55.dp).background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))


                )
            }



            //Сумма траты
            Row(Modifier.padding(top = 20.dp)) {
                Text(text = "Сумма траты", fontSize = 20.sp, color = fontLight, fontWeight = FontWeight.SemiBold)
            }
            Row(Modifier.padding(start =15.dp, top = 5.dp ).clip(RoundedCornerShape(10.dp))) {
                OutlinedTextField(
                    value = sumService.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = {newText -> sumService.value = newText},
                    singleLine = true,
                    placeholder = {Text(text = "Сумма, ₽", fontSize = 15.sp, color = fontLight, fontStyle = FontStyle.Italic)},

                    modifier =  Modifier.fillMaxWidth(0.6f).height(55.dp).background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))


                )
            }

            //пробег
            Row(Modifier.padding(top = 20.dp)) {
                Text(text = "Укажите пробег", fontSize = 20.sp, color = fontLight, fontWeight = FontWeight.SemiBold)
            }
            Row(Modifier.padding(start =15.dp, top = 5.dp ).clip(RoundedCornerShape(10.dp))) {
                OutlinedTextField(
                    value = distService.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = {newText -> distService.value = newText},
                    singleLine = true,
                    placeholder = {Text(text = "Км", fontSize = 15.sp, color = fontLight, fontStyle = FontStyle.Italic)},

                    modifier =  Modifier.fillMaxWidth(0.4f).height(55.dp).background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))


                )
            }

            //комментарий
            Row(Modifier.padding(top = 20.dp)) {
                Text(text = "Укажите комментарий", fontSize = 20.sp, color = fontLight, fontWeight = FontWeight.SemiBold)
            }

            Box(Modifier.padding(start =15.dp, top = 5.dp ).clip(RoundedCornerShape(10.dp))) {
                OutlinedTextField(
                    value = commentService.value,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = {newText -> commentService.value = newText},
                    singleLine = false,
                    placeholder = {Text(text = "Комментарий", fontSize = 15.sp, color = fontLight, fontStyle = FontStyle.Italic)},

                    modifier =  Modifier.fillMaxWidth(0.95f).align(Alignment.TopStart).height(180.dp).background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))

                )
            }

            Spacer(Modifier.fillMaxWidth().height(30.dp))
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Row() {
                    Column(Modifier.fillMaxWidth(0.25f), horizontalAlignment = Alignment.End) {
                        Text(text = "gg", fontSize = 15.sp)
                    }

                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                        Text(text = "Загрузить фото, документ, чек", fontSize = 15.sp)
                    }
                }
            }

            Spacer(Modifier.fillMaxWidth().height(40.dp))

            Box(Modifier.fillMaxWidth().height(72.dp), contentAlignment = Alignment.Center) {

                Button(onClick = {}, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth(0.7f).height(72.dp), colors = ButtonDefaults.buttonColors(mainLight)) {
                    Text(text = "Записать расход", fontSize = 22.sp, color = textformLight, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}






@Composable
public fun ServiceScreen(){
    Background()
    Column() {
        CommonHeader(advanceLight, fontLight, "Expanses")
        ServiceText()
        ContentService()
    }

}