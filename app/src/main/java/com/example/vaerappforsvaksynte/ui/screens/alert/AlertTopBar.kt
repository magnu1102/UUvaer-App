package com.example.vaerappforsvaksynte.ui.screens.alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vaerappforsvaksynte.ui.navigation.Screens
import com.example.vaerappforsvaksynte.R

//Top bar menu for the screen that contains screen name and close button
@Composable
fun TopbarAlertScreen (navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Back to main button
        Column(
            modifier = Modifier
                .weight(1f)
        ) {}

        //Screen name
        Column(
            modifier = Modifier
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.alert),
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        //Empty Column with weight for consistency
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton (
                modifier = Modifier
                    .size(80.dp),
                onClick = { navController.navigate(Screens.Main.route) }
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 55.dp),
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.back_to_weather_report)
                )
            }
        }
    }
}
