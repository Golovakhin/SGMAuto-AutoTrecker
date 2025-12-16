package com.example.sgmautotreckerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sgmautotreckerapp.screens.AnalysisScreen
import com.example.sgmautotreckerapp.screens.GarageScreen
import com.example.sgmautotreckerapp.screens.LoginScreen
import com.example.sgmautotreckerapp.screens.RegistrationScreen
import com.example.sgmautotreckerapp.ui.theme.SGMAutoTreckerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SGMAutoTreckerApp()
        }
    }
}

@Composable
private fun SGMAutoTreckerApp() {
    val navController = rememberNavController()

    SGMAutoTreckerAppTheme {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegistrationScreen(navController) }
            composable(
                route = "garage/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
                GarageScreen(userId = userId, navController = navController)
            }
            composable(
                route = "analysis/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
                AnalysisScreen(userId = userId, navController = navController)
            }
        }
    }
}