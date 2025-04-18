package com.example.projet_note.views

import com.google.gson.annotations.SerializedName

data class Weather(
    val id: Int,
    @SerializedName("main")
    val weather: String,
    @SerializedName("description")
    val desc: String,
    val icon: Int
)
