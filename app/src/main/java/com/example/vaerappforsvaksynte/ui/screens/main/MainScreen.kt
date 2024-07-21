package com.example.vaerappforsvaksynte.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.ui.screens.*
import com.example.vaerappforsvaksynte.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(VM: ForecastViewModel, navController: NavController){
    val uiState = VM.forecastUiState.collectAsState()

    // Show error message if an error occurred
    val errorMessage by VM.errorMessageUiState.collectAsState()
    val context = LocalContext.current

    if (errorMessage != null) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG)
            .show()
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ){

        // Column for TopCard
        Column(modifier = Modifier
            .weight(2f)
            .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            MainScreenTopBar(uiState.value.chosenLocation, VM, navController)
        }

        val listState = rememberLazyListState()
        val days = uiState.value.dayForecastList

        // Column for the main section with lazyrow and lazycolumn
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(19f)
        ) {
            if (days == null) {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${stringResource(R.string.loading)}...",
                        fontSize = 36.sp,
                    )
                }
            } else {
                LazyRow(modifier = Modifier.fillMaxSize(),
                    state = listState,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
                ){
                    items(days.size) {
                        Column (Modifier.fillParentMaxSize()){
                            MainScreenContent(VM, it, navController)
                        }
                    }
                }
            }
        }
    }
}



