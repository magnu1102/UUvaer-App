package com.example.vaerappforsvaksynte.data.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/*
Interface for accessing data from location database
 */
@Dao
interface LocationDao {
    // Returns a location with the given name from database
    @Query("SELECT * FROM locationdto WHERE name LIKE :locationName")
    suspend fun getLocation(locationName: String): LocationDto?

    // Returns all locations from database
    @Query("SELECT * FROM locationdto")
    suspend fun getLocations(): List<LocationDto>

    // Adds a location to the database
    @Insert
    suspend fun insert(vararg location: LocationDto)

    // Deletes a location from the database
    @Query("DELETE FROM locationdto WHERE name LIKE :locationName")
    suspend fun delete(locationName: String)
}