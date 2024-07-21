package com.example.vaerappforsvaksynte.data.repository

import com.example.vaerappforsvaksynte.data.nowforecast.NowForecast
import com.example.vaerappforsvaksynte.data.nowforecast.NowForecastModel
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecastModel
import com.example.vaerappforsvaksynte.data.forecastalert.ForecastAlert
import com.example.vaerappforsvaksynte.data.forecastalert.ForecastAlertModel
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*

/*
    Data layer interface for forecast data
 */

class ForecastRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    // Returns the forecast for right now for the given [lat] and [lon]
    suspend fun getNowForecast(lat: Double, lon: Double): NowForecast {
        val model = NowForecastModel(client = client)

        try {
            return model.getNowForecast(lat = lat, lon = lon)
        } catch (cause: Throwable) {
            throw cause
        }
    }

    // Returns the long-term forecast for the given [lat] and [lon]
    suspend fun getDayForecasts(lat: Double, lon: Double): List<DayForecast> {
        // Get daily forecasts without warning alerts
        val dayForecastModel = DayForecastModel(client)
        try {
            val dayForecasts =
                dayForecastModel.getDayForecasts(lat = lat, lon = lon)

            // If any forecast alerts, add warnings to all applicable days
            val alerts = getForecastAlerts(lat = lat, lon = lon)
            addAlertsToDayForecasts(
                dayForecasts,
                alerts
            )
            return dayForecasts

        } catch (cause: Throwable) {
            throw cause
        }
    }

    // Returns all forecast warning alerts for the given [lat] and [lon]
    private suspend fun getForecastAlerts(lat: Double, lon: Double): List<ForecastAlert> {
        val model = ForecastAlertModel(client)

        return model.getForecastAlerts(lat = lat, lon = lon)
    }

    /**
     * Adds each alert from the given [alerts] to all of the [days] that match the dates of the alert
     */
    private fun addAlertsToDayForecasts(
        days: List<DayForecast>,
        alerts: List<ForecastAlert>
    ) {
        val dayForecastModel = DayForecastModel(client)

        for (alert in alerts) {
            for (day in days) {
                if (day.date in alert.appliesToDates) {
                    dayForecastModel.addAlertToDay(day = day, alert = alert)
                }
            }
        }
    }
}