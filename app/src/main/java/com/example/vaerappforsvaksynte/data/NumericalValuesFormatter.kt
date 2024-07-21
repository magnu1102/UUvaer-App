package com.example.vaerappforsvaksynte.data

import java.math.RoundingMode
import kotlin.math.roundToInt

/**
 * Helper class that is responsible for formatting numerical values into appropriate strings
 */
class NumericalValuesFormatter {
    // Returns the temperature rounded to nearest integer
    fun formatTemperature(temp: Double?): String {
        if (temp == null) return ""

        val roundedTemp = temp.roundToInt()
        return roundedTemp.toString()
    }

    // Returns the precipitation rounded up to one decimal
    fun formatPrecipitation(precipitation: Double?): String {
        if (precipitation == null) return ""

        val roundedPrecipitation = precipitation.toBigDecimal().setScale(
            1,
            RoundingMode.UP
        )
        return roundedPrecipitation.toString()
    }

    // Returns the wind speed rounded to nearest integer
    fun formatWindSpeed(windSpeed: Double?): String {
        if (windSpeed == null) return ""

        val roundedWindSpeed = windSpeed.roundToInt()
        return roundedWindSpeed.toString()
    }
}