package com.example.whatstheweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whatstheweather.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherScreen(weatherViewModel)
        }

        weatherViewModel.fetchWeather(44.00,10.00)
    }
}

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel) {
    val weatherData = weatherViewModel.weatherData.observeAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        weatherData.value?.let {
            Text(text = "Temperature: ${it.main.temp}")
            Text(text = "Description: ${it.weather[0].description}")
        }
    }
}
