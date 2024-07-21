package com.example.vaerappforsvaksynte.data.nowforecast

import android.content.ContentValues.TAG
import android.util.Log
import com.example.vaerappforsvaksynte.BuildConfig.MET_API_KEY
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 *
 * This class is responsible for fetching unfiltered data from the NowCast api
 *
 * @param client the client used for making the api-requests
 */

class NowForecastDataSource(
    private val client: HttpClient) {

    // Returns the network model for NowForecast
    suspend fun fetchNowForecastRoot(lat: Double, lon: Double): NowForecastRoot {
        val path = "https://api.met.no/weatherapi/nowcast/2.0/complete?lat=${lat}&lon=${lon}"

        try {
            val response = client.get(path) {
                headers {
                    append("X-Gravitee-API-Key", MET_API_KEY)
                }
            }

            return response.body()

        } catch (cause: Throwable) {
            Log.d(TAG, "ERROR: ${cause.localizedMessage}")
            throw Throwable("Kunne ikke laste inn data")
        }
    }
}