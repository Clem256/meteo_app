package com.example.meteo.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meteo.MainActivity


@Composable
fun AuthScreen(mainActivity: MainActivity, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var message by remember { mutableStateOf("") }
    val contexte = LocalContext.current
    val isConnected = isOnline(contexte)
    val displayOneTime = remember { mutableStateOf(true) }
    if (isConnected and displayOneTime.value) {
        Toast.makeText(contexte, "Connecter a internet", Toast.LENGTH_SHORT).show()
        displayOneTime.value = false
    } else if (!displayOneTime.value) {
    } else {
        Toast.makeText(contexte, "Pas de connexion a internet", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenue", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                authViewModel.signUp(email.text, password.text) { success ->
                    message = if (success) "Inscription réussie" else "Erreur d'inscription"
                }
            }) {
                Text("S'inscrire")
            }

            Button(onClick = {
                authViewModel.signIn(email.text, password.text) { success ->
                    message = if (success) "Connexion réussie" else "Erreur de connexion"
                    if (success) {
                        mainActivity.triggerNotification()
                    }
                }
            }) {
                Text("Se connecter")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                authViewModel.signOut()
                message = "Déconnecté"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se déconnecter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}
