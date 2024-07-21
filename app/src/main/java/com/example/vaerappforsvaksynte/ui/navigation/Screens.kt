package com.example.vaerappforsvaksynte.ui.navigation


/*
    Class which defines an object for each screen to be used
    in navigation components
 */

sealed class Screens(val route: String) {
    object Main: Screens("main_screen")
    object Alert: Screens("alert_screen")
    object ChooseLocation: Screens("choose_location_screen")
}