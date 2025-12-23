package com.example.sgmautotreckerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sgmautotreckerapp.navigation.AppNavigation
import com.example.sgmautotreckerapp.ui.theme.SGMAutoTreckerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SGMAutoTreckerAppTheme {
                AppNavigation()
            }
        }
    }
}
