package com.example.projet_note.views

import com.google.gson.annotations.SerializedName

data class RainInfo(
    @SerializedName("1h")
    val precipitatiion: Int
)
