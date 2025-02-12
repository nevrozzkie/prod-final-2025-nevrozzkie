package tickers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateOwner

class TickersComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory
) : ComponentContext by componentContext,
    DefaultMVIComponent<TickersStore.Intent, TickersStore.State, TickersStore.Label>,
    NetworkStateOwner {

    override val store
        get() = instanceKeeper.getStore() {
            TickersStoreFactory(
                storeFactory = storeFactory,
                networkStateManager = networkStateManager
            ).create()
        }
}
