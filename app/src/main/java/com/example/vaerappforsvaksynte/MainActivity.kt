package com.example.vaerappforsvaksynte

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.vaerappforsvaksynte.BuildConfig.MAPS_API_KEY
import com.example.vaerappforsvaksynte.ui.navigation.NavGraph
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.ui.theme.VaerAppForSvaksynteTheme
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forecastViewModel = ForecastViewModel(applicationContext)

        // Initialize Google Places Api
        Places.initialize(applicationContext, MAPS_API_KEY)
        // Get current user location
        getCurrentUserLocation(viewModel = forecastViewModel)

        setContent {
            VaerAppForSvaksynteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val navController = rememberNavController()
                        NavGraph(navController = navController, VM = forecastViewModel)
                    }
                }
            }
        }
    }

    /**
     * Checks if user has previously granted the app access to their current location
     * If user has not been asked for access before, asks user if they would like to permit
     * the app to use their current location
     */
    private fun getUserLocationPermission(viewModel: ForecastViewModel) {
        // Build request
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    Log.d("Permission", "Precise location access granted.")

                    // Update data using user's current location
                    getCurrentUserLocation(viewModel)
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    Log.d("Permission", "Only approximate location access granted..")
                    getCurrentUserLocation(viewModel)
                }
                else -> {
                    // No location access granted.
                    Log.d("Permission", "No location access granted.")
                }
            }
        }

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**
     * Updates the given [viewModel] to use the users current location as the chosen location
     */
     private fun getCurrentUserLocation(viewModel: ForecastViewModel) {
        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        // Create client
        val placesClient = Places.createClient(this)
        // Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {

            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result

                    // update screens with new location
                    if (!response?.placeLikelihoods.isNullOrEmpty()) {
                        val place = response.placeLikelihoods[0].place

                        place.id?.let { getPlaceFromId(viewModel, it) }
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Place not found statusCode: ${exception.statusCode}")
                        Log.e(TAG, "Place not found statusCode: ${exception.message}")
                    }
                }
            }
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getUserLocationPermission(viewModel)
        }
    }

    /**
     * Returns a Place from the given Google [placeId]
     * Note: Done this way because the Google Places API does not give place details such as place city
     * when using the api to fetch user's current location
     */
    private fun getPlaceFromId(viewModel: ForecastViewModel, placeId: String) {
        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG
        )
        // Create client
        val placesClient = Places.createClient(this)

        // Build the request
        val request: FetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields)

        // Fetch response
        val placeResponse = placesClient.fetchPlace(request)

        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result

                val place = response.place

                viewModel.updateForecastFromUsersCurrentLocation(place)
            }
        }
    }
}