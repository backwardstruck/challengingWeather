package com.example.whatstheweather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.livedata.observeAsState
import java.util.Locale

@Composable
fun SearchScreen(viewModel: WeatherViewModel) {
    var cityName by remember { mutableStateOf("") }

    val coordinates by viewModel.coordinates.observeAsState()
    val weatherData by viewModel.weatherData.observeAsState()

    Column {
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            viewModel.fetchCoordinates(cityName)
        }) {
            Text("Get Coordinates")
        }

        coordinates?.let {
            Text(text = "Latitude: ${it[0].lat}, Longitude: ${it[0].lon}")
            Text(text = "Country: ${it[0].country}, State: ${it[0].state ?: "N/A"}")
        }

        weatherData?.let { weather ->
            val temperatureFahrenheit = (weather.main.temp - 273.15) * 9 / 5 + 32
            val temperatureCelsius = weather.main.temp - 273.15
            Text(text = "Temperature: %.2f°F / %.2f°C".format(temperatureCelsius, temperatureFahrenheit))
            Text(text = "Weather: ${weather.weather[0].description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }}")
        }
    }
}
