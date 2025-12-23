package com.example.sgmautotreckerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.sgmautotreckerapp.data.preferences.ThemePreferences
import com.example.sgmautotreckerapp.navigation.AppNavigation
import com.example.sgmautotreckerapp.ui.theme.SGMAutoTreckerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themePreferences = ThemePreferences(applicationContext)
            val isDarkTheme = remember { mutableStateOf(themePreferences.isDarkTheme()) }
            
            SGMAutoTreckerAppTheme(darkTheme = isDarkTheme.value) {
                AppNavigation(
                    onThemeChange = { isDark ->
                        isDarkTheme.value = isDark
                        themePreferences.setDarkTheme(isDark)
                    }
                )
            }
        }
    }
}
