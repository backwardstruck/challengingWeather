package com.example.whatstheweather

data class WeatherResponse(
    val weather: List<Weather>,
    val wind: Wind,
    val main: Main
)

data class Weather(
    val description: String,
    val icon: String
)

data class Main(
    val temp: Float,
    val humidity: Int
)

data class Wind(
    val speed: Float
)
