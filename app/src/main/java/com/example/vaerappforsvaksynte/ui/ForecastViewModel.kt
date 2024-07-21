package com.example.vaerappforsvaksynte.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.data.nowforecast.NowForecast
import com.example.vaerappforsvaksynte.data.location.LocationDto
import com.example.vaerappforsvaksynte.data.repository.LocationRepository
import com.example.vaerappforsvaksynte.data.repository.ForecastRepository
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * This class is responsible for exposing data to the UI components and performing UI logic
 */
class ForecastViewModel(context: Context) : ViewModel() {
    private val _forecastUiState = MutableStateFlow(
        ForecastUiState(
            chosenLocation = "Oslo",
            latitude = 59.9,
            longitude = 10.7,
            nowForecastObject = NowForecast(),
            dayForecastList = null,
            favoriteLocations = listOf(),
            isUsingAFavoriteLocation = false
        )
    )

    val forecastUiState: StateFlow<ForecastUiState> = _forecastUiState.asStateFlow()

    private val _errorMessageUiState = MutableStateFlow<String?>(null)
    val errorMessageUiState = _errorMessageUiState.asStateFlow()

    private val forecastRepo = ForecastRepository()
    private val locationRepo = LocationRepository(context = context)

    init {
        initialDataLoad()
    }

    //Function initialized by the classes init method for initial data load of the
    // nowCast API
    private fun initialDataLoad() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteLocations = locationRepo.getLocations()

