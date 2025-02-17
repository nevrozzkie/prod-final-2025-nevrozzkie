package com.prodfinal2025.nevrozq.navigation.root

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.instancekeeper.retainedSimpleInstance
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Child
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Child.FinanceChild
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Child.MainChild
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Config
import finance.FinanceComponent
import main.MainComponent
import socialFeed.SocialFeedComponent
import kotlin.reflect.KClass

class RootComponentImpl(
    private val activity: ComponentActivity,
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
            Config.Main ->
                MainChild(
                    retainedInstance {
                        MainComponent(
                            childContext,
                            storeFactory,
                            instanceKeeper
                        )
                    }
                )


            Config.Finance -> FinanceChild(
                FinanceComponent(childContext, storeFactory, instanceKeeper)
            )

            Config.SocialFeed -> Child.SocialFeedChild(
                SocialFeedComponent(childContext, storeFactory, instanceKeeper)
            )
        }


    override fun onBackClicked() {
        popOnce(child = stack.active.instance::class)
    }

    private fun popOnce(child: KClass<out Child>) {
        val currentConfig = stack.active.configuration
        if (currentConfig is Config.Main)
            activity.finish()
        else
            nav.pop()

    }

    override fun onOutput(config: Config) {
        nav.pushToFront(config)
    }
}