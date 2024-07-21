package com.example.vaerappforsvaksynte.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.ui.navigation.Screens
import com.example.vaerappforsvaksynte.R

@Composable
fun MainScreenTopBar(location: String, VM: ForecastViewModel, navController: NavController){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp),
        ) {
            FavoriteLocationButton(viewModel = VM)
        }
        Column(
            modifier = Modifier
                .weight(4f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            LocationAndSearchBox(location = location, navController = navController)
        }
    }
}

@Composable
fun FavoriteLocationButton(viewModel: ForecastViewModel) {
    val uiState = viewModel.forecastUiState.collectAsState()

    val currentLocation = uiState.value.chosenLocation
    val isUsingAFavoriteLocation = uiState.value.isUsingAFavoriteLocation
    val icon = if (isUsingAFavoriteLocation) Icons.Default.Star else Icons.Default.StarOutline

    IconButton(
        modifier = Modifier
            .size(100.dp),
        onClick = {
            if (isUsingAFavoriteLocation) {
                viewModel.deleteLocationFromFavorites(currentLocation)
            } else {
                viewModel.addLocationToFavorites()
            }
        }
    ) {
        Icon(
            modifier = Modifier
                .size(size = 80.dp),
            contentDescription = stringResource(R.string.save_as_favorite),
            imageVector = icon
        )
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun LocationAndSearchBox(
    location: String,
    navController: NavController

) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            // Navigate to ChooseLocationScreen
            navController.navigate(Screens.ChooseLocation.route)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = location,
                        textAlign = TextAlign.Center,
                        lineHeight = 35.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 32.sp
                    )
                }
            }

            Spacer(Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 100.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_for_place)
                )
            }
        }
    }
}