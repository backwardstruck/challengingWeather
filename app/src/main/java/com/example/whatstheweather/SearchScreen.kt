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

@Composable
fun SearchScreen(
    viewModel: WeatherViewModel,
    sharedPreferences: SharedPreferences,
    placeName: String,
    onCityNameChange: (String) -> Unit
) {
    val coordinates by viewModel.coordinates.observeAsState()  // Observe reverse geocoding results
    val weatherData by viewModel.weatherData.observeAsState()

    Column {
        TextField(
            value = placeName,
            onValueChange = { onCityNameChange(it) },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
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
            val temperatureFahrenheit = (weather.main.temp - 273.15) * 9 / 5 + 32
            val temperatureCelsius = weather.main.temp - 273.15
            HorizontalDivider()

            Text(text = "Temperature: %.2f°F / %.2f°C".format(temperatureCelsius, temperatureFahrenheit))
            Text(text = "Humidity: ${weather.main.humidity}%")
            Text(text = "Weather: ${weather.weather[0].description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }}")

            HorizontalDivider()

            val windSpeedMph = (weather.wind.speed * 2.23694).roundToInt()
            Text(text = "Wind Speed: $windSpeedMph MPH")

            HorizontalDivider()

            val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
            Image(
                painter = rememberAsyncImagePainter(iconUrl),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(200.dp).padding(32.dp)
            )
        }
    }
}
