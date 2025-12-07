package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.commonfunction.Navigation
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundAdvanceLight
import com.example.sgmautotreckerapp.ui.theme.fontLight
import com.example.sgmautotreckerapp.ui.theme.mainLight


@Composable
public fun Header_Profile() {
    Box(modifier = Modifier
        .background(color = mainLight)
        .fillMaxWidth()
        .fillMaxHeight(0.10f),

        Alignment.BottomCenter
    ) {
        Text(text = "Profile",fontSize = 60.sp,color =backgroundAdvanceLight, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
    }
}

@Composable
public fun Info_Profile() {
    Spacer(Modifier
        .fillMaxHeight(0.05f))
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.33f),

        horizontalArrangement = Arrangement.Center

    ) {
        Box(Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
            .background(color = fontLight, shape = RoundedCornerShape(25.dp)),
            contentAlignment = Alignment.TopCenter

        ){
            Row(Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
                Text(text = "Выход", Modifier.padding(top = 20.dp, end = 20.dp))

            }
            Column(Modifier
                .padding(top = 20.dp)
                .fillMaxHeight(0.4f)
                .fillMaxWidth(0.33f)
                .background(color = backgroundAdvanceLight, shape = RoundedCornerShape(25.dp)))
            {


            }
            Column(Modifier
                .padding(top = 160.dp)) {
                Text("Дмитрий Нагиев",fontSize = 20.sp,color =backgroundAdvanceLight, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)

            }
            Column(Modifier
                .padding(top = 220.dp)) {
                Text("Dimka_Nagiev@mail.ru",fontSize = 14.sp,color =backgroundAdvanceLight, fontStyle = FontStyle.Italic)
            }


        }

    }

}


@Composable
public fun Text_Profile(){
    Spacer(Modifier
        .fillMaxHeight(0.05f))
    Text("Главная", Modifier.padding(start = 20.dp), color = fontLight, fontWeight = FontWeight.Bold,fontSize = 20.sp, fontStyle = FontStyle.Italic)
}

@Composable
public fun Button_Profile1(){
    Spacer(Modifier
        .fillMaxHeight(0.05f))
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),

        horizontalArrangement = Arrangement.Center

    ) {
        Row(Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
            .background(color = mainLight, shape = RoundedCornerShape(25.dp)),)
        {
            Column(modifier = Modifier
                .aspectRatio(1f)
                .fillMaxHeight()
                .background(color = mainLight, shape = RoundedCornerShape(25.dp)),
                Arrangement.Center, Alignment.CenterHorizontally) {
                Box(Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.8f)
                    .background(color = fontLight,shape = RoundedCornerShape(25.dp)))

                {

                }

            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = mainLight , shape = RoundedCornerShape(25.dp)),
            ) {
                Text("Настройка профиля", Modifier.padding(top = 20.dp, start = 20.dp), color = backgroundAdvanceLight, fontWeight = FontWeight.Bold,fontSize = 20.sp, fontStyle = FontStyle.Italic)
                Text("Обнови или модифицируй свой профиль", Modifier.padding(top = 20.dp, start = 20.dp), color = backgroundAdvanceLight,fontSize = 12.sp, fontStyle = FontStyle.Italic)

            }

        }


    }


}

@Composable
public fun Button_Profile2(){
    Spacer(Modifier
        .fillMaxHeight(0.05f))
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),

        horizontalArrangement = Arrangement.Center

    ) {
        Row(Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
            .background(color = mainLight, shape = RoundedCornerShape(25.dp)),)
        {
            Column(modifier = Modifier
                .aspectRatio(1f)
                .fillMaxHeight()
                .background(color = mainLight, shape = RoundedCornerShape(25.dp)),
                Arrangement.Center, Alignment.CenterHorizontally) {
                Box(Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.8f)
                    .background(color = fontLight,shape = RoundedCornerShape(25.dp)))

                {

                }

            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = mainLight , shape = RoundedCornerShape(25.dp)),
            ) {
                Text("Безопасность", Modifier.padding(top = 20.dp, start = 20.dp), color = backgroundAdvanceLight, fontWeight = FontWeight.Bold,fontSize = 20.sp, fontStyle = FontStyle.Italic)
                Text("Изменить пароль", Modifier.padding(top = 20.dp, start = 20.dp), color = backgroundAdvanceLight,fontSize = 12.sp, fontStyle = FontStyle.Italic)

            }

        }


    }


}


@Composable
public fun Profile(){
    Background()
    Column {
        Header_Profile()
        Info_Profile()
        Text_Profile()
        Button_Profile1()
        Button_Profile2()
        Navigation()
    }
}
