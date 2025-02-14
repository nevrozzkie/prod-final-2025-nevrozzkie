package com.prodfinal2025.nevrozq.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prodfinal2025.nevrozq.navigation.RootComponent.Child
import com.prodfinal2025.nevrozq.navigation.RootComponent.Child.MainChild
import com.prodfinal2025.nevrozq.navigation.RootComponent.Config
import main.MainComponent

class RootComponentImpl(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : RootComponent, ComponentContext by componentContext {
    private val nav = StackNavigation<Config>()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main,
        childFactory = ::child,
    )

    override val stack = _stack

    private fun child(config: Config, childContext: ComponentContext): Child =
        when (config) {
            Config.Main -> MainChild(
                MainComponent(
                    childContext,
                    storeFactory = storeFactory
                )
            )
        }


    override fun onBackClicked() {
        nav.pop()
    }
}