package com.example.projet_note.views

import com.google.gson.annotations.SerializedName

data class WeatherInfo(
    val temp: Double,
    @SerializedName("feels_like")
    val ressenti: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)
