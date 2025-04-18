package com.example.projet_note.views

import com.example.projet_note_clement_johann.classes.City
import com.example.projet_note_clement_johann.classes.Data
import com.google.gson.annotations.SerializedName

data class CountryInfo(
    val cod: Int,
    val message: Int,
    @SerializedName("cnt")
    val nbCountry: Int,
    val countryLists: List<Data>,
    val city: City,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int
)
