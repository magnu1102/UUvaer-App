package com.example.vaerappforsvaksynte.data.forecastalert

import com.example.vaerappforsvaksynte.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/*
This class is responsible for fetching data from the MetAlerts api
 */

class ForecastAlertsDataSource(
    private val client: HttpClient) {

    private val basePath = "https://api.met.no/weatherapi/metalerts/1.1/.json"

    // Fetches data from MetAlerts api
    suspend fun fetchForecastAlertsRoot(lat: Double, lon: Double): ForecastAlertRoot {
        val fullPath = "$basePath?lat=$lat&lon=$lon"

        val response: ForecastAlertRoot = client.get(fullPath){
            headers{
                append("X-Gravitee-API-Key", BuildConfig.MET_API_KEY)
            }
        }.body()

        return response
    }
}
