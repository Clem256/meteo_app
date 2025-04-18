package com.example.projet_note.views

import com.example.meteo.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getWeatherDays(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

