package com.example.whatstheweather

data class GeocodingResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?
)
