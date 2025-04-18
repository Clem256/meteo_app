package com.example.meteo.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(navController: NavController) {
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    var user by remember { mutableStateOf(firebaseAuth.currentUser) }
    // observe changement d'état , sinon favori ne sera pas affiché
    DisposableEffect(Unit) {
        val authListener = FirebaseAuth.AuthStateListener { auth ->
            user = auth.currentUser
        }
        firebaseAuth.addAuthStateListener(authListener)
        onDispose {
            firebaseAuth.removeAuthStateListener(authListener)
        }
    }

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        containerColor = Color( 0xFF1E3A8A),
        contentColor = Color.Red
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        var items = listOf(
            Screen.Weather,
            Screen.Auth,
        )
        if (user != null) {
            items += Screen.Favori
        }

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (screen == Screen.Favori && user == null) {
                        navController.navigate(Screen.Auth.route) {
                            launchSingleTop = true
                        }
                    } else if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )
        }
    }
}