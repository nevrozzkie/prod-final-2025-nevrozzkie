package goals

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent

class GoalsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    executor: GoalsExecutor
) : ComponentContext by componentContext,
    DefaultMVIComponent<GoalsStore.Intent, GoalsStore.State, GoalsStore.Label> {
    private val factory = GoalsStoreFactory(
        storeFactory = storeFactory,
        executor = executor
    )
    override val store: GoalsStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    fun onDispatch(msg: GoalsStore.Message) {
        factory.executor.onDispatch(msg)
    }
}