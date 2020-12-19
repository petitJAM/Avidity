package theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val green = Color(0xFF1b5e20)
val lightGreen = Color(0xFF487e4c)
val darkGreen = Color(0xFF124116)

val yellow = Color(0xFFf9a825)
val lightYellow = Color(0XFFfab950)
val darkYellow = Color(0xFFae7519)

val AvidityLightColorPalette = lightColors(
    primary = green,
    primaryVariant = lightGreen,
    secondary = yellow,
    secondaryVariant = darkYellow,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    background = Color.White,
    surface = Color.White,
    error = Color.Red,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
)

val AvidityDarkColorPalette = darkColors(
    primary = green,
    primaryVariant = lightGreen,
    secondary = darkYellow,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color.Red,
    onError = Color.Black,
)