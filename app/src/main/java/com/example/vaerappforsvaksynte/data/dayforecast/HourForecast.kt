package com.example.vaerappforsvaksynte.data.dayforecast

/**
 * Data class responsible for holding the forecasted data for a given hour of a day
 */
data class HourForecast(
    val date: String = "",
    val weekday: String = "",
    val time: String = "",
    val temperature: String = "",
    val windspeed: String = "",
    val precipitation: String = "",
    val next1HoursSymbolCode: String = "",
    val next6HoursSymbolCode: String = "",
    val precipitationSymbol: String = "",
    val windSymbol: String = ""
)