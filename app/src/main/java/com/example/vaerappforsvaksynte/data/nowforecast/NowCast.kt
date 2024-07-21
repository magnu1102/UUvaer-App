package com.example.vaerappforsvaksynte.data.nowforecast

import com.google.gson.annotations.SerializedName
/*
    Data class which models the data fetched from the API nowcast.
    The data class serializes data with a few modifications to attribute names
    to keep them within camelCase convention.
 */
data class NowForecastRoot(
    val type: String? = null,
    val geometry: NowcastGeometry? = null,
    val properties: NowcastProperties? = null,
)

data class NowcastGeometry(
    val type: String,
    val coordinates: List<Double>,
)

data class NowcastProperties(
    val meta: NowcastMeta,
    val timeseries: List<NowcastSeries>,
)

data class NowcastMeta(
    @SerializedName("updated_at")
    val updatedAt: String,
    val units: NowcastUnits,
    @SerializedName("radar_coverage")
    val radarCoverage: String,
)

data class NowcastUnits(
    @SerializedName("air_temperature")
    val airTemperature: String,
    @SerializedName("precipitation_amount")
    val precipitationAmount: String,
    @SerializedName("precipitation_rate")
    val precipitationRate: String,
    @SerializedName("relative_humidity")
    val relativeHumidity: String,
    @SerializedName("wind_from_direction")
    val windFromDirection: String,
    @SerializedName("wind_speed")
    val windSpeed: String,
    @SerializedName("wind_speed_of_gust")
    val windSpeedOfGust: String,
)

data class NowcastSeries(
    val time: String,
    val data: NowcastData,
)

data class NowcastData(
    val instant: NowcastInstant,
    @SerializedName("next_1_hours")
    val next1Hours: NowcastNext1Hours?,
)

data class NowcastInstant(
    val details: NowcastDetails,
)

data class NowcastDetails(
    @SerializedName("air_temperature")
    val airTemperature: Double?,
    @SerializedName("precipitation_rate")
    val precipitationRate: Double,
    @SerializedName("relative_humidity")
    val relativeHumidity: Double?,
    @SerializedName("wind_from_direction")
    val windFromDirection: Double?,
    @SerializedName("wind_speed")
    val windSpeed: Double?,
    @SerializedName("wind_speed_of_gust")
    val windSpeedOfGust: Double?,
)

data class NowcastNext1Hours(
    val summary: NowcastSummary,
    val details: NowcastDetails2,
)

data class NowcastSummary(
    @SerializedName("symbol_code")
    val symbolCode: String,
)

data class NowcastDetails2(
    @SerializedName("precipitation_amount")
    val precipitationAmount: Double,
)