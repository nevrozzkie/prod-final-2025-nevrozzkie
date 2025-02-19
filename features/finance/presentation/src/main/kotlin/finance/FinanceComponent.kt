package finance

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateManager
import goals.GoalsComponent
import transactions.TransactionsComponent

class FinanceComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    override val instanceKeeper: InstanceKeeper
) : ComponentContext by componentContext,
    DefaultMVIComponent<FinanceStore.Intent, FinanceStore.State, FinanceStore.Label> {
    private val factory = FinanceStoreFactory(
        storeFactory = storeFactory,
        executor = FinanceExecutor()
    )
    override val store: FinanceStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    val goalsComponent = GoalsComponent(
        componentContext = childContext("goals"),
        storeFactory = storeFactory
    )
    val transactionsComponent = TransactionsComponent(
        componentContext = childContext("transactions"),
        storeFactory = storeFactory
    )

}