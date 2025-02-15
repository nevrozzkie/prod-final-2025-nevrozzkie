package com.prodfinal2025.nevrozq.navigation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import finance.FinanceComponent
import main.MainComponent
import kotlinx.serialization.Serializable
import socialFeed.SocialFeedComponent

interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class MainChild(val component: MainComponent) : Child()
        data class FinanceChild(val component: FinanceComponent) : Child()
        data class SocialFeedChild(val component: SocialFeedComponent) : Child()
    }


    @Serializable
    sealed interface Config {
        @Serializable
        data object Main : Config
        @Serializable
        data object Finance : Config
        @Serializable
        data object SocialFeed : Config
    }

    fun onBackClicked()

    fun onOutput(config: Config)
}