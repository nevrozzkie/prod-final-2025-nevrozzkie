package transactions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import goals.GoalsExecutor
import goals.GoalsStore
import goals.GoalsStoreFactory

class TransactionsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : ComponentContext by componentContext,
    DefaultMVIComponent<TransactionsStore.Intent, TransactionsStore.State, TransactionsStore.Label> {
    private val factory = TransactionsStoreFactory(
        storeFactory = storeFactory,
        executor = TransactionsExecutor()
    )
    override val store: TransactionsStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    val flows = TransactionsStore.Flows()

    fun onDispatch(msg: TransactionsStore.Message) {
        factory.executor.onDispatch(msg)
    }
}