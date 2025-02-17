package main

import MainRepository
import NewsItem
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateManager
import koin.Inject
import kotlinx.coroutines.flow.Flow
import tickers.TickersComponent
import tickers.TickersStore

class MainComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    override val instanceKeeper: InstanceKeeper
) : ComponentContext by componentContext,
    DefaultMVIComponent<MainStore.Intent, MainStore.State, MainStore.Label> {

    val networkStateManager = NetworkStateManager()

    override val store: MainStore
        get() = instanceKeeper.getStore {
            MainStoreFactory(
                storeFactory = storeFactory,
                executor = MainExecutor(
                    networkStateManager = networkStateManager
                )
            ).create()
        }

    val tickersComponent = TickersComponent(
        childContext("tickers"),
        storeFactory = storeFactory
    )

    init {
        tickersComponent.onEvent(TickersStore.Intent.FetchMainTickers)
        onEvent(MainStore.Intent.OnInit)
    }

}