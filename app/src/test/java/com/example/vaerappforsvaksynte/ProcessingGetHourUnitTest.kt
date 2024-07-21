package com.example.vaerappforsvaksynte

import com.example.vaerappforsvaksynte.data.dayforecast.*
import org.junit.Assert
import org.junit.Test

class ProcessingGetHourUnitTest {
    private val processor = DayForecastProcessing()

    // Test with "normal" input day
    @Test
    fun testGetHour() {
        // Arrange
        val mockTimeseries = TimeSeries(
            time = "2023-05-12T12:00:00Z",
            data = LocationData(
                instant = LocationInstant(
                    details = LocationDetails(
                        airTemperature = 15.1,
                        windSpeed =  2.2
                    )
                ),
                next1Hours = LocationNext1Hours(
                    summary = LocationSummary2(
                        symbolCode = "cloudy"
                    ),
                    LocationDetails2(
                        precipitationAmount = 12.0
                    )
                ),
                next6Hours = LocationNext6Hours(
                    summary = LocationSummary3(
                        symbolCode = "fair_day"
                    ),
                    details = LocationDetails3(
                        precipitationAmount = 0.0
                    )
                )
            )
        )
        val expected =
            HourForecast(
                date = "12.5",
                weekday = "Fredag",
                time = "14:00",
                temperature = "15",
                windspeed = "2",
                precipitation = "12.0",
                next1HoursSymbolCode = "cloudy",
                next6HoursSymbolCode = "fair_day",
                precipitationSymbol = "icon_precipitation_heavy",
                windSymbol = "icon_wind_low"
            )

        // Act
        val outcome = processor.getHour(mockTimeseries)
        Assert.assertEquals(expected, outcome)
    }

    // Test if date switches when timezone offset leads to new day
    @Test
    fun testGetHourWithNoNext1Hour() {
        val mockTimestampData = TimeSeries(
            time = "2023-05-15T23:00:00Z",
            data = LocationData(
                instant = LocationInstant(
                    details = LocationDetails(
                        airTemperature = 0.0,
                        windSpeed =  10.0
                    )
                ),
                next1Hours = null,
                next6Hours = LocationNext6Hours(
                    summary = LocationSummary3(
                        symbolCode = "fair_day"
                    ),
                    details = LocationDetails3(
                        precipitationAmount = 2.0
                    )
                )
            )
        )

        val expected =
            HourForecast(
                date = "16.5",
                weekday = "Tirsdag",
                time = "01:00",
                temperature = "0",
                windspeed = "10",
                precipitation = "2.0",
                next1HoursSymbolCode = "",
                next6HoursSymbolCode = "fair_day",
                precipitationSymbol = "icon_precipitation_low",
                windSymbol = "icon_wind_medium"
            )

        val outcome = processor.getHour(mockTimestampData)
        Assert.assertEquals(expected, outcome)
    }

    // Tests when going over to daylight savings
    @Test
    fun testGetHourDaylightSavings() {
        val mockTimestampData = TimeSeries(
            time = "2023-03-26T01:00:00Z",
            data = LocationData(
                instant = LocationInstant(
                    details = LocationDetails(
                        airTemperature = 15.1,
                        windSpeed =  2.2
                    )
                ),
                next1Hours = LocationNext1Hours(
                    summary = LocationSummary2(
                        symbolCode = "cloudy"
                    ),
                    LocationDetails2(
                        precipitationAmount = 12.0
                    )
                ),
                next6Hours = LocationNext6Hours(
                    summary = LocationSummary3(
                        symbolCode = "fair_day"
                    ),
                    details = LocationDetails3(
                        precipitationAmount = 0.0
                    )
                )
            )
        )

        val expected =
            HourForecast(
                date = "26.3",
                weekday = "Søndag",
                time = "03:00",
                temperature = "15",
                windspeed = "2",
                precipitation = "12.0",
                next1HoursSymbolCode = "cloudy",
                next6HoursSymbolCode = "fair_day",
                precipitationSymbol = "icon_precipitation_heavy",
                windSymbol = "icon_wind_low"
            )

        // Act
        val outcome = processor.getHour(mockTimestampData)
        Assert.assertEquals(expected, outcome)
    }
}