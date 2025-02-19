package transactions

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import transactions.TransactionsStore.Intent
import transactions.TransactionsStore.Label
import transactions.TransactionsStore.State
import transactions.TransactionsStore.Message

class TransactionsStoreFactory(
    private val storeFactory: StoreFactory,
    val executor: TransactionsExecutor
) {

    fun create(): TransactionsStore {
        return TransactionsStoreImpl()
    }

    private inner class TransactionsStoreImpl :
        TransactionsStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "TransactionsStore",
            initialState = TransactionsStore.State(),
            executorFactory = ::executor,
            reducer = TransactionsReducer
        )
}