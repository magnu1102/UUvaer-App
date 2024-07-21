package com.example.vaerappforsvaksynte.data.forecastalert

import com.google.gson.annotations.SerializedName

/*
Data classes used for parsing network response from the Met Alerts API
 */
data class ForecastAlertRoot(
    val features: List<MetAlertsFeature>
)

data class MetAlertsFeature(
    val properties: MetAlertsProperty,

    @SerializedName("when")
    val whenDates: MetAlertsWhen
)

data class MetAlertsProperty(
    val area: String?, // "Troms og Finnmark"
    val awareness_level: String?,   //"2; yellow; Moderate"
    val awareness_type: String?, //  "2; snow-ice",
    val consequences: String?, // "Noen reiser vil kunne få lengre reisetid. D..
    val description: String?,
    val event: String?, // snow
    val eventAwarenessName: String?, //Snø pågar
    val instruction: String?, //"Beregn noe ekstra tid til transport og kjøring. "
    val severity: String? // "Moderate"
)

data class MetAlertsWhen(
    val interval: List<String>?
)