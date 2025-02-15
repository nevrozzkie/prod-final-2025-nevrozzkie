package com.prodfinal2025.nevrozq.navigation.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.prodfinal2025.nevrozq.navigation.root.RootComponent

data class NavigationButtonItem(
    val icon: ImageVector,
    val label: String,
    val config: RootComponent.Config
)

enum class NavigationItems(
    private val icon: ImageVector,
    private val label: String,
    private val config: RootComponent.Config
) {
    HOME(Icons.Rounded.Home, "Главная", RootComponent.Config.Main),
    FINANCE(Icons.Rounded.Savings, "Финансы", RootComponent.Config.Finance),
    SOCIAL_FEED(Icons.Rounded.RssFeed, "Лента", RootComponent.Config.SocialFeed);
//    PROFILE(ProfileIcon, "Profile", ProfileConfig),
//    SETTINGS(SettingsIcon, "Settings", SettingsConfig)

    fun toNavigationButtonItem(): NavigationButtonItem {
        return NavigationButtonItem(icon, label, config)
    }
}

fun getNavigationItems() = NavigationItems.entries.map { it.toNavigationButtonItem() }