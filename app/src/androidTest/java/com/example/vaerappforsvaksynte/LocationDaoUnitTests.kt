package com.example.vaerappforsvaksynte

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vaerappforsvaksynte.data.location.LocationDB
import com.example.vaerappforsvaksynte.data.location.LocationDao
import com.example.vaerappforsvaksynte.data.location.LocationDto
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Unit tests for LocationDao
 * Source: https://developer.android.com/training/data-storage/room/testing-db#android
 * Source: https://developer.android.com/kotlin/coroutines/test
 */
@RunWith(AndroidJUnit4::class)
class LocationDaoUnitTests {
    private lateinit var locationDao: LocationDao
    private lateinit var db: LocationDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, LocationDB::class.java).build()

        locationDao = db.locationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // Tests writing a location and reading it
    @Test
    @Throws(Exception::class)
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun writeLocationAndRead() = runTest {
        val locationName = "Tønsberg"
        val location : LocationDto = TestUtil.createLocation(locationName)
        locationDao.insert(location)
        val byName = locationDao.getLocation(locationName)
        assertThat(byName, equalTo(location))
    }

    // Tests writing several locations and that searching for all locations returns a list of all locations
    @Test
    @Throws(Exception::class)
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun writeLocationsAndReadList() = runTest {
        val location1 = TestUtil.createLocation("Teststed 1")
        val location2 = TestUtil.createLocation("Teststed 2")
        val location3 = TestUtil.createLocation("Teststed 3")

        locationDao.insert(location1)
        locationDao.insert(location2)
        locationDao.insert(location3)

        val expectedResult = listOf(location1, location2, location3)
        val outcome = locationDao.getLocations()

        assertEquals(expectedResult, outcome)
    }

    // Tests that searching for all locations in an empty DB returns empty list
    @Test
    @Throws(Exception::class)
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun testGetLocationsFromEmptyDBShouldReturnEmptyList() = runTest {
        val expectedResult = emptyList<LocationDto>()
        val outcome = locationDao.getLocations()

        assertEquals(outcome, expectedResult)
    }

    // Tests that deleting a location works and searching for all locations return empty list when no locations
    @Test
    @Throws(Exception::class)
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun testDeleteLocationAndReadListShouldReturnEmptyList() = runTest {
        val location = TestUtil.createLocation("Teststed 1")

        locationDao.insert(location)
        locationDao.delete("Teststed 1")

        val expectedResult = listOf<LocationDto>()
        val outcome = locationDao.getLocations()
        assertEquals(outcome, expectedResult)
    }

    // Tests that searching for a deleted location returns null
    @Test
    @Throws(Exception::class)
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun testDeleteLocationAndReadShouldReturnNull() = runTest {
        val locationName = "Teststed 1"
        val location = TestUtil.createLocation(locationName)

        locationDao.insert(location)
        locationDao.delete(locationName)

        assertEquals(locationDao.getLocation(locationName), null)
        assertEquals(locationDao.getLocations(), emptyList<LocationDto>())
    }
}