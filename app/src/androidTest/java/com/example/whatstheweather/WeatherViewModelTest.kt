package com.example.whatstheweather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var weatherViewModel: WeatherViewModel

    @Mock
    private lateinit var weatherApiService: WeatherApiService

    @Mock
    private lateinit var weatherObserver: Observer<WeatherResponse>

    @Mock
    private lateinit var mockCall: Call<WeatherResponse>

    @Before
    fun setUp() {

        weatherViewModel = WeatherViewModel()
        weatherViewModel.weatherData.observeForever(weatherObserver)
    }

    @Test
    fun fetchWeatherwithvalidresponseupdatesLiveData() {
        val mockResponse = WeatherResponse(main = Main(temp = 298.0F, humidity = 17), weather = listOf(), wind = Wind(speed = 50.0F))
        val response = Response.success(mockResponse)

        Mockito.`when`(weatherApiService.getWeatherByCoordinates(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyString()))
            .thenReturn(mockCall)
        Mockito.doAnswer { invocation ->
            val callback: Callback<WeatherResponse> = invocation.getArgument(0)
            callback.onResponse(mockCall, response)
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        weatherViewModel.fetchWeather(37.7749, -122.4194)

        Mockito.verify(weatherObserver).onChanged(mockResponse)
    }
}
