package tickers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateManager
import decompose.asValue
import main.MainReducer.reduce
import main.MainStore
import tickers.TickersReducer.reduce

object TickersDefaults {
    val mainTickers = listOf("AAPL", "MSFT", "AMZN")
}

class TickersComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory
) : ComponentContext by componentContext,
    DefaultMVIComponent<TickersStore.Intent, TickersStore.State, TickersStore.Label> {
    val networkStateManager = NetworkStateManager()
    private val factory = TickersStoreFactory(
        storeFactory = storeFactory,
        executor = TickersExecutor(
            networkStateManager = networkStateManager
        )
    )
    override val store: TickersStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    fun onDispatch(message: TickersStore.Message) {
        factory.executor.onDispatch(message)
    }
}
