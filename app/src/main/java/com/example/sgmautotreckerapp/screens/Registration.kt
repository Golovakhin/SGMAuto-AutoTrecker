package com.example.sgmautotreckerapp.screens

import android.provider.ContactsContract
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import com.example.sgmautotreckerapp.ui.theme.textformLight



@Composable
public fun Registration(){
    Column(Modifier.background(backgroundLight).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.padding(45.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = "SGM", fontSize = 64.sp, fontWeight = FontWeight.Bold, color = fontLight)
            Text(text = "-", Modifier.padding(bottom = 10.dp), fontSize = 32.sp, color = fontLight)
            Text(text = "Auto", Modifier.padding(bottom = 8.dp), fontSize = 36.sp, color = fontLight)
        }

        Spacer(Modifier.fillMaxHeight(0.1f))
        Row() {
            Text(text = "Регистрация", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = fontLight)
        }


        val emailReg = remember{mutableStateOf("")}
        val passwordReg = remember{mutableStateOf("")}
        val confirmPassReg = remember{mutableStateOf("")}

        Row(Modifier.fillMaxHeight(0.5f).fillMaxWidth(0.8f)) {

            Column() {
                Spacer(Modifier.fillMaxWidth(1f).weight(1f))

                Row(Modifier.fillMaxWidth().weight(1f).clip(shape = RoundedCornerShape(10.dp))) {

                    OutlinedTextField(
                        value = emailReg.value,
                        textStyle = TextStyle(fontSize = 20.sp),
                        onValueChange = {newText -> emailReg.value = newText},
                        singleLine = true,
                        placeholder = {Text(text = "Почта", fontSize = 20.sp, color = fontLight)},

                        modifier =  Modifier.fillMaxSize().background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))


                    )
                }

                Spacer(Modifier.fillMaxWidth(1f).weight(1f))

                Row(Modifier.fillMaxWidth().weight(1f).clip(shape = RoundedCornerShape(10.dp))) {
                    OutlinedTextField(
                        value = passwordReg.value,
                        textStyle = TextStyle(fontSize = 20.sp),
                        onValueChange = {newText -> passwordReg.value = newText},
                        singleLine = true,
                        placeholder = {Text(text = "Пароль", fontSize = 20.sp, color = fontLight)},

                        modifier =  Modifier.fillMaxSize().background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))


                    )
                }

                Spacer(Modifier.fillMaxWidth(1f).weight(1f))

                Row(Modifier.fillMaxWidth().weight(1f).clip(shape = RoundedCornerShape(10.dp))) {
                    OutlinedTextField(
                        value = confirmPassReg.value,
                        textStyle = TextStyle(fontSize = 20.sp),
                        onValueChange = {newText -> confirmPassReg.value = newText},
                        singleLine = true,
                        placeholder = {Text(text = "Подтвердите пароль", fontSize = 20.sp, color = fontLight)},

                        modifier =  Modifier.fillMaxSize().background(textformLight).border(width = 3.dp, color = advanceLight, shape = RoundedCornerShape(10.dp))


                    )
                }
            }

        }

        Spacer(Modifier.fillMaxHeight(0.15f))

        Button(onClick = {}, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth(0.7f).fillMaxHeight(0.2f), colors = ButtonDefaults.buttonColors(mainLight)) {
            Text(text = "Создать профиль", fontSize = 18.sp, color = textformLight)
        }
    }
}