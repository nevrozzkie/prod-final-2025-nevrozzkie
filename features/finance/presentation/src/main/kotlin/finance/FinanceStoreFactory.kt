package finance

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import finance.FinanceStore.Intent
import finance.FinanceStore.Label
import finance.FinanceStore.State
import finance.FinanceStore.Message

class FinanceStoreFactory(
    private val storeFactory: StoreFactory,
    val executor: FinanceExecutor
) {

    fun create(): FinanceStore {
        return FinanceStoreImpl()
    }

    private inner class FinanceStoreImpl :
        FinanceStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "FinanceStore",
            initialState = State(),
            executorFactory = ::executor,
            reducer = FinanceReducer,
            bootstrapper = SimpleBootstrapper(Unit)
        )
}