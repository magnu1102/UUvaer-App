package com.example.vaerappforsvaksynte.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/*
Data class that represents data for a location
 */
@Entity(tableName = "locationdto", indices = [Index(value = ["name"], unique = true)])
data class LocationDto(
    @PrimaryKey val name: String,

    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "lat") val lat: Double
)
