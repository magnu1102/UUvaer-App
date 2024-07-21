package com.example.vaerappforsvaksynte

import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecastProcessing
import com.example.vaerappforsvaksynte.data.dayforecast.HourForecast
import org.junit.Assert
import org.junit.Test
import kotlin.math.roundToInt

class ProcessingGetDayUnitTest {
    private val processor = DayForecastProcessing()

    @Test
    fun testEmptyDay() {
        val hours = mutableListOf<HourForecast>()

        val expectedResult = DayForecast()
        val outcome = processor.getDay(hours)

        Assert.assertEquals(expectedResult, outcome)
    }

    @Test
    fun testParseDay() {
        val hours = mutableListOf(
            HourForecast(
                date = "12.5",
                weekday = "Fredag",
                time = "08:00",
                temperature = "14",
                windspeed = "3",
                precipitation = "5.0",
                next1HoursSymbolCode = "cloudy",
                next6HoursSymbolCode = "fair_day",
                precipitationSymbol = "icon_precipitation_heavy",
                windSymbol = "icon_wind_low"
            ),
            HourForecast(
                date = "12.5",
                weekday = "Fredag",
                time = "15:00",
                temperature = "0",
                windspeed = "10",
                precipitation = "2.0",
                next1HoursSymbolCode = "",
                next6HoursSymbolCode = "fair_day",
                precipitationSymbol = "icon_precipitation_low",
                windSymbol = "icon_wind_medium"
            ),
            HourForecast(
                date = "12.5",
                weekday = "Fredag",
                time = "19:00",
                temperature = "15",
                windspeed = "2",
                precipitation = "12.0",
                next1HoursSymbolCode = "cloudy",
                next6HoursSymbolCode = "fair_day",
                precipitationSymbol = "icon_precipitation_heavy",
                windSymbol = "icon_wind_low"
            )
        )

        val expectedResult = DayForecast(
            date = "12.5",
            weekday = "Fredag",
            hourForecastList = hours,
            symbolCode = "cloudy",
            minTemperature = "0",
            maxTemperature = "15",
            totalPrecipitation = "19.0",
            maxWindSpeed = "10",
            precipitationSymbol = "icon_precipitation_medium",
            windSymbol = "icon_wind_medium"
        )
        val outcome = processor.getDay(hours)

        Assert.assertEquals(expectedResult, outcome)
    }

    @Test
    fun testDayWith24Hours() {
        val date = "24.06"
        val temperatures = (-5..19).toList().shuffled()
        val precipitations = mutableListOf(0.0)
        for (i in 0..22) {
            precipitations.add(i+0.7)
        }

        val hourForecasts = mutableListOf<HourForecast>()
        for (i in 0..23) {
            hourForecasts.add(HourForecast(
                date = date,
                time = if (i == 8) "08:00" else i.toString(),
                weekday = "Mandag",
                temperature = temperatures[i].toString(),
                precipitation = precipitations[i].toString(),
                windspeed = i.toString(),
                next1HoursSymbolCode = if (i == 8) "fair_day" else "cloudy",
                next6HoursSymbolCode = if (i == 8) "rainy" else "windy",
                precipitationSymbol = "icon_precipitation_heavy",
                windSymbol = "icon_wind_low"
            ))
        }

        val expectedResult = DayForecast(
            date = date,
            weekday = "Mandag",
            hourForecastList = hourForecasts,
            symbolCode = "fair_day",
            minTemperature = "-5",
            maxTemperature = "19",
            totalPrecipitation = ((precipitations.sum() * 100).roundToInt() / 100.0).toString(),
            maxWindSpeed = "23",
            precipitationSymbol = "icon_precipitation_heavy",
            windSymbol = "icon_wind_strong",
            forecastAlertsList = mutableListOf()
        )

        val outcome = processor.getDay(hourForecasts)

        Assert.assertEquals(precipitations.size, 24)
        Assert.assertEquals(expectedResult, outcome)
    }
}