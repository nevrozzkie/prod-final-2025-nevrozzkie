package com.prodfinal2025.nevrozq.navigation.bottomBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pushToFront
import com.prodfinal2025.nevrozq.navigation.root.RootComponent
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Config

@Composable
fun DefaultNavigationBar(
    childStack: ChildStack<*, RootComponent.Child>,
    component: RootComponent,
    items: List<NavigationButtonItem> = getNavigationItems()
) {

    val backgroundColor = MaterialTheme.colorScheme.background

    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.Black,
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
    )


    Column(Modifier.background(backgroundColor)) {
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
        NavigationBar(
            containerColor = backgroundColor
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = (childStack.active.configuration as Config) == item.config,
                    onClick = { component.onOutput(item.config) },
                    icon = { Icon(item.icon, null) },
                    label = {
                        Text(item.label)
                    },
                    alwaysShowLabel = true,
                    colors = itemColors
                )
            }
        }
    }
}