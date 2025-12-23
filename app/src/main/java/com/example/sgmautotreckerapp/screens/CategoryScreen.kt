package com.example.sgmautotreckerapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sgmautotreckerapp.R
import com.example.sgmautotreckerapp.commonfunction.Background
import com.example.sgmautotreckerapp.navigation.AppRoutes
import com.example.sgmautotreckerapp.ui.theme.advanceLight
import com.example.sgmautotreckerapp.ui.theme.backgroundLight
import com.example.sgmautotreckerapp.ui.theme.fontLight

@Composable
private fun CategoryScreenAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(advanceLight),
        horizontalArrangement = Arrangement.Center,
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

@Composable
private fun CategoryHeader(navController: NavController?) {
    Spacer(modifier = Modifier.height(24.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back_arrow_category),
            contentDescription = "Назад",
            modifier = Modifier
                .padding(start = 4.dp)
                .size(32.dp)
                .clickable { navController?.popBackStack() },
            colorFilter = ColorFilter.tint(fontLight)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Выберите категорию",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = fontLight
        )
    }
}

@Composable
private fun CategoryCard(
    iconRes: Int,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = advanceLight, shape = RoundedCornerShape(25.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(64.dp),
                colorFilter = ColorFilter.tint(fontLight)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = fontLight
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = fontLight
                )
            }
        }
    }
}

@Composable
private fun CategoriesGrid(navController: NavController?, userId: Int) {
    Spacer(modifier = Modifier.height(32.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryCard(
                iconRes = R.drawable.ic_fuel,
                title = "Топливо",
                onClick = { navController?.navigate(AppRoutes.categoryFuelRoute(userId)) },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
            CategoryCard(
                iconRes = R.drawable.ic_repair,
                title = "Ремонт",
                onClick = { navController?.navigate(AppRoutes.categoryServiceRoute(userId)) },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryCard(
                iconRes = R.drawable.ic_parking,
                title = "Парковки",
                subtitle = "Платные дороги",
                onClick = { navController?.navigate(AppRoutes.categoryParkingRoadRoute(userId)) },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
            CategoryCard(
                iconRes = R.drawable.ic_policeman,
                title = "Штрафы",
                subtitle = "Налоги",
                onClick = { navController?.navigate(AppRoutes.categoryFinesTaxesRoute(userId)) },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryCard(
                iconRes = R.drawable.ic_car_wash,
                title = "Мойка",
                onClick = { navController?.navigate(AppRoutes.categoryCarWashRoute(userId)) },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
            CategoryCard(
                iconRes = R.drawable.ic_fail,
                title = "Страхование",
                onClick = { navController?.navigate(AppRoutes.categoryInsuranceRoute(userId)) },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CategoryCard(
                iconRes = R.drawable.ic_another,
                title = "Прочее",
                onClick = { navController?.navigate(AppRoutes.categoryOthersRoute(userId)) },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
public fun CategoryScreen(navController: NavController? = null, userId: Int) {
    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        CategoryScreenAppBar()
        CategoryHeader(navController)
        CategoriesGrid(navController, userId)
    }
}


