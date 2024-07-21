package com.example.vaerappforsvaksynte.data

import java.time.ZoneId
import java.time.ZonedDateTime

/*
Helper class that is responsible for parsing dates and times
 */
class DateTimeParser {
    // Returns the time of the given [timestamp] on the format HH:mm with timezone offset
    fun parseTime(timestamp: String): String {
        val zonedResultOfParsing =
            ZonedDateTime.parse(timestamp).withZoneSameInstant(ZoneId.of("Europe/Oslo"))

        val hourOffsetForTimezone = zonedResultOfParsing.toString().substring(11, 13)
        val mins = zonedResultOfParsing.toString().substring(14, 16)

        return "$hourOffsetForTimezone:$mins"
    }

    // Returns the date of the given [timestamp] on the format "d.M" with timezone-offset
     fun parseDate(timestamp: String?): String {
        if (timestamp == null) return ""

        val zonedResultOfParsing =
            ZonedDateTime.parse(timestamp).withZoneSameInstant(ZoneId.of("Europe/Oslo"))

        val day = zonedResultOfParsing.dayOfMonth
        val month = zonedResultOfParsing.monthValue
        return "$day.$month"
    }

    // Returns the weekday of the given [timestamp] in Norwegian, ie "Mandag"
    fun parseWeekday(timestamp: String?): String {
        val daysOfWeekEngNor = mapOf(
            "MONDAY" to "Mandag",
            "TUESDAY" to "Tirsdag",
            "WEDNESDAY" to "Onsdag",
            "THURSDAY" to "Torsdag",
            "FRIDAY" to "Fredag",
            "SATURDAY" to "Lørdag",
            "SUNDAY" to "Søndag"
        )
        if (timestamp == null) return ""

        val zonedResultOfParsing =
            ZonedDateTime.parse(timestamp).withZoneSameInstant(ZoneId.of("Europe/Oslo"))

        val dayOfWeekEng = zonedResultOfParsing.dayOfWeek.toString()
        return daysOfWeekEngNor[dayOfWeekEng] ?: ""
    }
}