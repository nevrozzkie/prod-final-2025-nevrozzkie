package com.prodfinal2025.nevrozq.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import main.MainComponent
import kotlinx.serialization.Serializable

interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class MainChild(val component: MainComponent) : Child()
    }


    @Serializable
    sealed interface Config {
        @Serializable
        data object Main : Config
    }

    fun onBackClicked()
}