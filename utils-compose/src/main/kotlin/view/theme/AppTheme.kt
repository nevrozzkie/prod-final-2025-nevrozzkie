package view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import view.LocalViewManager
import view.ThemeTint

@Composable
fun AppTheme(isDarkPriority: Boolean = false, content: @Composable () -> Unit) {
    val viewManager = LocalViewManager.current

    viewManager.isDark.value = isThemeDark(isDarkPriority, viewManager.tint.value)

    val colorScheme = if (viewManager.isDark.value)
        greenDarkColorScheme() else greenLightColorScheme()

    SystemBarsColorFix(viewManager)

    MaterialTheme(
        colorScheme = colorScheme.animated(),

    ) {
        content()
    }
}

@Composable
fun isThemeDark(isDarkPriority: Boolean, tint: ThemeTint) = isDarkPriority ||
        if (tint == ThemeTint.Auto) isSystemInDarkTheme()
        else tint == ThemeTint.Dark
