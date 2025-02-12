package com.prodfinal2025.nevrozq.navigation

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import kotlinx.serialization.Serializable

interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data object FeedChild : Child()
    }


    @Serializable
    sealed interface Config {
        data object Feed : Config
    }

    fun onBackClicked()
}