package com.example.vaerappforsvaksynte.ui.screens.alert

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.data.forecastalert.ForecastAlert
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.ui.theme.OurOrange
import com.example.vaerappforsvaksynte.R

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AlertScreen(VM: ForecastViewModel, navController: NavController) {
    val forecastAlertsList: MutableList<ForecastAlert> = VM.forecastUiState.value.forecastAlertsHolder

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .background(OurOrange),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopbarAlertScreen(navController)
        }

        LazyColumn(
            modifier = Modifier
                .weight(20f)
                .padding(20.dp)
        ) {
            item {
                if (forecastAlertsList.size > 1) {
                    Text(
                        text = "${stringResource(R.string.multiple_alerts)}\n",
                        fontSize = 24.sp,
                        lineHeight = 36.sp)
                }
            }

            items(forecastAlertsList) { alert ->
                Text(
                    text = alert.eventName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 54.sp
                )
                Text(
                    text = "\n${stringResource(R.string.area)} ${alert.area}",
                    fontSize = 24.sp,
                    lineHeight = 36.sp)
                Text(
                    text = "\n${alert.timeInterval}",
                    fontSize = 24.sp,
                    lineHeight = 36.sp)
                Text(
                    text = "\n${alert.consequences}" +
                            "\n\n${alert.description}" +
                            "\n\n${alert.instruction}\n\n",
                    fontSize = 24.sp,
                lineHeight = 36.sp)
            }
        }
    }
}
