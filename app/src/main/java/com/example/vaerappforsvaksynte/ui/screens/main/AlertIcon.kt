package com.example.vaerappforsvaksynte.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.ui.navigation.Screens
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.R

/**
 * Displays a weather alert icon for a day if there are any alerts for the given [day]
 * Otherwise, displays nothing
*/
@SuppressLint("DiscouragedApi")
@Composable
fun AlertIcon(viewModel: ForecastViewModel, day: DayForecast, navController: NavController) {
    if (day.forecastAlertsList.size > 0) {
        // Todo: handle more than one alert
        val alert = day.forecastAlertsList[0]

        val context = LocalContext.current
        val drawableId = remember(alert.symbolCode) {
            context.resources.getIdentifier(
                alert.symbolCode,
                "drawable",
                context.packageName
            )
        }
        Image(
            modifier = Modifier
                .size(100.dp)
                .clickable(
                    onClick = {
                        viewModel.forecastUiState.value.forecastAlertsHolder =
                            day.forecastAlertsList
                        navController.navigate(Screens.Alert.route)
                    }
                ),
            painter = painterResource(id = drawableId),
            contentDescription = "${stringResource(R.string.alert)}: ${alert.eventName}"
        )
    }

}
