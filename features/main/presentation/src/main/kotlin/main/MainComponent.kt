package main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import tickers.TickersComponent
import tickers.TickersStore

class MainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : ComponentContext by componentContext {
    val tickersComponent = TickersComponent(
        childContext("tickers"),
        storeFactory = storeFactory
    )

    init {
        tickersComponent.onEvent(TickersStore.Intent.LoadMainTickers)
    }
}