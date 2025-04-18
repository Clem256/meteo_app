package com.example.meteo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FavoriScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var city by remember { mutableStateOf("") }
    val favoriteCities by authViewModel.favoriteCities.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Favorite Cities", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { authViewModel.addFavoriCity(city, true) }) {
                Text("Add Favorite")
            }

            Button(onClick = { authViewModel.addFavoriCity(city, false) }) {
                Text("Remove Favorite")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { authViewModel.getFavoriCity() }) {
            Text("Get Favorite Cities")
        }

        Spacer(modifier = Modifier.height(16.dp))

        favoriteCities.forEach { favoriteCity ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = favoriteCity, style = MaterialTheme.typography.bodyMedium)

                Button(onClick = {
                    navController.navigate(Screen.Weather.createRoute(favoriteCity))
                }) {
                    Text("Voir Météo")
                }
            }
        }
    }
}