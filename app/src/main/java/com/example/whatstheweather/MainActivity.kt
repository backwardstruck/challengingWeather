package com.example.whatstheweather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
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

                RequestLocationPermission { granted ->
                    if (granted) {
                        getCurrentLocationAndFetchWeather()
                    } else if (!lastCity.isNullOrEmpty()) {
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

    @Composable
    fun RequestLocationPermission(onPermissionResult: (Boolean) -> Unit) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            onPermissionResult(isGranted)
        }

        LaunchedEffect(Unit) {
            when {
                ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    onPermissionResult(true)
                }
                else -> {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    private fun getCurrentLocationAndFetchWeather() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude


                        weatherViewModel.fetchWeather(lat, lon)
                    } else {
                        // TODO: Handle case where location is null
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()

        }
    }
}

