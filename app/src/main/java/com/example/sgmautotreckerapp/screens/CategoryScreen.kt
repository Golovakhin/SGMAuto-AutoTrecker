package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight

@Composable
private fun CategoryScreenAppBar() {
    Column() {
        Row(
            Modifier.fillMaxHeight(0.1f).fillMaxWidth().background(advanceLight),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Expanses",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = fontLight
            )

        }
    }
}



@Composable
private fun SelectCategoryText(){
    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().fillMaxHeight(0.05f).background(Color.Gray)) {

        Column(Modifier.fillMaxWidth(0.2f).fillMaxHeight().background(Color.Red), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "gg", fontSize = 16.sp)
        }

        Column(Modifier.fillMaxWidth().fillMaxHeight().background(Color.Green), verticalArrangement = Arrangement.Center) {
            Text(text = "Выберите категорию", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        }
    }
}





@Composable
private fun CategoriesIcons(){
    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().height(152.dp).background(Color.Gray), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.8f).fillMaxHeight()) {
            Row() {
                Box(Modifier.fillMaxWidth(0.45f).fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp)), contentAlignment = Alignment.Center) {
                    Column() {

                        //Image()
                        Text(text = "Топливо", fontSize = 26.sp)
                    }
                }

                Box(Modifier.fillMaxWidth(0.2f).fillMaxHeight().background(backgroundLight)) {

                }

                Box(Modifier.fillMaxWidth().fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }
            }
        }
    }

    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().height(152.dp).background(Color.Gray), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.8f).fillMaxHeight()) {
            Row() {
                Box(Modifier.fillMaxWidth(0.45f).fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }

                Box(Modifier.fillMaxWidth(0.2f).fillMaxHeight().background(backgroundLight)) {

                }

                Box(Modifier.fillMaxWidth().fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }
            }
        }
    }

    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().height(152.dp).background(Color.Gray), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.8f).fillMaxHeight()) {
            Row() {
                Box(Modifier.fillMaxWidth(0.45f).fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }

                Box(Modifier.fillMaxWidth(0.2f).fillMaxHeight().background(backgroundLight)) {

                }

                Box(Modifier.fillMaxWidth().fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }
            }
        }
    }

    Spacer(Modifier.fillMaxWidth().height(30.dp))
    Row(Modifier.fillMaxWidth().height(152.dp).background(Color.Gray), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.fillMaxWidth(0.8f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                Box(Modifier.width(152.dp).height(152.dp).background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }
            }
        }
    }

}






@Composable
public fun CategoryScreen(){
    Background()
    Column {
        CategoryScreenAppBar()
        SelectCategoryText()
        CategoriesIcons()
    }
}