            try {
                val nowForecast = forecastRepo.getNowForecast(lat = forecastUiState.value.latitude, lon = forecastUiState.value.longitude)
                val dayForecastList = forecastRepo.getDayForecasts(lat = forecastUiState.value.latitude, lon = forecastUiState.value.longitude)

                _forecastUiState.value = ForecastUiState(
                    chosenLocation = "Oslo",
                    latitude = 59.9,
                    longitude = 10.7,
                    nowForecastObject = nowForecast,
                    dayForecastList = dayForecastList,
                    favoriteLocations = favoriteLocations,
                    isUsingAFavoriteLocation = (locationRepo.getLocation("Oslo") != null)
                )
            } catch (cause: Throwable) {
                Log.d(TAG, "ERROR: ${cause.message.toString()}")
                _errorMessageUiState.value = cause.message
                _forecastUiState.value = ForecastUiState(
                    chosenLocation = "Oslo",
                    latitude = 59.9,
                    longitude = 10.7,
                    favoriteLocations = favoriteLocations,
                    isUsingAFavoriteLocation = (locationRepo.getLocation("Oslo") != null)
                )
            }
        }
    }


    /**
     * Updates the ui state with the given [locationName], [lat] and [lon]
     * [fromCurrentLocation] specifies whether the new location comes
     * from using users current position
     */
    private fun updateForecast(
        locationName: String,
        lat: Double,
        lon: Double,
        fromCurrentLocation: Boolean = false) {
            viewModelScope.launch(Dispatchers.IO) {

                // Update list of favorite locations and check if the current location is a favorite
                updateFavoriteLocations()
                val isUsingAFavoriteLocation =
                    locationRepo.getLocation(locationName = locationName) != null

                var nowForecastObject: NowForecast? = null
                var dayForecastList: List<DayForecast>? = null

                // Try to fetch forecasts from network
                try {
                     nowForecastObject = forecastRepo.getNowForecast(lat, lon)
                     dayForecastList = forecastRepo.getDayForecasts(lat, lon)
                    _errorMessageUiState.value = null
                } catch (e: Throwable) {
                    _errorMessageUiState.value = e.message
                }

                // Update UI state
                _forecastUiState.value = ForecastUiState(
                    chosenLocation = locationName,
                    latitude = lat,
                    longitude = lon,
                    nowForecastObject = nowForecastObject,
                    dayForecastList = dayForecastList,
                    isUsingCurrentLocation = fromCurrentLocation,
                    isUsingAFavoriteLocation = isUsingAFavoriteLocation,
                    favoriteLocations = locationRepo.getLocations()
                )
            }
    }

    /**
     * Updates the ui state using the name, lat and lon of the given [place]
     */
    fun updateForecastFromGoogleSearchLocation(place: Place) {
        val locationName = place.name
        val lat = place.latLng?.latitude
        val lon = place.latLng?.longitude

        if (locationName != null && lat != null && lon != null) {
            updateForecast(locationName = locationName, lat = lat, lon = lon)
        } else {
            throw Throwable("Valgt sted kan ikke lastes inn")
        }
    }

    /**
     * Updates the ui state with location information from the given [place]
     * and that the location is currently coming from using users current position
     */
    fun updateForecastFromUsersCurrentLocation(place: Place) {
        var name = place.name

        // Find name - use point of interest if available, else city name, else default place name
        val addressComponents = place.addressComponents
        val addressComponentsList: List<AddressComponent> = if (addressComponents != null) {
            addressComponents.asList()
        } else {
            emptyList()
        }

        for (addressComp in addressComponentsList) {
            if ("point_of_interest" in addressComp.types) {
                name = addressComp.shortName
                break
            } else if ("postal_town" in addressComp.types) {
                name = addressComp.shortName
            }
        }

        val locationName = name
        val lat = place.latLng?.latitude
        val lon = place.latLng?.longitude

        var inNorway = false
        for (addressComp in addressComponentsList) {
            if (addressComp.shortName == "NO") {
                inNorway = true
            }
        }
        if (!inNorway) {
            throw Throwable(message = "Kan ikke vise værmelding utenfor Norge")
        } else if (locationName != null && lat != null && lon != null) {
            updateForecast(
                locationName = locationName,
                lat = lat,
                lon = lon,
                fromCurrentLocation = true,
            )
        } else {
            throw Throwable("Valgt sted kan ikke lastes inn")
        }
    }

    // Updates ui state with values from the given favorite [location]
    fun updateForecastFromFavoriteLocation(location: LocationDto) {
        updateForecast(
            locationName = location.name,
            lat = location.lat,
            lon = location.lon,
        )
    }

    // Saves the current location in ui state to favorite locations
    fun addLocationToFavorites() {
        val newLocation = LocationDto(
            name = forecastUiState.value.chosenLocation,
            lat = forecastUiState.value.latitude,
            lon = forecastUiState.value.longitude)

        viewModelScope.launch(Dispatchers.IO) {
            locationRepo.addLocation(newLocation)

            updateForecast(
                locationName = newLocation.name,
                lat = newLocation.lat,
                lon = newLocation.lon,
                fromCurrentLocation = forecastUiState.value.isUsingCurrentLocation
            )
        }
    }

    fun deleteLocationFromFavorites(locationName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _forecastUiState.value = ForecastUiState(
                chosenLocation = _forecastUiState.value.chosenLocation,
                latitude = _forecastUiState.value.latitude,
                longitude = _forecastUiState.value.longitude,
                nowForecastObject = _forecastUiState.value.nowForecastObject,
                dayForecastList = _forecastUiState.value.dayForecastList,
                isUsingCurrentLocation = _forecastUiState.value.isUsingCurrentLocation,
                forecastAlertsHolder = _forecastUiState.value.forecastAlertsHolder,
                favoriteLocations = locationRepo.deleteLocation(locationName),
                isUsingAFavoriteLocation = false
            )
        }
    }

    private fun updateFavoriteLocations() {
        viewModelScope.launch(Dispatchers.IO) {

            // Update state
            _forecastUiState.value = ForecastUiState(
                _forecastUiState.value.chosenLocation,
                _forecastUiState.value.latitude,
                _forecastUiState.value.longitude,
                _forecastUiState.value.nowForecastObject,
                _forecastUiState.value.dayForecastList,
                isUsingCurrentLocation = _forecastUiState.value.isUsingCurrentLocation,
                isUsingAFavoriteLocation = (locationRepo.getLocation(_forecastUiState.value.chosenLocation) != null),
                forecastAlertsHolder = _forecastUiState.value.forecastAlertsHolder,
                favoriteLocations = locationRepo.getLocations())
        }
    }
}
