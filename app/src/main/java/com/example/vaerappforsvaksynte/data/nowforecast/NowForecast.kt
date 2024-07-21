package com.example.vaerappforsvaksynte.data.nowforecast

/**
 * Data class responsible for holding the forecast data for the current time
 */
data class NowForecast(
    val temperature: String = "",
    val precipitation: String = "",
    val windSpeed: String = "",
    val symbol: String = "",
    val isLoaded: Boolean = false,
    val date: String = "",
    val lastUpdatedTime: String = "",
    val precipitationSymbol: String = "",
    val windSymbol: String = "",
)
