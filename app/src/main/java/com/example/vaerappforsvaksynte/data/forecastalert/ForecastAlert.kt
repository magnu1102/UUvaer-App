package com.example.vaerappforsvaksynte.data.forecastalert

/*
Data class used to represent a single weather warning alert
 */
data class ForecastAlert(
    val area: String,
    val eventName: String,
    val symbolCode: String?,
    val description: String,
    val consequences: String,
    val instruction: String,
    val timeInterval: String,
    val appliesToDates: List<String>,
)
