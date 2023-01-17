package fr.gilles.riceattend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.gilles.riceattend.services.storage.RiceAttendTheme
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.services.storage.themeFromString

private val DarkColorPalette = darkColors(
    primary = DarkGreen,
    primaryVariant = SecondaryGreen,
    secondary = Secondary,
    background = DarkBackground,
    error = Error
)

private val LightColorPalette = lightColors(
    primary = LightGreen,
    primaryVariant = SecondaryGreen,
    secondary = Secondary,
    background = LightBackground,
    error = Error

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)


@Composable
fun RiceAttendTheme( darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    var userPreferences =  when (themeFromString(SessionManager.session.preferences["theme"] ?: "system")) {
        RiceAttendTheme.LIGHT -> false
        RiceAttendTheme.DARK -> true
        RiceAttendTheme.SYSTEM -> darkTheme
    }
    val systemUiController = rememberSystemUiController()
    val colors = if (userPreferences) {
        systemUiController.setSystemBarsColor(
            color = DarkBackground
        )
        DarkColorPalette
    } else {
        systemUiController.setSystemBarsColor(
            color = LightBackground
        )
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}