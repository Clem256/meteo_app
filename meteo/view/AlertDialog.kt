package com.example.projet_note_clement_johann.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projet_note.views.WeatherViewModel

@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>, viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("Lyon") }
    if (shouldShowDialog.value) { // 2
        AlertDialog( // 3
            onDismissRequest = { // 4
                shouldShowDialog.value = false
            },
            // 5
            title = { Text(text = "Affichage météo d'une ville") },
            text = {
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Enter City") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = { // 6
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                        viewModel.getWeatherDays(city)

                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text(
                        text = "Valider",
                        color = Color.White
                    )
                }
            }
        )
    }
}