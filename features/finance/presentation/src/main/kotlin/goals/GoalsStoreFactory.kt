package goals

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import finance.FinanceExecutor
import goals.GoalsStore.Intent
import goals.GoalsStore.Label
import goals.GoalsStore.State
import goals.GoalsStore.Message

class GoalsStoreFactory(
    private val storeFactory: StoreFactory,
    val executor: GoalsExecutor
) {

    fun create(): GoalsStore {
        return GoalsStoreImpl()
    }

    private inner class GoalsStoreImpl :
        GoalsStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "GoalsStore",
            initialState = State(),
            executorFactory = ::executor,
            reducer = GoalsReducer,
            bootstrapper = SimpleBootstrapper(Unit)
        )
}