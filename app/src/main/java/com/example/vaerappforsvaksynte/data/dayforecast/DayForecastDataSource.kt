package com.example.vaerappforsvaksynte.data.dayforecast

import android.content.ContentValues.TAG
import android.util.Log
import com.example.vaerappforsvaksynte.BuildConfig.MET_API_KEY
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 *
 * This class is responsible for fetching unfiltered data from the LocationForecast api
 *
 * @param client the client used for making the api-requests
 */

class DayForecastDataSource(private val client: HttpClient) {

    // Returns the network model for DayForecast
    suspend fun fetchDayForecastRoot(lat: Double, lon: Double): DayForecastRoot {
        val path = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"

        try {
            val root: DayForecastRoot = client.get(path){
                headers{
                    append("X-Gravitee-API-Key", MET_API_KEY)
                }
            }.body()

            return root
        } catch (cause: Throwable) {
            Log.d(TAG, "ERROR: ${cause.message.toString()}")
            throw Throwable("Kunne ikke laste inn data")
        }
    }
    }