package com.example.vaerappforsvaksynte.data.forecastalert

import io.ktor.client.*
import java.time.ZonedDateTime

/**
 * This class processes network response from MetAlerts into UI-ready ForecastAlerts
 */
class ForecastAlertModel(client: HttpClient) {
    private val dataSource = ForecastAlertsDataSource(client)
    private val awarenessLevelToColor = mapOf(
        "1" to "green",
        "2" to "yellow",
        "3" to "red",
        "4" to "red"
    )
    private val eventsToFilePath = mapOf(
        "avalanches" to "avalanches",
        "blowingsnow" to "snow",
        "snow" to "snow",
        "drivingconditions" to "drivingconditions",
        "extreme" to "extreme",
        "flood" to "flood",
        "forestfire" to "forestfire",
        "generic" to "generic",
        "ice" to "ice",
        "icing" to "ice",
        "landslide" to "landslide",
        "lightning" to "lightning",
        "polarlow" to "polarlow",
        "rain" to "rain",
        "rainflood" to "rainflood",
        "stormsurge" to "stormsurge",
        "gale" to "wind",
        "wind" to "wind"
    )

    /**
     * Returns a list of all forecasted alerts for the given [lat] and [lon]
     */
    suspend fun getForecastAlerts(lat: Double, lon: Double): List<ForecastAlert> {
        // fetch unfiltered data
        val unfilteredData = dataSource.fetchForecastAlertsRoot(lat = lat, lon = lon)
        val listOfUnfilteredAlerts = unfilteredData.features

        // parse to ForecastAlert objects
        val listOfAlerts = mutableListOf<ForecastAlert>()

        for (feature in listOfUnfilteredAlerts) {
            val properties = feature.properties
            val dates = feature.whenDates.interval

            val newAlert = ForecastAlert(
                    area = properties.area ?: "Område ikke tilgjengelig",
                    eventName = properties.eventAwarenessName ?: "Hendelsesnavn ikke tilgjengelig",
                    symbolCode = getSymbolCode(feature),
                    description = properties.description ?: "Beskrivelse ikke tilgjengelig",
                    consequences = properties.consequences ?: "Konsekvenser ikke tilgjengelig",
                    instruction = properties.instruction ?: "Instrukser ikke tilgjengelig",
                    timeInterval = getTimeInterval(dates),
                    appliesToDates = getDatesAppliedTo(dates),
                )

            listOfAlerts.add(newAlert)
        }

        return listOfAlerts
    }

    // Returns a list of dates on the format "dd.MM" for the given [timestamps]
    private fun getDatesAppliedTo(timestamps: List<String>?): List<String> {
        if (timestamps == null) return emptyList()

        val firstDate = parseDateToZonedDateTime(timestamps[0])
        var lastDate = parseDateToZonedDateTime(timestamps.last())

        // Only add last day if timestamp ends after 00:00:00
        if (lastDate.hour > 0) {
            lastDate = lastDate.plusDays(1)
        }

        val listOfDays = mutableListOf<String>()
        var currentDate = firstDate

        while (currentDate.dayOfMonth != lastDate.dayOfMonth) {
            listOfDays.add(parseDate(currentDate.toString()))

            currentDate = currentDate.plusDays(1)
        }

        return listOfDays
    }

    // Returns a string with date and hour the alert is valid from and to
    private fun getTimeInterval(timestamps: List<String>?): String {
        if (timestamps == null) return ""

        val startDate = parseDate(timestamps[0])
        val startHour = parseHour(timestamps[0])
        val endDate = parseDate(timestamps.last())
        val endHour = parseHour(timestamps.last())

        return "Gjelder fra: $startDate kl. $startHour til $endDate kl. $endHour"
    }

    // Returns the filename of the warning icon for the given [unfilteredAlert]
    private fun getSymbolCode(unfilteredAlert: MetAlertsFeature): String? {
        val baseString = "icon_warning"

        val event = unfilteredAlert.properties.event?.lowercase()

        val awarenessLevel = unfilteredAlert.properties.awareness_level
        val awarenessLevelNum = awarenessLevel
            ?.substringBefore(";")
            ?.replace(" ", "")
        val awarenessLevelColor = awarenessLevelToColor[awarenessLevelNum]

        return if (event == "extreme") {
            "${baseString}_${event}"
        } else if (
            event == null ||
            awarenessLevelColor == null ||
            event !in eventsToFilePath) {
            "no_image_found"
        } else if (awarenessLevelColor == "green") {
            null
        } else "${baseString}_${eventsToFilePath[event]}_$awarenessLevelColor"
    }

    // Returns the date of a given [timestamp] on the format "dd.MM"
    private fun parseDate(timestamp: String?): String {
        if (timestamp == null) return ""

        val zonedResultOfParsing = ZonedDateTime.parse(timestamp)

        val day = zonedResultOfParsing.dayOfMonth
        val month = zonedResultOfParsing.monthValue
        return "$day.$month"
    }

    // Returns a ZonedDateTime for the given [timestamp]
    private fun parseDateToZonedDateTime(timestamp: String?): ZonedDateTime {
        if (timestamp == null) return ZonedDateTime.parse("")

        return ZonedDateTime.parse(timestamp)
    }

    // Return the hour of a timestamp on the format "(H)H"
    private fun parseHour(timestamp: String): String {
        val zonedResultOfParsing = ZonedDateTime.parse(timestamp)
        return zonedResultOfParsing.hour.toString()
    }
}