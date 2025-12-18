package com.example.sgmautotreckerapp.screens

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.Segment
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.MainContent
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.circleColor
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import kotlin.math.round





@Composable
private fun AppBar (){
    Column() {
        Row(Modifier.fillMaxHeight(0.1f).fillMaxWidth().background(advanceLight), horizontalArrangement = Arrangement.Absolute.Center, verticalAlignment = Alignment.Bottom) {
            Text(text = "SGM", fontSize = 64.sp, fontWeight = FontWeight.Bold, color = fontLight)
            Text(text = "-", Modifier.padding(bottom = 10.dp), fontSize = 32.sp, color = fontLight)
            Text(text = "Auto", Modifier.padding(bottom = 8.dp), fontSize = 36.sp, color = fontLight)
        }
    }
}

@Composable
private fun CarMain() {
    Column() {
        Spacer(Modifier.fillMaxWidth().fillMaxHeight(0.05f))

        Row(Modifier.fillMaxWidth().height(250.dp), horizontalArrangement = Arrangement.Center) {
            Box(Modifier.background(fontLight, shape = RoundedCornerShape(25.dp)).fillMaxHeight().fillMaxWidth(0.8f))
            {
                Column() {
                    Row() {
                        Column(Modifier.fillMaxWidth().fillMaxHeight(0.3f).background(Color.Red), horizontalAlignment = Alignment.CenterHorizontally)
                        {
//                        Text(text = "Honda Civic", fontSize = 30.sp, color = backgroundLight, fontWeight = FontWeight.Normal)
//                        Text(text = "Type R", fontSize = 30.sp, color = backgroundLight, fontWeight = FontWeight.Normal)
                        }
                    }

                    Row() {
                        Column(Modifier.fillMaxWidth(0.5f).fillMaxHeight().background(Color.Green), verticalArrangement = Arrangement.Center) {

//                        Text(text = "Year: ", fontSize = 14.sp, color = backgroundLight, fontWeight = FontWeight.Normal)
//                        Text(text = "Generation: ", fontSize = 14.sp, color = backgroundLight, fontWeight = FontWeight.Normal)
//                        Text(text = "Number: ", fontSize = 14.sp, color = backgroundLight, fontWeight = FontWeight.Normal)
                        }
                        Column(Modifier.fillMaxSize().background(Color.Blue)) { }

                    }
                }

            }

        }
    }
}


@Composable
private fun Analitika(){
    Spacer(Modifier.fillMaxWidth().fillMaxHeight(0.05f))

    Row(Modifier.fillMaxWidth().fillMaxHeight(0.25f), horizontalArrangement = Arrangement.Center) {
        Box(Modifier.fillMaxWidth(0.8f).fillMaxHeight()){
            Row() {
                Column(Modifier.fillMaxWidth(0.5f).fillMaxHeight().background(mainLight, shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp))) {

                }

                Column(Modifier.fillMaxWidth().fillMaxHeight().background(mainLight, shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp))) {

                }
            }
        }
    }
}



@Composable
private fun AnotherBlocks(){

    Spacer(Modifier.fillMaxWidth().fillMaxHeight(0.08f))

    Row(Modifier.fillMaxWidth().fillMaxHeight(0.4f).background(backgroundLight), horizontalArrangement = Arrangement.Center) {
        Box(Modifier.fillMaxWidth(0.8f).fillMaxHeight() ) {
            Row() {
                Box(Modifier.fillMaxWidth(0.45f).fillMaxHeight().background(advanceLight, shape = RoundedCornerShape(25.dp))) {

                }

                Box(Modifier.fillMaxWidth(0.2f).fillMaxHeight().background(backgroundLight)) {

                }

                Box(Modifier.fillMaxWidth().fillMaxHeight().background(fontLight, shape = RoundedCornerShape(25.dp))) {

                }
            }
        }
    }
}












@Composable
private fun Legend(){

    val segments = listOf(
        Segment(circleColor.firstColor,90f),
        Segment(circleColor.secondColor,90f),
        Segment(circleColor.thirdColor,90f),
        Segment(circleColor.fourthColor,90f),
    )

    Box(
        Modifier.padding(30.dp)
            .border(width = 1.dp, color = advanceLight, shape = RoundedCornerShape(20.dp))
            .background(backgroundAdvanceLight, shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        contentAlignment = Alignment.TopStart
    ) {
        Row() {
            Column(Modifier.padding(top = 30.dp), verticalArrangement = Arrangement.Top) {
                for (segment in segments){

                    Canvas(Modifier.fillMaxWidth(0.15f)) {
                        drawCircle(color = segment.color, radius = 4.dp.toPx(),)

                    }
                    Spacer(Modifier.height(30.dp))
                }

            }

            Column(Modifier.padding(top = 22.dp)) {
                for (segment in segments){

                    Text(text = "GG", color = fontLight)
                    Spacer(Modifier.height(14.dp))
                }
            }
        }


    }
}















@Composable
public fun MainScreen(
    navController: androidx.navigation.NavController? = null,
    userId: Int? = null
){
    MainContent(
        navController = navController,
        userId = userId,
        contentFunctions = listOf(
            { AppBar() },
            { CarMain() },
            { Analitika() },
            { AnotherBlocks() }
        )
    )
}