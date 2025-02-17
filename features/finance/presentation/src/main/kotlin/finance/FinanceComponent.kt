package finance

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateManager

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

}