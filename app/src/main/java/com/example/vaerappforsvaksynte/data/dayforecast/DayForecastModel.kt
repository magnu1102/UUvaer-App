package com.example.vaerappforsvaksynte.data.dayforecast

import com.example.vaerappforsvaksynte.data.forecastalert.ForecastAlert
import io.ktor.client.*


/**
 * This class processes long-term forecast data from network into UI-ready data classes
 *
 * Responsibilities:
 * - To process the data for long-term forecast
 * - Represent the business logic for the DayForecast-class
 */

class DayForecastModel(client: HttpClient) {
    private val dataSource = DayForecastDataSource(client = client)

    /**
     * Returns a list of day forecasts for the given [lat] and [lon]
     */
    suspend fun getDayForecasts(lat: Double, lon: Double): MutableList<DayForecast> {
        val processor = DayForecastProcessing()

        val dayForecasts = mutableListOf<DayForecast>()

        // Fetch network response
        val data = fetchDayForecastRoot(lat, lon)

        // Process the network response
        val timeSeries: List<TimeSeries> = getTimeSeries(data)
        var currentDate = ""

        var hoursForCurrentDate = mutableListOf<HourForecast>()

        // Create a new hourly object for each timeSeries
        for (timeData in timeSeries) {
            val newHour = processor.getHour(timeData)

            // Add hour to the current day if same date, else create new daily object and add hour to that
            if (newHour.date == currentDate) {
                hoursForCurrentDate.add(newHour)
            } else {
                if (currentDate != "") {
                    val newDay = processor.getDay(hoursForCurrentDate)
                    dayForecasts.add(newDay)
                }

                currentDate = newHour.date
                hoursForCurrentDate = mutableListOf()
            }
        }
        return dayForecasts
    }

    // Adds the given [alert] to the given [day]
    fun addAlertToDay(day: DayForecast, alert: ForecastAlert) {
        val previousAlertsForDay = day.forecastAlertsList
        previousAlertsForDay.add(alert)
    }

    // Fetches the network response for daily forecasts for the given [lat] and [lon]
    private suspend fun fetchDayForecastRoot(lat: Double, lon: Double): DayForecastRoot {
        try {
            return dataSource.fetchDayForecastRoot(lat, lon)
        } catch (cause: Throwable) {
            throw cause
        }
    }

    // Returns a list of all timeSeries in the given [data], or the empty list
    private fun getTimeSeries(data: DayForecastRoot): List<TimeSeries> {
        return if (data.properties == null) {
            emptyList()
        } else {
            data.properties.timeseries
        }
    }
}

