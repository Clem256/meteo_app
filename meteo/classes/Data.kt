package com.example.projet_note_clement_johann.classes

import com.example.projet_note.views.CloudInfo
import com.example.projet_note.views.RainInfo
import com.example.projet_note.views.Weather
import com.example.projet_note.views.WeatherInfo
import com.example.projet_note.views.WindInfo

data class Data(
    val dt: Int,
    val main: WeatherInfo,
    val weather: Weather,
    val clouds: CloudInfo,
    val wind: WindInfo,
    val visibility: Int,
    val pop: Double,
    val rain: RainInfo,
    val sys: SysInfo,
    val dt_txt: String
)
