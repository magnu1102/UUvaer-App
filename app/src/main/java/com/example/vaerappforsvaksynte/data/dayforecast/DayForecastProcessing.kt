package com.example.vaerappforsvaksynte.data.dayforecast

import com.example.vaerappforsvaksynte.data.DateTimeParser
import com.example.vaerappforsvaksynte.data.NumericalValuesFormatter


/**
 * This class contains methods for processing data from DayForecastModel into data classes
 */
class DayForecastProcessing {
    private val dateTimeParser = DateTimeParser()
    private val formatter = NumericalValuesFormatter()

    // Returns a new day from the given list of [hours]
    fun getDay(hours: MutableList<HourForecast>): DayForecast {
        if (hours.isNotEmpty()) {
            var symbolCode = ""
            var minTemp = hours[0].temperature.toDouble()
            var maxTemp = hours[0].temperature.toDouble()
            var maxWindSpeed = hours[0].windspeed.toDouble()
            var totalPrecipitation = 0.0

            // Find all aggregated values for day
            for (hour in hours) {
                if (hour.time == "08:00"){
                    symbolCode = if (hour.next1HoursSymbolCode != "")
                        hour.next1HoursSymbolCode
                    else hour.next6HoursSymbolCode
                }

                if (hour.temperature.toDouble() < minTemp) {
                    minTemp = hour.temperature.toDouble()
                }

                if (hour.temperature.toDouble() > maxTemp) {
                    maxTemp = hour.temperature.toDouble()
                }

                if (hour.windspeed.toDouble() > maxWindSpeed) {
                    maxWindSpeed = hour.windspeed.toDouble()
                }

                totalPrecipitation += hour.precipitation.toDouble()
            }

            return DayForecast(
                date = hours[0].date,
                weekday = hours[0].weekday,
                hourForecastList = hours,
                symbolCode = symbolCode,
                minTemperature = formatter.formatTemperature(minTemp),
                maxTemperature = formatter.formatTemperature(maxTemp),
                totalPrecipitation = formatter.formatPrecipitation(totalPrecipitation),
                maxWindSpeed = formatter.formatWindSpeed(maxWindSpeed),
                precipitationSymbol = getDailyPrecipitationSymbol(totalPrecipitation),
                windSymbol = getWindSymbol(maxWindSpeed)
            )
        } else {
            return DayForecast()
        }
    }

    // Returns an hour object for the given [timeSeries]
    fun getHour(timeSeries: TimeSeries): HourForecast {
        val hourDetails = timeSeries.data.instant.details
        val next1Hours = timeSeries.data.next1Hours
        val next6Hours = timeSeries.data.next6Hours

        val precipitation =
            next1Hours?.details?.precipitationAmount ?: next6Hours?.details?.precipitationAmount

        return HourForecast(
            date = dateTimeParser.parseDate(timeSeries.time),
            weekday = dateTimeParser.parseWeekday(timeSeries.time),
            time = dateTimeParser.parseTime(timeSeries.time),
            temperature = formatter.formatTemperature(hourDetails.airTemperature),
            windspeed = formatter.formatWindSpeed(hourDetails.windSpeed),
            precipitation = formatter.formatPrecipitation(precipitation),
            next1HoursSymbolCode = next1Hours?.summary?.symbolCode ?: "",
            next6HoursSymbolCode = next6Hours?.summary?.symbolCode ?: "",
            precipitationSymbol = getHourlyPrecipitationSymbol(
                precipitation,
                next1Hours?.summary?.symbolCode
            ),
            windSymbol = getWindSymbol(hourDetails.windSpeed)
        )
    }

    /**
     * Returns the name of the precipitation symbol for the given [precipitationAmount]
     * If no [dataForNext1Hour] found, uses data for next 6 hours instead
     */
    private fun getHourlyPrecipitationSymbol(precipitationAmount: Double?, dataForNext1Hour: String?): String {
        val precipitationSymbolCode: String

        if (dataForNext1Hour == null){
            precipitationSymbolCode = when (precipitationAmount) {
                null -> "NO_IMAGE_FOUND"
                0.0 -> "icon_precipitation_no"
                in 0.1..3.0 -> "icon_precipitation_low"
                in 3.1..6.0 -> "icon_precipitation_medium"
                else -> "icon_precipitation_heavy"
            }
        }

        else {
            precipitationSymbolCode = when (precipitationAmount) {
                null -> "NO_IMAGE_FOUND"
                0.0 -> "icon_precipitation_no"
                in 0.1..0.5 -> "icon_precipitation_low"
                in 0.6..1.0 -> "icon_precipitation_medium"
                else -> "icon_precipitation_heavy"
            }
        }
        return precipitationSymbolCode
    }

    /**
     * Returns the name of the precipitation symbol for the given [precipitationAmount]
     */
    private fun getDailyPrecipitationSymbol(precipitationAmount: Double?): String {

        val precipitationSymbolCode: String = when (precipitationAmount) {
            null -> "no_image_found"
            0.0 -> "icon_precipitation_no"
            in 0.1..12.0 -> "icon_precipitation_low"
            in 12.1..24.0 -> "icon_precipitation_medium"
            else -> "icon_precipitation_heavy"
        }

        return precipitationSymbolCode
    }

    /**
     * Returns the name of the wind symbol for the given [windSpeed]
     */
    private fun getWindSymbol(windSpeed: Double?): String {

        val windSymbolCode: String = when (windSpeed) {
            null -> "no_image_found"
            in 0.0..1.5 -> "icon_wind_no"
            in 1.6..5.4 -> "icon_wind_low"
            in 5.5..10.7 -> "icon_wind_medium"
            else -> "icon_wind_strong"
        }

        return windSymbolCode
    }
}