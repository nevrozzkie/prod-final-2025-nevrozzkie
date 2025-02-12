package tickers

import FeedRepository
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.NetworkStateManager
import koin.Inject
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State
import tickers.TickersStore.Message

class TickersStoreFactory(
    private val storeFactory: StoreFactory,
    private val networkStateManager: NetworkStateManager
) {

    fun create(): TickersStore {
        return TickersStoreImpl()
    }

    private inner class TickersStoreImpl :
        TickersStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "TickersStore",
            initialState = TickersStore.State(),
            executorFactory = { TickersExecutor(
                networkStateManager = networkStateManager
            ) },
            reducer = TickersReducer
        )
}