package com.prodfinal2025.nevrozq.navigation.root

import AnimateSlideVertically
import BarShadow
import FinanceScreen
import MainScreen
import NewsWebView
import SocialFlowScreen
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.prodfinal2025.nevrozq.navigation.bottomBar.DefaultNavigationBar

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootFlowScreen(
    component: RootComponent
) {
    val stack by component.stack.subscribeAsState()
    Scaffold(
        bottomBar = {
            val isShowing =
                !RootComponent.Child.NewYorkTimesChild::class.isInstance(stack.active.instance)

            Box(Modifier.animateContentSize()) {

                AnimateSlideVertically(
                    isShowing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DefaultNavigationBar(
                        stack, component
                    )
                }
            }
        }
    ) { paddings ->
        Children(
            stack = stack,
            animation = predictiveBackAnimation(
                backHandler = component.backHandler,
                fallbackAnimation = stackAnimation { child ->
                    when (child.instance) {
                        is RootComponent.Child.NewYorkTimesChild -> slide() + fade()
                        else -> fade()
                    }
                },
                onBack = component::onBackClicked
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            Box {
                Box(
                    Modifier
                        // https://github.com/google/accompanist/issues/938?ysclid=m7c09cdesr792867624
                        .padding(
                            PaddingValues(
                                bottom = with(LocalDensity.current) {
                                    WindowInsets.ime.getBottom(this)
                                        .toDp() - paddings.calculateBottomPadding()
                                }.coerceAtLeast(0.dp)
                            )
                        )
                ) {
                    when (val child = it.instance) {
                        is RootComponent.Child.MainChild -> {
                            MainScreen(
                                child.component
                            )
                        }

                        is RootComponent.Child.FinanceChild -> FinanceScreen(
                            child.component
                        )

                        is RootComponent.Child.SocialChild -> SocialFlowScreen(
                            child.component
                        )

                        is RootComponent.Child.NewYorkTimesChild -> NewsWebView(
                            child.component
                        )
                    }
                }
                BarShadow(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    isReversed = true
                )
            }
        }
    }
}