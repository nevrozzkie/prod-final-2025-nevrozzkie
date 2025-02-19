package view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import view.DarkThemeColors
import view.LightThemeColors
import view.LocalThemeColors
import view.LocalViewManager
import view.ThemeTint

@Composable
fun AppTheme(isDarkPriority: Boolean = false, content: @Composable () -> Unit) {
    val viewManager = LocalViewManager.current

    viewManager.isDark.value = isThemeDark(isDarkPriority, viewManager.tint.value)

    val colorScheme = if (viewManager.isDark.value)
        DarkColorScheme else LightColorScheme

    val themeColors = if (viewManager.isDark.value) DarkThemeColors else LightThemeColors

    SystemBarsColorFix(viewManager)

    CompositionLocalProvider(
        LocalThemeColors provides themeColors,
        // fix xiaomi dark theme with dark color
        LocalContentColor provides colorScheme.onSurface
    ) {
        MaterialTheme(
            colorScheme = colorScheme.animated()
        ) {
            content()
        }
    }
}

@Composable
fun isThemeDark(isDarkPriority: Boolean, tint: ThemeTint) = isDarkPriority ||
        if (tint == ThemeTint.Auto) isSystemInDarkTheme()
        else tint == ThemeTint.Dark
