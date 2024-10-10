package com.example.whatstheweather

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.whatstheweather.ui.theme.WhatsTheWeatherTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val sharedPreferences = getSharedPreferences("weather_prefs", MODE_PRIVATE)
        val lastCity = sharedPreferences.getString("lastPlace", "")

        setContent {

            WhatsTheWeatherTheme {

                var cityName by remember { mutableStateOf(lastCity ?: "") }



                val errorMessage by weatherViewModel.errorMessage.observeAsState()


                errorMessage?.let {

                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }

                if (!lastCity.isNullOrEmpty()) {
                    LaunchedEffect(Unit) {
                        weatherViewModel.fetchCoordinates(lastCity)
                    }
                }


                SearchScreen(
                    viewModel = weatherViewModel,
                    sharedPreferences = sharedPreferences,
                    placeName = cityName,
                    onCityNameChange = { newCityName ->
                        cityName = newCityName
                    }
                )

            }
        }
    }
}
