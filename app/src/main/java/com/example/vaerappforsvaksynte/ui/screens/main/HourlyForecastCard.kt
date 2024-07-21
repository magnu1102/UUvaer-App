package com.example.vaerappforsvaksynte.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.data.dayforecast.HourForecast
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.R

@SuppressLint("DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourlyForecastCard(viewModel: ForecastViewModel, day: DayForecast, hour: HourForecast){
    val uiState by viewModel.forecastUiState.collectAsState()

    Card (
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ){

        //Time
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .semantics(mergeDescendants = true) {},
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            val isToday: Boolean = day.date == uiState.dayForecastList?.get(0)?.date

            val weekdayText: String = if (isToday) {
                stringResource(R.string.today)
            } else {
                day.weekday
            }

            Text(
                text= weekdayText,
                fontSize = 54.sp,
            )


            // Displaying of time that creates an interval if the forecast
            // is for the next 6 hours in intervals instead of hour by hour
            val first2NumbersInHour = "${hour.time[0]}${hour.time[1]}"

            if (hour.next1HoursSymbolCode == "") {
                Text(
                    text= "$first2NumbersInHour - ${getIntervalEndTime(hour.time)}",
                    fontSize = 54.sp,
                )
            }
            else {
                Text(
                    text= hour.time,
                    fontSize = 54.sp,
                )
            }

        }

        //Weather icon
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (hour.next1HoursSymbolCode == "") {
                WeatherIcon(symbolCode = hour.next6HoursSymbolCode)
            } else {
                WeatherIcon(symbolCode = hour.next1HoursSymbolCode)
            }
        }

        //Temperature
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "${hour.temperature}\u00B0",
                fontSize = 72.sp,
            )
        }

        //Precipitation and Wind
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 35.dp, bottom = 20.dp, end = 35.dp),
                verticalAlignment = Alignment.Bottom

            ) {

                //Precipitation
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.Start,
                ) {

                    //Precipitation icon
                    val context = LocalContext.current
                    val drawableId = remember(hour.precipitationSymbol) {
                        context.resources.getIdentifier(
                            hour.precipitationSymbol,
                            "drawable",
                            context.packageName
                        )
                    }
                    Image(
                        modifier = Modifier
                            .size(90.dp),
                        painter = painterResource(id = drawableId),
                        contentDescription = stringResource(R.string.precipitation_amount)
                    )

                    //Precipitation text
                    Text(
                        text = "${hour.precipitation} mm",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                //Wind
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.End,
                ) {

                    //Wind icon
                    val context = LocalContext.current
                    val drawableId = remember(hour.windSymbol) {
                        context.resources.getIdentifier(
                            hour.windSymbol,
                            "drawable",
                            context.packageName
                        )
                    }
                    Image(
                        modifier = Modifier
                            .size(80.dp),
                        painter = painterResource(id = drawableId),
                        contentDescription = stringResource(R.string.wind)
                    )

                    //Wind text
                    Text(
                        text = "${hour.windspeed} m/s",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


fun getIntervalEndTime(startTime: String): String {
    var endTime = "08"
    when(startTime){
        "08:00" -> endTime = "14"
        "14:00" -> endTime = "20"
        "20:00" -> endTime = "02"
    }
    return endTime
}