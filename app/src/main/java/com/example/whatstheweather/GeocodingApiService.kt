package com.example.whatstheweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("geo/1.0/direct")
    fun getCoordinatesByCityName(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): Call<List<GeocodingResponse>>
}
