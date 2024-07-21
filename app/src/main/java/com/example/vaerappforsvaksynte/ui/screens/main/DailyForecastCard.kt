package com.example.vaerappforsvaksynte.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.R

@SuppressLint("DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyForecastCard(viewModel: ForecastViewModel, day: DayForecast, navController: NavController) {

    Card (
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
            ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            //Day and date
            if (day.forecastAlertsList.size > 0) {

                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .semantics(mergeDescendants = true) {},
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = day.weekday,
                            fontSize = 54.sp
                        )
                        Text(
                            text = day.date,
                            fontSize = 54.sp
                        )
                    }

                    //Warning icon if any
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 5.dp, end = 15.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End
                    ) {
                        AlertIcon(viewModel, day = day, navController)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .semantics(mergeDescendants = true) {},
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = day.weekday,
                        fontSize = 54.sp
                    )
                    Text(
                        text = day.date,
                        fontSize = 54.sp
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
                WeatherIcon(symbolCode = day.symbolCode)
            }

            //Temperature, daily high and low
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${day.maxTemperature}\u00B0 / ${day.minTemperature}°",
                    fontSize = 58.sp,)
            }

            //Precipitation and wind
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Row which holds the precipitation and wind
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 35.dp, bottom = 20.dp, end = 35.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {

                    //Precipitation
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.Start,
                    ) {

                        //Precipitation icon
                        val context = LocalContext.current
                        val drawableId = remember(day.precipitationSymbol) {
                            context.resources.getIdentifier(
                                day.precipitationSymbol,
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
                            text = "${day.totalPrecipitation} mm",
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
                        val drawableId = remember(day.windSymbol) {
                            context.resources.getIdentifier(
                                day.windSymbol,
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
                            text = "${day.maxWindSpeed} m/s",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                    }
                }
            }
        }
    }
}
