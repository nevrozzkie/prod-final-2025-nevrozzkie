package view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

data class ViewManager(
    val tint: MutableState<ThemeTint>,
    val isDark: MutableState<Boolean> = mutableStateOf(true)
)

val LocalViewManager: ProvidableCompositionLocal<ViewManager> = compositionLocalOf {
    error("No ViewManager provided")
}

fun getViewManager(themeTintName: String): ViewManager {
    return ViewManager(
        tint = mutableStateOf(getThemeTint(themeTintName))
    )
}

fun getThemeTint(themeTintName: String): ThemeTint = when(themeTintName) {
    ThemeTint.Dark.name -> ThemeTint.Dark
    ThemeTint.Light.name -> ThemeTint.Light
    else -> ThemeTint.Auto
}