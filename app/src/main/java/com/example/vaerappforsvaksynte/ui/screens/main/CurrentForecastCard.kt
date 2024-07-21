package com.example.vaerappforsvaksynte.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.R

@SuppressLint("DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentForecastCard(viewModel: ForecastViewModel, navController: NavController){
    val uiState by viewModel.forecastUiState.collectAsState()

    Card (
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
            ) {

        //Time
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            if (uiState.dayForecastList?.get(0)?.forecastAlertsList?.size!! > 0) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Column (
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .weight(2f)
                            .semantics(mergeDescendants = true) {},
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                            ) {
                        Text(
                            text= stringResource(R.string.now),
                            fontSize = 64.sp,
                        )
                    }

                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 5.dp, end = 15.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End
                    ) {
                        AlertIcon(viewModel, uiState.dayForecastList!![0], navController)
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text= stringResource(R.string.now),
                        fontSize = 64.sp,
                    )
                }
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

            WeatherIcon(symbolCode = uiState.nowForecastObject!!.symbol)
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
                text = "${uiState.nowForecastObject!!.temperature}\u00B0",
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
                    horizontalAlignment = Alignment.Start
                    ) {

                    //Precipitation icon
                    val context = LocalContext.current
                    val drawableId = remember(uiState.nowForecastObject!!.precipitationSymbol) {
                        context.resources.getIdentifier(
                            uiState.nowForecastObject!!.precipitationSymbol,
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
                        text = "${uiState.nowForecastObject!!.precipitation} mm",
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
                    val drawableId = remember(uiState.nowForecastObject!!.windSymbol) {
                        context.resources.getIdentifier(
                            uiState.nowForecastObject!!.windSymbol,
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
                        text = "${uiState.nowForecastObject!!.windSpeed} m/s",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold
                        )
                }
            }
        }
    }
}
