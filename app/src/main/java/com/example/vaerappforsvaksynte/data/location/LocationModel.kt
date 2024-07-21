package com.example.vaerappforsvaksynte.data.location

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room

/**
 * Class responsible for processing location data and representing business logic of the locationDto class
 */
class LocationModel(applicationContext: Context) {
    // Create an instance of the database
    private val db = Room.databaseBuilder(
        applicationContext,
        LocationDB::class.java, "LocationDatabase"
    ).build()

    // Data transfer object (DAO) to transfer between the app and database.
    private val locationDao = db.locationDao()

    // Returns the location with the given name from database
    suspend fun getLocation(locationName: String): LocationDto? {
        return locationDao.getLocation(locationName)
    }

    // Returns all locations from database
    suspend fun getLocations(): List<LocationDto> {
        return locationDao.getLocations()
    }

    // Adds a location to the database
    suspend fun addLocation(location: LocationDto): List<LocationDto> {
        try {
            locationDao.insert(location)
        } catch (e: SQLiteConstraintException) {
            throw Throwable(message = "Sted er allerede lagret", cause = e)
        }
        return getLocations()
    }

    // Deletes a location from database
    suspend fun deleteLocation(locationName: String) {
        locationDao.delete(locationName)
    }
}