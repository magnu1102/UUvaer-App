package com.example.vaerappforsvaksynte.data.dayforecast

import com.google.gson.annotations.SerializedName

/**
 * This file contains data classes representing the network response from Locationforecast
 */
data class DayForecastRoot(
    val properties: LocationProperties? = null,
)

data class LocationProperties(
    val timeseries: List<TimeSeries>,
)

data class TimeSeries(
    val time: String,
    val data: LocationData,
)

data class LocationData(
    val instant: LocationInstant,
    @SerializedName("next_1_hours")
    val next1Hours: LocationNext1Hours?,
    @SerializedName("next_6_hours")
    val next6Hours: LocationNext6Hours?,
)

data class LocationInstant(
    val details: LocationDetails,
)

data class LocationDetails(
    @SerializedName("air_temperature")
    val airTemperature: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double,
)

data class LocationNext1Hours(
    val summary: LocationSummary2,
    val details: LocationDetails2,
)

data class LocationSummary2(
    @SerializedName("symbol_code")
    val symbolCode: String,
)

data class LocationDetails2(
    @SerializedName("precipitation_amount")
    val precipitationAmount: Double,
)

data class LocationNext6Hours(
    val summary: LocationSummary3,
    val details: LocationDetails3,
)

data class LocationSummary3(
    @SerializedName("symbol_code")
    val symbolCode: String,
)

data class LocationDetails3(
    @SerializedName("precipitation_amount")
    val precipitationAmount: Double,
)
