package com.example.sgmautotreckerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sgmautotreckerapp.screens.AddCarScreen
import com.example.sgmautotreckerapp.screens.AnalysisScreen
import com.example.sgmautotreckerapp.screens.CategoryScreen
import com.example.sgmautotreckerapp.screens.GarageScreen
import com.example.sgmautotreckerapp.screens.LoginScreen
import com.example.sgmautotreckerapp.screens.MainScreen
import com.example.sgmautotreckerapp.screens.Profile
import com.example.sgmautotreckerapp.screens.RegistrationScreen

// Импортируем тестовый экран

// Объект с названиями маршрутов
object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main/{userId}"
    const val ANALYSIS = "analysis/{userId}"
    const val GARAGE = "garage/{userId}"
    const val PROFILE = "profile/{userId}"
    const val ADD_CAR = "addCar/{userId}"
    const val CATEGORY = "category"
    
    // Функции для создания маршрутов с параметрами
    fun mainRoute(userId: Int) = "main/$userId"
    fun analysisRoute(userId: Int) = "analysis/$userId"
    fun garageRoute(userId: Int) = "garage/$userId"
    fun profileRoute(userId: Int) = "profile/$userId"
    fun addCarRoute(userId: Int) = "addCar/$userId"
    fun categoryRoute() = CATEGORY
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppRoutes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Экран входа
        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController)
        }
        
        // Экран регистрации
        composable(AppRoutes.REGISTER) {
            RegistrationScreen(navController = navController)
        }
        
        // Главный экран (SGM-Auto)
        composable(
            route = AppRoutes.MAIN,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            MainScreen(navController = navController, userId = userId)
        }
        
        // Экран статистики (Analysis)
        composable(
            route = AppRoutes.ANALYSIS,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            AnalysisScreen(
                userId = userId,
                navController = navController
            )
        }
        
        // Экран гаража
        composable(
            route = AppRoutes.GARAGE,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            GarageScreen(
                userId = userId,
                navController = navController
            )
        }
        
        // Экран профиля с userId
        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            Profile(
                navController = navController,
                userId = userId
            )
        }
        
        // Экран профиля без userId (для навигации без авторизации)
        composable("profile") {
            Profile(
                navController = navController,
                userId = null
            )
        }
        
        // Экран добавления машины
        composable(
            route = AppRoutes.ADD_CAR,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            AddCarScreen(
                userId = userId,
                navController = navController
            )
        }

        // Экран выбора категории расходов
        composable(AppRoutes.CATEGORY) {
            CategoryScreen()
        }
    }
}

