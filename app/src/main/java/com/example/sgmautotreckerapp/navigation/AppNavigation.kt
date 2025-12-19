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
import com.example.sgmautotreckerapp.screens.CategoryScreens.CarWashScreen
import com.example.sgmautotreckerapp.screens.CategoryScreens.FinesTaxesScreen
import com.example.sgmautotreckerapp.screens.CategoryScreens.FuelScreen
import com.example.sgmautotreckerapp.screens.CategoryScreens.InsuranceScreen
import com.example.sgmautotreckerapp.screens.CategoryScreens.OthersScreen
import com.example.sgmautotreckerapp.screens.CategoryScreens.ParkingRoadScreen
import com.example.sgmautotreckerapp.screens.CategoryScreens.ServiceScreen

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
    const val CATEGORY = "category/{userId}"
    const val CATEGORY_FUEL = "category_fuel/{userId}"
    const val CATEGORY_SERVICE = "category_service/{userId}"
    const val CATEGORY_PARKING_ROAD = "category_parking_road/{userId}"
    const val CATEGORY_FINES_TAXES = "category_fines_taxes/{userId}"
    const val CATEGORY_CAR_WASH = "category_car_wash/{userId}"
    const val CATEGORY_INSURANCE = "category_insurance/{userId}"
    const val CATEGORY_OTHERS = "category_others/{userId}"
    
    // Функции для создания маршрутов с параметрами
    fun mainRoute(userId: Int) = "main/$userId"
    fun analysisRoute(userId: Int) = "analysis/$userId"
    fun garageRoute(userId: Int) = "garage/$userId"
    fun profileRoute(userId: Int) = "profile/$userId"
    fun addCarRoute(userId: Int) = "addCar/$userId"
    fun categoryRoute(userId: Int) = "category/$userId"
    fun categoryFuelRoute(userId: Int) = "category_fuel/$userId"
    fun categoryServiceRoute(userId: Int) = "category_service/$userId"
    fun categoryParkingRoadRoute(userId: Int) = "category_parking_road/$userId"
    fun categoryFinesTaxesRoute(userId: Int) = "category_fines_taxes/$userId"
    fun categoryCarWashRoute(userId: Int) = "category_car_wash/$userId"
    fun categoryInsuranceRoute(userId: Int) = "category_insurance/$userId"
    fun categoryOthersRoute(userId: Int) = "category_others/$userId"
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
        composable(
            route = AppRoutes.CATEGORY,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            CategoryScreen(navController = navController, userId = userId)
        }

        // Экраны форм по категориям расходов
        composable(
            route = AppRoutes.CATEGORY_FUEL,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            FuelScreen(navController = navController, userId = userId)
        }
        composable(
            route = AppRoutes.CATEGORY_SERVICE,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            ServiceScreen(navController = navController, userId = userId)
        }
        composable(
            route = AppRoutes.CATEGORY_PARKING_ROAD,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            ParkingRoadScreen(navController = navController, userId = userId)
        }
        composable(
            route = AppRoutes.CATEGORY_FINES_TAXES,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            FinesTaxesScreen(navController = navController, userId = userId)
        }
        composable(
            route = AppRoutes.CATEGORY_CAR_WASH,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            CarWashScreen(navController = navController, userId = userId)
        }
        composable(
            route = AppRoutes.CATEGORY_INSURANCE,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            InsuranceScreen(navController = navController, userId = userId)
        }
        composable(
            route = AppRoutes.CATEGORY_OTHERS,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            OthersScreen(navController = navController, userId = userId)
        }
    }
}

