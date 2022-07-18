package com.example.dailyplanner.weatherAPI

import com.example.dailyplanner.modules.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather?")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("APPID") app_id: String = "",
        @Query("units") units: String = "metric"
    ): Call<WeatherModel>
}
