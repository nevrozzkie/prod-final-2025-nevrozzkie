package tickers

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State

class TickersStoreFactory(
    private val storeFactory: StoreFactory,
    val executor: TickersExecutor
) {

    fun create(): TickersStore {
        return TickersStoreImpl()
    }

    private inner class TickersStoreImpl :
        TickersStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "TickersStore",
            initialState = State(),
            executorFactory = ::executor,
            reducer = TickersReducer,
            bootstrapper = SimpleBootstrapper(Unit)
        )
}