package com.example.whatstheweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel(private val weatherApiService: WeatherApiService) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData
    private val _coordinates = MutableLiveData<List<GeocodingResponse>?>()
    val coordinates: MutableLiveData<List<GeocodingResponse>?> = _coordinates
    private val apiKey = "6b3178c1548686e04c0061d3c19f21e8"
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchWeather(lat: Double, lon: Double) {
        val call = weatherApiService.getWeatherByCoordinates(lat, lon, apiKey)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    _weatherData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                _errorMessage.postValue("Error: ${t.message}")
            }
        })
    }

    fun fetchCoordinates(cityName: String) {
        val call = RetrofitInstance.api.getCoordinatesByCityName(cityName, apiKey = apiKey)
        call.enqueue(object : Callback<List<GeocodingResponse>> {
            override fun onResponse(
                call: Call<List<GeocodingResponse>>,
                response: Response<List<GeocodingResponse>>
            ) {
                if (response.isSuccessful) {
                    val coordinatesList = response.body()
                    if (!coordinatesList.isNullOrEmpty()) {
                        _coordinates.postValue(coordinatesList)


                        val lat = coordinatesList[0].lat
                        val lon = coordinatesList[0].lon
                        fetchWeather(lat, lon)
                    }
                }
            }

            override fun onFailure(call: Call<List<GeocodingResponse>>, t: Throwable) {

                _errorMessage.postValue("Error: ${t.message}")


            }
        })
    }

    fun reverseGeocode(lat: Double, lon: Double) {
        val call = RetrofitInstance.api.reverseGeocode(lat, lon, 1, apiKey)
        call.enqueue(object : Callback<List<GeocodingResponse>> {
            override fun onResponse(
                call: Call<List<GeocodingResponse>>,
                response: Response<List<GeocodingResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _coordinates.postValue(response.body())
                } else {
                    // TODO: Handle API failure
                }
            }

            override fun onFailure(call: Call<List<GeocodingResponse>>, t: Throwable) {
                // TODO: Handle error
            }
        })
    }

}
