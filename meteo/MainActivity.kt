package com.example.meteo

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.meteo.classes.Wind
import com.example.meteo.ui.theme.MeteoTheme
import com.example.meteo.view.AuthScreen
import com.example.meteo.view.FavoriScreen
import com.example.meteo.view.MainScreen
import com.example.meteo.view.Screen
import com.example.projet_note.views.WeatherViewModel
import com.example.projet_note_clement_johann.views.WeatherScreen
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.annotations.SerializedName

class MainActivity : ComponentActivity() {
    private var notificationOk = mutableStateOf(false)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            notificationOk.value = true
        } else {
            notificationOk.value = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeteoTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { MainScreen(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Weather.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = Screen.Weather.route,
                            arguments = listOf(navArgument("city") { nullable = true })
                        ) { item ->
                            val city =
                                if (item.arguments?.getString("city").isNullOrBlank()) {
                                    ""
                                } else {
                                    item.arguments?.getString("city")!!
                                }
                            // Récupération instance du ViewModel
                            val weatherViewModel: WeatherViewModel = viewModel()
                            WeatherScreen(weatherViewModel, city)
                        }
                        composable(Screen.Favori.route) {
                            FavoriScreen(navController)
                        }
                        composable(Screen.Auth.route) {
                            AuthScreen(this@MainActivity)
                        }
                    }

                }
            }
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.i("token firebase", it)
        }
        Firebase.messaging.isAutoInitEnabled = true
        askNotificationPermission()

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notificationOk.value = true
            } else if (shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                showPermissionDialog()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun triggerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askNotificationPermission() //redemande les perms
            return
        }

        val channelId = "default_channel_id"
        val notificationId = 1

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.weather)
            .setContentTitle("Connexion")
            .setContentText("Félicitations, vous êtes connecté")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission requise")
            .setMessage("Cette application a besoin des notifications pour vous alerter lors de la connexion. Veuillez l'autoriser dans les paramètres.")
            .setPositiveButton("Autoriser") { dialogInterface: DialogInterface, i: Int ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

}


data class WeatherResponse(
    val name: String,
    val sys: Sys,
    val main: Main,
    val wind: Wind,
    val rain: Rain,
    val weather: List<Weather>,
    val visibility: Int,
)

data class Sys(
    val country: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val temp_kf: Int,
)

data class Wind(
    val speed: Double,
    val gust: Double,
    val deg: Int,
)

data class Rain(
    @SerializedName("3h")
    val vit: Double
)

data class Weather(
    val description: String
)

