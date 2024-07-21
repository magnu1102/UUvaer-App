package com.example.vaerappforsvaksynte

import com.example.vaerappforsvaksynte.data.location.LocationDto

object TestUtil {
    fun createLocation(name: String, lat: Double = 0.0, lon: Double = 0.0) = LocationDto(
        name = name,
        lat = lat,
        lon = lon
    )
}