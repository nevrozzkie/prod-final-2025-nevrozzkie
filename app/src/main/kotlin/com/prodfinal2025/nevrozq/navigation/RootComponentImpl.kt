package com.prodfinal2025.nevrozq.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.prodfinal2025.nevrozq.navigation.RootComponent.Child
import com.prodfinal2025.nevrozq.navigation.RootComponent.Child.FeedChild
import com.prodfinal2025.nevrozq.navigation.RootComponent.Config

class RootComponentImpl(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val nav = StackNavigation<Config>()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.Feed,
        childFactory = ::child,
    )

    override val stack = _stack

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Config.Feed -> FeedChild
        }


    override fun onBackClicked() {
        nav.pop()
    }


}