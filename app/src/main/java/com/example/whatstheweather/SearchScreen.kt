package com.example.whatstheweather

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.util.Locale
import kotlin.math.roundToInt

const val MAX_LINES = 1
const val ICON_SIZE_DP = 200
const val PADDING_DP = 32
const val KELVIN_TO_CELSIUS_OFFSET = 273.15
const val KELVIN_TO_FAHRENHEIT_MULTIPLIER = 9.0 / 5.0
const val FAHRENHEIT_OFFSET = 32
const val METER_PER_SEC_TO_MPH = 2.23694

@Composable
fun SearchScreen(
    viewModel: WeatherViewModel,
    sharedPreferences: SharedPreferences,
    placeName: String,
    onCityNameChange: (String) -> Unit
) {
    val coordinates by viewModel.coordinates.observeAsState()
    val weatherData by viewModel.weatherData.observeAsState()

    var locationSetFromGPS by remember { mutableStateOf(false) }

    coordinates?.let {
        if (it.isNotEmpty() && !locationSetFromGPS) {
            onCityNameChange(it[0].name)
            locationSetFromGPS = true
        }
    }

    Column {
        TextField(
            value = placeName,
            onValueChange = { onCityNameChange(it) },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = MAX_LINES
        )

        Button(onClick = {
            viewModel.fetchCoordinates(placeName)

            with(sharedPreferences.edit()) {
                putString("lastPlace", placeName)
                apply()
            }
        }) {
            Text("Get Weather")
        }

        HorizontalDivider()

        coordinates?.let {
            Text(text = "Location: ${it[0].name}, ${it[0].country}")
            Text(text = "Latitude: ${it[0].lat}, Longitude: ${it[0].lon}")
            Text(text = "State: ${it[0].state ?: "N/A"}")
        }

        weatherData?.let { weather ->
            val temperatureFahrenheit = (weather.main.temp - KELVIN_TO_CELSIUS_OFFSET) * KELVIN_TO_FAHRENHEIT_MULTIPLIER + FAHRENHEIT_OFFSET
            val temperatureCelsius = weather.main.temp - KELVIN_TO_CELSIUS_OFFSET
            HorizontalDivider()

            Text(text = "Temperature: %.2f°F / %.2f°C".format(temperatureCelsius, temperatureFahrenheit))
            Text(text = "Humidity: ${weather.main.humidity}%")
            Text(text = "Weather: ${weather.weather[0].description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }}")

            HorizontalDivider()

            val windSpeedMph = (weather.wind.speed * METER_PER_SEC_TO_MPH).roundToInt()
            Text(text = "Wind Speed: $windSpeedMph MPH")

            HorizontalDivider()

            val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
            Image(
                painter = rememberAsyncImagePainter(iconUrl),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(ICON_SIZE_DP.dp).padding(PADDING_DP.dp)
            )
        }
    }
}
