package com.example.projet_note.views

import com.google.gson.annotations.SerializedName

data class WindInfo(
    val speed: Double,
    @SerializedName("deg")
    val direction: Int,
    @SerializedName("gust")
    val rafale: Double
)
