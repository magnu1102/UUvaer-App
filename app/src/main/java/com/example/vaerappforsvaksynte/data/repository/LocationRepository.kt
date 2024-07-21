package com.example.vaerappforsvaksynte.data.repository

import android.content.Context
import com.example.vaerappforsvaksynte.data.location.LocationDto
import com.example.vaerappforsvaksynte.data.location.LocationModel

/*
Data layer interface for locations
 */

class LocationRepository(context: Context) {
    private val locationModel: LocationModel = LocationModel(context)

    // Returns the location with the given name from the database
    suspend fun getLocation(locationName: String): LocationDto? {
        return locationModel.getLocation(locationName = locationName)
    }

    // Returns all locations in location database
    suspend fun getLocations() : List<LocationDto> {
        return locationModel.getLocations()
    }

    // Adds a location to location database
    suspend fun addLocation(location: LocationDto): List<LocationDto> {
        try {
            locationModel.addLocation(location)
        } catch (e: Throwable) {
            throw e
        }
        return getLocations()
    }

    // Deletes a location from database
    suspend fun deleteLocation(locationName: String): List<LocationDto> {
        locationModel.deleteLocation(locationName)
        return getLocations()
    }
}