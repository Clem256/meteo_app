package com.example.projet_note.views

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo.WeatherResponse
import com.example.meteo.view.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val apikey = "8b7c7c099a6d47b2c80c60b66870b3d3"

    private val WeatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = WeatherState.asStateFlow()

    fun getWeatherDays(city: String) {
        viewModelScope.launch {
            try {
                val weather = RetrofitInstance.api.getWeatherDays(city, apikey)
                WeatherState.value = weather
                Log.d("WeatherViewModel", "Weather fetched: $weather")
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather", e)
            }
        }
    }
}
