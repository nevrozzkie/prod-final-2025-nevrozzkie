package com.prodfinal2025.nevrozq.navigation.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.ui.graphics.vector.ImageVector
import com.prodfinal2025.nevrozq.navigation.root.RootComponent
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Child
import kotlin.reflect.KClass

data class NavigationButtonItem(
    val icon: ImageVector,
    val label: String,
    val config: RootComponent.Config,
    val child: KClass<out Child>
)

enum class NavigationItems(
    private val icon: ImageVector,
    private val label: String,
    private val config: RootComponent.Config,
    val child: KClass<out Child>
) {
    HOME(Icons.Rounded.Home, "Главная", RootComponent.Config.Main, Child.MainChild::class),
    FINANCE(Icons.Rounded.Savings, "Финансы", RootComponent.Config.Finance, Child.FinanceChild::class),
    SOCIAL_FEED(Icons.Rounded.RssFeed, "Лента", RootComponent.Config.Social(null), Child.SocialChild::class);
//    PROFILE(ProfileIcon, "Profile", ProfileConfig),
//    SETTINGS(SettingsIcon, "Settings", SettingsConfig)

    fun toNavigationButtonItem(): NavigationButtonItem {
        return NavigationButtonItem(icon, label, config, child)
    }
}

fun getNavigationItems() = NavigationItems.entries.map { it.toNavigationButtonItem() }