package com.example.whatstheweather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory(private val weatherApiService: WeatherApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
