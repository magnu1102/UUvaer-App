package com.example.vaerappforsvaksynte.data.dayforecast

import com.example.vaerappforsvaksynte.data.forecastalert.ForecastAlert

/**
 * Data class responsible for holding the summarized forecast for a given day
 */
data class DayForecast(
    val date: String = "",
    val weekday: String = "",
    val hourForecastList: MutableList<HourForecast> = mutableListOf(),
    val symbolCode: String? = null,
    val minTemperature: String = "",
    val maxTemperature: String = "",
    val totalPrecipitation: String = "",
    val maxWindSpeed: String = "",
    val precipitationSymbol: String = "",
    val windSymbol: String = "",
    val forecastAlertsList: MutableList<ForecastAlert> = mutableListOf()
)