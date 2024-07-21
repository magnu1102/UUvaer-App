@file:Suppress("FunctionName", "FunctionName", "FunctionName", "FunctionName")

package com.example.vaerappforsvaksynte.data.nowforecast

import com.example.vaerappforsvaksynte.data.NumericalValuesFormatter
import io.ktor.client.*

/**
 * This class processes data from network into UI-ready data classes
 *
 * Responsibilities:
 * - To process the data for forecast for the current time (For example filter out unwanted data and/or do calculations)
 * - Represent the business logic for the NowForecast-class
 */

@Suppress("FunctionName", "FunctionName")
class NowForecastModel (client: HttpClient) {
    private val dataSource = NowForecastDataSource(client)
    private val formatter = NumericalValuesFormatter()

    // Returns a NowForecast-object for the given [lat] and [lon]
    suspend fun getNowForecast(lat: Double, lon: Double): NowForecast {
        // fetch network response
        val root = fetchNowForecastRoot(lat = lat, lon = lon)

        // filter and format data
        val tsDetails = root.properties!!.timeseries[0].data.instant.details
        val ts = root.properties.timeseries[0]
        val rootProperties = root.properties

        return NowForecast(
            temperature = formatter.formatTemperature(tsDetails.airTemperature),
            precipitation = formatter.formatPrecipitation(tsDetails.precipitationRate),
            windSpeed = formatter.formatWindSpeed(tsDetails.windSpeed),
            symbol = ts.data.next1Hours!!.summary.symbolCode,
            isLoaded = true,
            date = ts.time,
            lastUpdatedTime = parseLastUpdatedTime(rootProperties.meta.updatedAt),
            precipitationSymbol = getPrecipitationSymbol(tsDetails.precipitationRate),
            windSymbol = getWindSymbol(tsDetails.windSpeed)
        )
    }

    private fun parseLastUpdatedTime(input: String): String {
        return input.substring(11, 16)
    }


    // Returns the network response for the now forecast for the given [lat] and [lon]
    private suspend fun fetchNowForecastRoot(lat: Double, lon: Double): NowForecastRoot {
        try {
            return dataSource.fetchNowForecastRoot(lat = lat, lon = lon)
        } catch (cause: Throwable) {
            throw cause
        }
    }

    // Returns the file name of the precipitation icon to be displayed
    private fun getPrecipitationSymbol(precipitationAmount: Double?): String {
        val precipitationSymbolCode: String = when (precipitationAmount!!) {
            0.0 -> "icon_precipitation_no"
            in 0.1..0.5 -> "icon_precipitation_low"
            in 0.6..1.0 -> "icon_precipitation_medium"
            else -> "icon_precipitation_heavy"
        }
        return precipitationSymbolCode
    }

    // Returns the file name of the wind icon to be displayed
    private fun getWindSymbol(windSpeed: Double?): String {
        val windSymbolCode: String = when (windSpeed!!) {
            in 0.0..1.5 -> "icon_wind_no"
            in 1.6..5.4 -> "icon_wind_low"
            in 5.5..10.7 -> "icon_wind_medium"
            else -> "icon_wind_strong"
        }
        return windSymbolCode
    }

}