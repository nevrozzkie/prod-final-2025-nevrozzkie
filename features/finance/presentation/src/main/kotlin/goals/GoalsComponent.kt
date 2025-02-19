package goals

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import finance.FinanceExecutor
import finance.FinanceStore
import finance.FinanceStoreFactory

class GoalsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : ComponentContext by componentContext,
    DefaultMVIComponent<GoalsStore.Intent, GoalsStore.State, GoalsStore.Label> {
    private val factory = GoalsStoreFactory(
        storeFactory = storeFactory,
        executor = GoalsExecutor()
    )
    override val store: GoalsStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    val flowers = GoalsStore.Flowers(
        activeGoals = factory.executor.financeRepository.getActiveGoalsFlow(),
        completedGoals = factory.executor.financeRepository.getCompletedGoalsFlow()
    )

    fun onDispatch(msg: GoalsStore.Message) {
        factory.executor.onDispatch(msg)
    }
}