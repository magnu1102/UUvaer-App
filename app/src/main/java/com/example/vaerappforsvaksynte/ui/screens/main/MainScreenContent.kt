package com.example.vaerappforsvaksynte.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.data.dayforecast.DayForecast
import com.example.vaerappforsvaksynte.ui.ForecastViewModel

//Composable function enabling scrolling through daily and hourly forecast cards
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(VM: ForecastViewModel, index: Int, navController : NavController) {
    val uiState = VM.forecastUiState.collectAsState()

    val hoursLeftInDay: Int = uiState.value.dayForecastList?.get(index)!!.hourForecastList.size
    val dayForecastObject: DayForecast = uiState.value.dayForecastList!![index]

    ElevatedCard(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val lazyColumnState = rememberLazyListState()
            LazyColumn(
                state = lazyColumnState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyColumnState)
            ){

                if (index == 0){
                    item {
                        Column (modifier = Modifier.fillParentMaxSize()) { CurrentForecastCard(VM, navController) }
                    }
                    items(hoursLeftInDay){
                        Column(modifier = Modifier.fillParentMaxSize()) {
                            HourlyForecastCard(
                                viewModel = VM,
                                day = dayForecastObject,
                                hour = dayForecastObject.hourForecastList[it],
                            )
                        }
                    }
                } else {
                    item {
                        Column (modifier = Modifier.fillParentMaxSize()) { DailyForecastCard(VM, dayForecastObject, navController) }
                    }
                    items(hoursLeftInDay){
                        Column(modifier = Modifier.fillParentMaxSize()) {
                            HourlyForecastCard(
                                viewModel = VM,
                                day = dayForecastObject,
                                hour = dayForecastObject.hourForecastList[it],
                            )
                        }
                    }
                }
            }
        }
    }
}



