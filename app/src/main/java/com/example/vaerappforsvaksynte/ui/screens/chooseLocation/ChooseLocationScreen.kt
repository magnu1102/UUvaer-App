package com.example.vaerappforsvaksynte.ui.screens.chooseLocation

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.data.location.LocationDto
import com.example.vaerappforsvaksynte.ui.navigation.Screens
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.ui.theme.OurOrange
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.example.vaerappforsvaksynte.R

@Composable
fun ChooseLocationScreen (VM: ForecastViewModel, navController: NavController) {
    val uiState by VM.forecastUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        //Topbar
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(OurOrange),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ChooseLocationTopbar(navController)
        }

        //Search and use location button
        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UseMyLocationButton(VM = VM, navController = navController)
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    SearchButton(VM = VM, navController = navController)
                }
            }

            //Favorite locations

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .weight(15f)
        ) {

            item {
                Text(
                    text = stringResource(R.string.choose_among_favorites),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(uiState.favoriteLocations) {location ->
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    DisplayFavoritesAndDelete(VM = VM, navController = navController, location = location)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
fun UseMyLocationButton(VM: ForecastViewModel, navController: NavController) {
    var updateToUserLocation by remember { mutableStateOf(false) }

    Button(
        onClick = {
          updateToUserLocation = true
        },
        modifier = Modifier
            .size(115.dp),
            //.padding(start = 30.dp, end = 10.dp),
        shape = RoundedCornerShape(size = 20.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "",
                modifier = Modifier
                    .size(60.dp),

            )
        }
    }

    if (updateToUserLocation) {
        UpdateToCurrentUserLocation(viewModel = VM)
        navController.navigate(Screens.Main.route)
        updateToUserLocation = false
    }
}

// Source code for Google Autocomplete https://developers.google.com/maps/documentation/places/android-sdk/autocomplete#option_2_use_an_intent_to_launch_the_autocomplete_activity
@Composable
fun SearchButton(VM: ForecastViewModel, navController: NavController) {
    val context = LocalContext.current

    // Define which values to return
    val field = listOf(Place.Field.NAME, Place.Field.LAT_LNG)

    // Build search bar with parameter set in Norway
    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, field)
        .setCountry("no")
        .build(context)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val place = it.data?.let { it1 -> Autocomplete.getPlaceFromIntent(it1) }

            // Updates the view model with the new location
            if (place != null) {
                VM.updateForecastFromGoogleSearchLocation(place = place)
            }
            navController.navigate(Screens.Main.route)
        }
        else {
            Log.d("ERROR when fetching from Google Places Api. result code: ",
                it.resultCode.toString())
        }
    }

    Button(
        onClick = {
            launcher.launch(intent)
                  },
        modifier = Modifier
            //.padding(start = 10.dp, end = 10.dp)
            .size(115.dp),
        shape = RoundedCornerShape(size = 20.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),

        ) {
        Column {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .size(70.dp))
        }
    }
}


@Composable
fun DisplayFavoritesAndDelete(VM: ForecastViewModel, navController: NavController, location: LocationDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Location button:
        Column(
            modifier = Modifier
                .weight(5f)
        ) {
            Button(
                onClick = {
                    VM.updateForecastFromFavoriteLocation(location = location)
                    navController.navigate(Screens.Main.route) },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(size = 20.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),

                ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = location.name,
                    textAlign = TextAlign.Start,
                    lineHeight = 35.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        //Delete button:
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DeleteButton(VM = VM, locationName = location.name)
        }
    }
}

@Composable
fun DeleteButton (VM: ForecastViewModel, locationName: String) {
    IconButton(
        onClick = { VM.deleteLocationFromFavorites(locationName) }
    ) {
        Icon(
            modifier = Modifier
                .size(size = 100.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.remove_place_from_favorites)
        )
    }
}

/**
 * Updates the given [viewModel] to use the users current location as the chosen location
 */
@Composable
fun UpdateToCurrentUserLocation(viewModel: ForecastViewModel) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    // Use fields to define the data types to return.
    val placeFields: List<Place.Field> =
        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
    // Create client
    val placesClient = Places.createClient(applicationContext)
    // Use the builder to create a FindCurrentPlaceRequest.
    val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

    // Call findCurrentPlace and handle the response (first check that the user has granted permission).
    if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED
    ) {

        val placeResponse = placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result

                // update screens with new location
                if (!response?.placeLikelihoods.isNullOrEmpty()) {

                    val place = response.placeLikelihoods[0].place

                    // Get name of POI or city for place
                    if (place.id != null) {
                        try {
                            place.id?.let {
                                getPlaceFromId(
                                    viewModel = viewModel,
                                    placesClient = placesClient,
                                    placeId = it
                                )
                            }
                        } catch (cause: Throwable) {
                            Toast.makeText(context, cause.message, Toast.LENGTH_LONG)
                                .show()
                        }

                    } else {
                        Toast.makeText(context, "Kunne ikke laste inn nåværende posisjon", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Log.e(ContentValues.TAG, "Place not found statusCode: ${exception.statusCode}")
                    Log.e(ContentValues.TAG, "Place not found statusCode: ${exception.message}")
                    Toast.makeText(context, "Kunne ikke laste inn nåværende posisjon", Toast.LENGTH_LONG).show()
                }
            }
        }
    } else {
        // A local method to request required permissions;
        // See https://developer.android.com/training/permissions/requesting
        // getUserLocationPermission(viewModel)
        Toast.makeText(context, "App har ikke tillatelse til å bruke posisjon", Toast.LENGTH_LONG).show()
    }
}

/**
 * Returns a Place from the given Google [placeId]
 * Note: Done this way because the Google Places API does not give place details such as place city
 * when using the api to fetch user's current location
 */
fun getPlaceFromId(
    viewModel: ForecastViewModel,
    placesClient: PlacesClient,
    placeId: String) {
    // Use fields to define the data types to return.
    val placeFields: List<Place.Field> = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS_COMPONENTS,
        Place.Field.LAT_LNG
    )

    // Build the request
    val request: FetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields)

    // Fetch response
    val placeResponse = placesClient.fetchPlace(request)

    placeResponse.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val response = task.result

            val place = response.place

            // Update ViewModel with new location
            viewModel.updateForecastFromUsersCurrentLocation(place)
        }
    }
}

