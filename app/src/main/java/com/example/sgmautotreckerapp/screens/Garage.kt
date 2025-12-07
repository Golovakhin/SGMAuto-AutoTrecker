package com.example.sgmautotreckerapp.screens

import android.media.Image
import android.preference.PreferenceActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.R
import com.example.sgmautotreckerapp.ui.theme.fontLight
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.Navigation
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.mainLight
import org.w3c.dom.Text

@Composable
public fun Garage(){
    Background()
    Column() {
        Header()
        GarageCar("Honda Civic", "Type R", "XI", 2025, "Р805ХР33", "22")
        NewCar()
        Navigation()

    }

}
@Composable
public fun GarageCar(mark: String,model:String, generation: String, year: Int, gosNumber: String, image: String) {
    Row(Modifier.fillMaxWidth().padding(top = 40.dp).height(200.dp), horizontalArrangement = Arrangement.Center) {
        Box(
            Modifier.fillMaxWidth(0.9f).fillMaxHeight()
                .height(200.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(fontLight)
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().fillMaxHeight(0.5f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = mark, fontSize = 30.sp, color = backgroundLight)
                        Text(text = model, fontSize = 30.sp, color = backgroundLight)
                    }

                }
                Row(Modifier.fillMaxSize()) {
                    Row(Modifier.fillMaxHeight().fillMaxWidth(0.4f), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.padding(start = 15.dp, bottom = 10.dp), verticalArrangement = Arrangement.Center) {
                            Text(text = "Year: $year", fontSize = 14.sp, lineHeight = 23.sp, color = backgroundLight)
                            Text(text = "Generation: $generation", fontSize = 14.sp, lineHeight = 23.sp, color = backgroundLight)
                            Text(text = "Number: $gosNumber", fontSize = 14.sp, lineHeight = 23.sp, color = backgroundLight)
                        }
                    }
                    Row(Modifier.fillMaxSize()) {
                        Image(
                            bitmap = ImageBitmap.imageResource(R.drawable.civic),
                            contentDescription = "22",
                            modifier = Modifier.fillMaxSize()
                        )
                    }


                }
            }
        }

    }
}

@Composable
private fun NewCar(){
    Row(Modifier.fillMaxWidth().padding(top = 40.dp).height(200.dp), horizontalArrangement = Arrangement.Center){
        Box(
            Modifier.fillMaxWidth(0.9f).fillMaxHeight()
                .height(200.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(fontLight)
        ) {
            Column() {
                Row(Modifier.fillMaxWidth().fillMaxHeight(0.4f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Add new car", color = backgroundLight, fontSize = 30.sp,)
                }
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                    Canvas(Modifier.size(85.dp).clip(shape = CircleShape)) {

                        drawCircle(
                            color = backgroundLight,
                            radius = 42.5.dp.toPx(),

                        )

                        drawLine(
                            color = fontLight,
                            start = Offset(x = size.width/3,y = size.height/2),
                            end = Offset(x = size.width - size.width/3,y = size.height/2),
                            strokeWidth = 20f,
                            cap = StrokeCap.Round

                        )
                        drawLine(
                            color = fontLight,
                            start = Offset(x = size.width/2,y = size.height/3),
                            end = Offset(x = size.width/2,y = size.height - size.height/3),
                            strokeWidth = 20f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun Header(){
Row(Modifier.fillMaxWidth().fillMaxHeight(0.10f).background(fontLight), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
    Text(text = "Garage", fontSize = 60.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, color = backgroundLight)
}
}
