import android.content.SharedPreferences
import view.ThemeTint
import view.ViewManager
import view.themeSettings

fun changeTheme(theme: ThemeTint, viewManager: ViewManager, prefs: SharedPreferences) {
    prefs.edit().putString(themeSettings, theme.name).apply()
    viewManager.tint.value = theme
}
