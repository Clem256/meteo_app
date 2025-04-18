package com.example.projet_note_clement_johann.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projet_note.views.WeatherViewModel

@Composable
fun DétailDialog(
    shouldShowDialog: MutableState<Boolean>,
    viewModel: WeatherViewModel,
    city: String
) {
    if (shouldShowDialog.value) { // 2
        AlertDialog( // 3
            onDismissRequest = { // 4
                shouldShowDialog.value = false
            },
            // 5
            title = { Text(text = "Plus de détails") },
            text = {
                Column(

                ) {
                    if (viewModel.weatherState.value?.rain?.vit !== null) {
                        Text(text = "Précipitation : ${viewModel.weatherState.value?.rain?.vit}")
                    }
                    Text(text = "Wind gust : ${viewModel.weatherState.value?.wind?.gust}")
                    Text(text = "Visibilité : ${viewModel.weatherState.value?.visibility} m")
                    Text(text = "Sea level : ${viewModel.weatherState.value?.main?.sea_level} m")
                    Text(text = "Ground level : ${viewModel.weatherState.value?.main?.grnd_level} m")
                    Text(text = "Temp kf : ${viewModel.weatherState.value?.main?.temp_kf}")
                    Text(text = "Wind degree : ${viewModel.weatherState.value?.wind?.deg} °")
                }
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
                        text = "Fermer",
                        color = Color.White
                    )
                }
            }
        )
    }
}