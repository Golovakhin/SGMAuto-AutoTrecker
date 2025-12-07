package com.example.sgmautotreckerapp.commonfunction

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.mainLight

@Composable
public fun Navigation(){
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


