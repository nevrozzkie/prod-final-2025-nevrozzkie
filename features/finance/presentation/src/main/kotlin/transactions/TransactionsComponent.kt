package transactions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent

class TransactionsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    val executor: TransactionsExecutor
) : ComponentContext by componentContext,
    DefaultMVIComponent<TransactionsStore.Intent, TransactionsStore.State, TransactionsStore.Label> {
    private val factory = TransactionsStoreFactory(
        storeFactory = storeFactory,
        executor = executor
    )
    override val store: TransactionsStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    fun onDispatch(msg: TransactionsStore.Message) {
        executor.onDispatch(msg)
    }
}