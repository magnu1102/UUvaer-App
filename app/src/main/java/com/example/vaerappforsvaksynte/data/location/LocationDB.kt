package com.example.vaerappforsvaksynte.data.location

import androidx.room.Database
import androidx.room.RoomDatabase

/*
Abstract class that represent the location database
 */
@Database(entities = [LocationDto::class], version = 1)
abstract class LocationDB: RoomDatabase() {
    abstract fun locationDao(): LocationDao
}