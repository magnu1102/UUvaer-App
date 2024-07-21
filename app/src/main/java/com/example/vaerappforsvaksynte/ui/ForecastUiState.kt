package com.example.vaerappforsvaksynte.ui

import com.example.vaerappforsvaksynte.data.nowforecast.NowForecast
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.data.location.LocationDto
import com.example.vaerappforsvaksynte.data.forecastalert.ForecastAlert

/**
 * Data class that is responsible for holding information necessary for the UI
 */

data class ForecastUiState(
    val chosenLocation: String,
    val latitude: Double,
    val longitude: Double,
    val nowForecastObject: NowForecast? = null,
    val dayForecastList: List<DayForecast>? = null,
    val isUsingCurrentLocation: Boolean = false,
    val isUsingAFavoriteLocation: Boolean,
    var forecastAlertsHolder: MutableList<ForecastAlert> = mutableListOf(),
    val favoriteLocations: List<LocationDto>,

)
