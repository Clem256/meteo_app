package com.example.meteo.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Weather : Screen("weather?city={city}", Icons.Filled.Cloud, "Météo") {
        fun createRoute(city: String) = "weather?city=$city"
    }
    object Favori : Screen("favori", Icons.Filled.Favorite, "Favoris")
    object Auth : Screen("auth", Icons.Filled.Lock, "Connexion")
}
