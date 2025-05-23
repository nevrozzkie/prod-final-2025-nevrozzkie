package com.prodfinal2025.nevrozq.navigation.root

import ManagePostDTO
import SearchComponent
import SocialComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import finance.FinanceComponent
import main.MainComponent
import kotlinx.serialization.Serializable
import newYorkTimes.NewYorkTimesComponent
import tickers.TickersComponent
import kotlin.reflect.KClass

interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class MainChild(val component: MainComponent) : Child()
        data class NewYorkTimesChild(val component: NewYorkTimesComponent) : Child()
        data class FinanceChild(val component: FinanceComponent) : Child()
        data class SocialChild(val component: SocialComponent) : Child()
        data class SearchChild(val component: SearchComponent) : Child()
    }


    @Serializable
    sealed interface Config {
        data object Main : Config
//        @Serializable
        data class Search(val tickersComponent: TickersComponent, val mainComponent: MainComponent) : Config
        @Serializable
        data object Finance : Config
        @Serializable
        data class Social(val post: ManagePostDTO?) : Config
        @Serializable
        data class NewYorkTimes(
            val initialUrl: String,
            val id: String,
            val icon: ByteArray?,
            val title: String,
        ) : Config
    }

    fun onBackClicked()

    fun onOutput(child: KClass<out Child>, config: Config)
}