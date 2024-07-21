package com.example.vaerappforsvaksynte.ui.screens.main

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vaerappforsvaksynte.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.io.InputStream

/*
File containing UI components that display weather icons
Weather icons are uniquely identified by a symbolCode that is held inside CurrentWeather and DailyForecast-objects
To display a weather icon, insert WeatherIcon(symbolCode: String?) where you would like the icon to appear
 */

private val SYMBOLCODEDESCRIPTIONS = mapOf(
    "clearsky" to "Klarvær",
    "cloudy" to "Skyet",
    "fair" to "Lettskyet",
    "fog" to "Tåke",
    "heavyrain" to "Kraftig regn",
    "heavyrainandthunder" to "Kraftig regn og torden",
    "heavyrainshowers" to "Kraftige regnbyger",
    "heavyrainshowersandthunder" to "Kraftige regnbyger og torden",
    "heavysleet" to "Kraftig sludd",
    "heavysleetandthunder" to "Kraftig sludd og torden",
    "heavysleetshowers" to "Kraftige sluddbyger",
    "heavysleetshowersandthunder" to "Kraftige sluddbyger og torden",
    "heavysnow" to "Kraftig snø",
    "heavysnowandthunder" to "Kraftig snø og torden",
    "heavysnowshowers" to "Kraftige snøbyger",
    "heavysnowshowersandthunder" to "Kraftige snøbyger og torden",
    "lightrain" to "Lett regn",
    "lightrainandthunder" to "Lett regn og torden",
    "lightrainshowers" to "Lette regnbyger",
    "lightrainshowersandthunder" to "Lette regnbyger og torden",
    "lightsleet" to "Lett sludd",
    "lightsleetandthunder" to "Lett sludd og torden",
    "lightsleetshowers" to "Lette sluddbyger",
    "lightsnow" to "Lett snø",
    "lightsnowandthunder" to "Lett snø og torden",
    "lightsnowshowers" to "Lette snøbyger",
    "lightssleetshowersandthunder" to "Lette sluddbyger og torden",
    "lightssnowshowersandthunder" to "Lette snøbyger og torden",
    "partlycloudy" to "Delvis skyet",
    "rain" to "Regn",
    "rainandthunder" to "Regn og torden",
    "rainshowers" to "Regnbyger",
    "rainshowersandthunder" to "Regnbyger og torden",
    "sleet" to "Sludd",
    "sleetandthunder" to "Sludd og torden",
    "sleetshowers" to "Sluddbyger",
    "sleetshowersandthunder" to "Sluddbyger og torden",
    "snow" to "Snø",
    "snowandthunder" to "Snø og torden",
    "snowshowers" to "Snøbyger",
    "snowshowersandthunder" to "Snøbyger og torden"
)

// Displays a weather icon from a given symbolCode
@Composable
fun WeatherIcon(symbolCode: String?) {
    Image(
        painter = _GetPainterFromSymbolCode(symbolCode = symbolCode),
        contentDescription = getDescriptionFromSymbolCode(symbolCode),
        modifier = Modifier.size(135.dp)
    )
}

// Returns the Norwegian description of the given [symbolCode]
private fun getDescriptionFromSymbolCode(symbolCode: String?): String? {
    if (symbolCode == null) return null

    val symbolCodeBaseString = symbolCode.substringBefore("_")

    return if (symbolCodeBaseString == "" || symbolCodeBaseString !in SYMBOLCODEDESCRIPTIONS) {
        null
    } else SYMBOLCODEDESCRIPTIONS[symbolCodeBaseString]
}

// Helper function to make a Drawable Painter-resource from a String symbolCode
@Composable
private fun _GetPainterFromSymbolCode(symbolCode: String?): Painter {

    return if (symbolCode == null || symbolCode == "") {
        painterResource(id = R.drawable.no_image_found)
    } else {
        val context = LocalContext.current
        val ims: InputStream = context.assets.open("${symbolCode}.png")
        val d = Drawable.createFromStream(ims, null)
        rememberDrawablePainter(drawable = d)
    }
}