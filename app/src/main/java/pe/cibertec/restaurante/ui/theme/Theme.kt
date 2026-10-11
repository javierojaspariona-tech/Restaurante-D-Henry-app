package pe.cibertec.restaurante.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    secondary = Primary,
    background = Dark,
    surface = Secondary,
    onPrimary = Dark,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Danger
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Accent,
    background = Background,
    surface = Surface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = Danger
)

@Composable
fun RestauranteAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun DVeranoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    RestauranteAppTheme(darkTheme = darkTheme, content = content)
}
