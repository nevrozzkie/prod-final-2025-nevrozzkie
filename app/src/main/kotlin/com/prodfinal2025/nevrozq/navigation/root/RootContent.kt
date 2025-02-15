package com.prodfinal2025.nevrozq.navigation.root

import FinanceScreen
import MainScreen
import SocialFeedScreen
import android.annotation.SuppressLint
import android.provider.DocumentsContract.Root
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.getValue
import com.prodfinal2025.nevrozq.navigation.bottomBar.DefaultNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(
    component: RootComponent
) {
    val stack by component.stack.subscribeAsState()
    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            DefaultNavigationBar(
                stack, component
            )
        }
    ) { padding ->

        Children(
            stack = stack,
            animation = predictiveBackAnimation(
                backHandler = component.backHandler,
                fallbackAnimation = stackAnimation(fade()),
                onBack = component::onBackClicked
            )
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.MainChild -> MainScreen(
                    child.component
                )
                is RootComponent.Child.FinanceChild -> FinanceScreen(
                    child.component
                )

                is RootComponent.Child.SocialFeedChild -> SocialFeedScreen(
                    child.component
                )
            }
        }
    }
}