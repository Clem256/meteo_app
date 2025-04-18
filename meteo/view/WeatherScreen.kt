package com.example.projet_note_clement_johann.views

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meteo.R
import com.example.projet_note.views.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, city: String) {
    val showDialog = remember { mutableStateOf(false) }
    val showDetailDialog = remember { mutableStateOf(false) }
    val weather by viewModel.weatherState.collectAsState()
    LaunchedEffect(key1 = city) {
        if (city.isNotBlank()) {
            Log.d("WeatherScreen", "get weather for $city")
            viewModel.getWeatherDays(city)
        }
    }

    if (showDialog.value) {
        MyAlertDialog(shouldShowDialog = showDialog, viewModel)
    } else if (showDetailDialog.value) {
        DétailDialog(shouldShowDialog = showDetailDialog, viewModel = viewModel, city)
    }

    Spacer(modifier = Modifier.height(24.dp))

    Scaffold(
        topBar = {},
        content = { paddingValues ->
            weather?.let { weather ->
                val backgroundColor = when (weather.weather.first().description) {
                    "clear sky" -> Color(0xFF87CEEB)
                    "clouds" -> Color(0xFFB0C4DE)
                    "rain" -> Color(0xFF708090)
                    "snow" -> Color(0xFFF0F8FF)
                    else -> Color(0xFFE0E0E0)
                }
                val textColor = when (weather.weather.first().description) {
                    "clear sky" -> Color.White
                    else -> Color.Black
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(backgroundColor)
                        .fillMaxSize()
                ) {
                    Row(modifier = Modifier.padding(paddingValues)) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Crossfade(targetState = weather.weather.first().description) { description ->
                                    when (description) {
                                        "overcast clouds", "broken clouds", "scattered clouds", "few clouds" -> Image(
                                            painter = painterResource(id = R.drawable.nuage),
                                            contentDescription = weather.weather.first().description,
                                            modifier = Modifier.size(38.dp)
                                        )

                                        "light rain", "moderate rain", "heavy intensity rain", "shower rain" -> Image(
                                            painter = painterResource(id = R.drawable.pluie),
                                            contentDescription = weather.weather.first().description,
                                            modifier = Modifier.size(38.dp)
                                        )

                                        "thunderstorm", "thunderstorm with rain" -> Image(
                                            painter = painterResource(id = R.drawable.orage),
                                            contentDescription = weather.weather.first().description,
                                            modifier = Modifier.size(38.dp)
                                        )

                                        "snow", "heavy snow", "light snow" -> Image(
                                            painter = painterResource(id = R.drawable.neige),
                                            contentDescription = weather.weather.first().description,
                                            modifier = Modifier.size(38.dp)
                                        )

                                        "mist", "fog", "haze" -> Image(
                                            painter = painterResource(id = R.drawable.brouillard),
                                            contentDescription = weather.weather.first().description,
                                            modifier = Modifier.size(38.dp)
                                        )

                                        "clear sky" -> Image(
                                            painter = painterResource(id = R.drawable.soleil),
                                            contentDescription = weather.weather.first().description,
                                            modifier = Modifier.size(38.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${weather.main.temp}°C",
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .animateContentSize(),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                            Text(
                                text = weather.weather.first().description,
                                color = textColor,
                                fontSize = 28.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.localisation),
                                    contentDescription = null,
                                    modifier = Modifier.size(26.dp)
                                )
                                Text(
                                    text = "${weather.name}, ${weather.sys.country}",
                                    color = textColor,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showDetailDialog.value = true },
                        modifier = Modifier.wrapContentSize(),
                    ) {
                        Text(text = "Voir plus de détails")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Feels Like: ${weather.main.feels_like}°C",
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                text = "Min: ${weather.main.temp_min}°C, Max: ${weather.main.temp_max}°C",
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Humidity: ${weather.main.humidity}%",
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                text = "Pressure: ${weather.main.pressure} hPa",
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Wind Speed: ${weather.wind.speed} m/s",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        },
        bottomBar = {
            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(text = "Choose another city")
            }
        }
    )
}