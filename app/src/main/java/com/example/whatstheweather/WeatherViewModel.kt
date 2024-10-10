package com.example.whatstheweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData
    private val _coordinates = MutableLiveData<List<GeocodingResponse>>()
    val coordinates: LiveData<List<GeocodingResponse>> = _coordinates

    fun fetchWeather(lat: Double, lon: Double) {
        val call = RetrofitInstance.api.getWeatherByCoordinates(
            lat, lon, "6b3178c1548686e04c0061d3c19f21e8"
        )

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    _weatherData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {



            }
        })
    }

    fun fetchCoordinates(cityName: String) {
        val call = RetrofitInstance.api.getCoordinatesByCityName(cityName, apiKey = "YOUR_API_KEY")
        call.enqueue(object : Callback<List<GeocodingResponse>> {
            override fun onResponse(
                call: Call<List<GeocodingResponse>>,
                response: Response<List<GeocodingResponse>>
            ) {
                if (response.isSuccessful) {
                    _coordinates.postValue(response.body())

                    print(response.body())
                }
            }

            override fun onFailure(call: Call<List<GeocodingResponse>>, t: Throwable) {



            }
        })
    }
}
