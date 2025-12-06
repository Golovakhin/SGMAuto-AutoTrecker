package com.example.sgmautotreckerapp.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.Segment
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.circleColor
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight

@Composable
fun AnalysisRing() {
    val segments = listOf(
        Segment(circleColor.firstColor, 90f),
        Segment(circleColor.secondColor,90f),
        Segment(circleColor.thirdColor,90f),
        Segment(circleColor.fourthColor,90f),
    )
    Box( modifier = Modifier.padding(top = 25.dp), contentAlignment =  Alignment.Center){
        Canvas(Modifier.fillMaxWidth().fillMaxHeight(0.4f).padding(start = 75.dp, end = 75.dp, top = 0.dp)) {
            var currentStartAngle = 0f
            for (segment in segments) {

                drawArc(
                    color=segment.color,
                    startAngle = currentStartAngle,
                    sweepAngle = segment.ratio,
                    useCenter = false,
                    style = Stroke(width = 15.dp.toPx()),
                    size = Size(size.width, size.width)
                )
                currentStartAngle += segment.ratio
            }
        }
    }
}


@Composable
fun Navigation(){
    Column(Modifier
        .fillMaxWidth().fillMaxHeight()
    ){
        Row(Modifier.fillMaxWidth().fillMaxHeight(0.5f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom){
            Box(Modifier
                .fillMaxWidth(1.55f/5.55f)
                .fillMaxHeight(0.5f)
                .align(Alignment.Bottom),
                contentAlignment = Alignment.BottomCenter){
                Canvas(Modifier.fillMaxSize())  {}

            }
        }
        Row(Modifier.background(color = advanceLight).fillMaxWidth().fillMaxHeight(),
            verticalAlignment = Alignment.Top){

            Box(Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center){
                Text("GG")
            }

            Box(Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center){
                Text("GG")
            }

            Box(Modifier
                .fillMaxHeight(0.5f)
                .weight(1.55f),
                contentAlignment = Alignment.TopCenter){
                Canvas(Modifier.fillMaxSize())
                {
                    drawCircle(
                        color = backgroundLight,
                        radius = 60.dp.toPx(),
                        center = Offset(x= size.width/2,y=0f)
                    )

                    drawCircle(
                        color = mainLight,
                        radius = 50.dp.toPx(),
                        center = Offset(x= size.width/2,y=0f)
                    )

                    drawLine(
                        color = backgroundLight,
                        start = Offset(x = size.width/3,y = 0f),
                        end = Offset(x = size.width - size.width/3,y = 0f),
                        strokeWidth = 20f,
                        cap = StrokeCap.Round

                    )
                    drawLine(
                        color = backgroundLight,
                        start = Offset(x = size.width/2,y = -size.height/3),
                        end = Offset(x = size.width/2,y = size.height/3),
                        strokeWidth = 20f,
                        cap = StrokeCap.Round
                    )
                }
            }

            Box(Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center){
                Text("GG")
            }

            Box(Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center){
                Text("GG")
            }
        }
    }
}


@Composable
private fun Background(){
    Row(modifier = Modifier.background(backgroundLight).fillMaxSize()) {

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
private fun Info(){
    Box(Modifier.padding(top = 75.dp, start = 50.dp,end = 50.dp, bottom = 75.dp)
        .clip(shape = RoundedCornerShape(30.dp))
        .background(color = advanceLight)
        .fillMaxWidth()
        .fillMaxHeight(0.1f),
        Alignment.Center
    ){
        Text("Все машины", color = Color.White, fontSize = 20.sp)
    }
}

@Composable
public fun Content(){
    Column() {
        Info()
        AnalysisRing()
        Legend()
        Navigation()
        Background()
    }
}