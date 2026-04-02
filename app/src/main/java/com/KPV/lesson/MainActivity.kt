package com.kpv.lesson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.kpv.lesson.navscreens.weather.WeatherAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF00695C),
                    secondary = Color(0xFF4DB6AC),
                    background = Color(0xFFF0F4F8),
                    surface = Color.White
                )
            ) {
                WeatherAppScreen()
            }
        }
    }
}

