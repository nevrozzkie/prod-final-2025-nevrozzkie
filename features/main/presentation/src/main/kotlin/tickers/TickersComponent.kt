package tickers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateManager

object TickersDefaults {
    val mainTickers = listOf("AAPL", "MSFT", "AMZN")
}

class TickersComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory
) : ComponentContext by componentContext,
    DefaultMVIComponent<TickersStore.Intent, TickersStore.State, TickersStore.Label> {
    val networkStateManager = NetworkStateManager()
    override val store
        get() = instanceKeeper.getStore() {
            TickersStoreFactory(
                storeFactory = storeFactory,
                executor = TickersExecutor(
                    networkStateManager = networkStateManager
                )
            ).create()
        }
}
