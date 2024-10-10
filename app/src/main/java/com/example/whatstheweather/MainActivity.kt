package com.example.whatstheweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.whatstheweather.ui.theme.WhatsTheWeatherTheme

class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            WhatsTheWeatherTheme {

                SearchScreen(viewModel = weatherViewModel)
            }
        }
    }
}
