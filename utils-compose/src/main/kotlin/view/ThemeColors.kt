package view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import view.theme.Colors

data class ThemeColors(
    val green: Color,
)

val themeColors: ThemeColors
    @Composable @ReadOnlyComposable get() = LocalThemeColors.current

val DarkThemeColors = ThemeColors(
    green = Colors.greenDarkPrimary
)

val LightThemeColors = ThemeColors(
    green = Colors.greenLightPrimary,
)

val LocalThemeColors: ProvidableCompositionLocal<ThemeColors> = compositionLocalOf {
    error("No ThemeColors provided")
}