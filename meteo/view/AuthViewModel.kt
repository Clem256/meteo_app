package com.example.meteo.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://abbbaazz-default-rtdb.europe-west1.firebasedatabase.app/")
    private val userRef = database.reference.child("users")
    private val FavoriteCities = MutableStateFlow<List<String>>(emptyList())
    val favoriteCities: StateFlow<List<String>> = FavoriteCities
    fun signUp(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Ajouter l'utilisateur dans la base de données
                        val userId = auth.currentUser?.uid
                        userId?.let {
                            userRef.child(it).setValue(mapOf("email" to email))
                        }
                    }
                    onResult(task.isSuccessful)

                }
        }
    }

    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    onResult(task.isSuccessful)
                }
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getUser(): String? {
        return auth.currentUser?.uid
    }

    fun addFavoriCity(city: String, favori: Boolean) {
        val userId = getUser() ?: return
        if (userId.isEmpty()) {
            return
        }
        //récupère le fils favorites de l'utilisateur en fonction de son id
        val userFavorites = userRef.child(userId).child("favorites")
        if (favori) {
            //ajoute si premier clic
            userFavorites.child(city).setValue(true)
        } else {
            //supprime si second clic
            userFavorites.child(city).removeValue()
        }
    }

    fun getFavoriCity() {
        val userId = getUser() ?: return
        val userFavoritesRef = userRef.child(userId).child("favorites")
        userFavoritesRef.get().addOnSuccessListener { snapshot ->
            val cities = snapshot.children.mapNotNull { it.key }
            FavoriteCities.value = cities
        }
    }
}


