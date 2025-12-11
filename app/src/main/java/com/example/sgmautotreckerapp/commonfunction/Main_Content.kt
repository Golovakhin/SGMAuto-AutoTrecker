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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MainContent(

    contentFunctions: List<@Composable () -> Unit> = emptyList()
    ) {
    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = advanceLight) {
                Row(
                    Modifier.background(color = advanceLight).fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("GG")
                    }
                    Box(
                        Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("GG")
                    }
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1.55f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Canvas(Modifier.fillMaxSize())
                        {
                            drawCircle(
                                color = backgroundLight,
                                radius = 60.dp.toPx(),
                                center = Offset(x = size.width / 2, y = -10f)
                            )
                        }
                    }
                    Box(
                        Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("GG")
                    }
                    Box(
                        Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("GG")
                    }

                }
            }
        },


        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {},
                Modifier.offset(y = 64.dp),
                shape = CircleShape,
                containerColor = fontLight,
                contentColor = backgroundLight
            ) {
                Text("+", fontSize = 60.sp)
            }

        },

        floatingActionButtonPosition = FabPosition.Center
    ) {
        Background()
        Column() {
            contentFunctions.forEach { contentFunction ->
                contentFunction()
            }
        }

    }
}








@Composable
public fun CommonHeader(headerColor: Color,fontHeaderColor: Color,textHeader: String){
    Column() {
        Row(Modifier.fillMaxWidth().fillMaxHeight(0.1f).background(color =headerColor),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom) {
            Text(text = textHeader, fontSize = 60.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic,color = fontHeaderColor)
        }
    }
}