package com.example.vaerappforsvaksynte.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vaerappforsvaksynte.ui.ForecastViewModel
import com.example.vaerappforsvaksynte.ui.screens.alert.AlertScreen
import com.example.vaerappforsvaksynte.ui.screens.chooseLocation.ChooseLocationScreen
import com.example.vaerappforsvaksynte.ui.screens.main.MainScreen

/*
    Composable function which specifies a navigation graph for
    the navigation components
 */

@Composable
fun NavGraph (navController: NavHostController, VM: ForecastViewModel){
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route)
    {
        composable(route = Screens.Main.route) {
            MainScreen(VM, navController)
        }
        composable(route = Screens.Alert.route) {
            AlertScreen(VM, navController)
        }
        composable(route = Screens.ChooseLocation.route) {
            ChooseLocationScreen(VM, navController)
        }
    }
}