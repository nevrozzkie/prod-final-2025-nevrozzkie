package finance

import FinanceRepository
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.asValue
import goals.GoalsComponent
import goals.GoalsExecutor
import koin.Inject
import transactions.TransactionsComponent
import transactions.TransactionsExecutor

class FinanceComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    override val instanceKeeper: InstanceKeeper
) : ComponentContext by componentContext,
    DefaultMVIComponent<FinanceStore.Intent, FinanceStore.State, FinanceStore.Label> {

    private val financeRepository: FinanceRepository = Inject.instance()

    private val goalsFlow = financeRepository.getGoalsFlow()
    private val transactionsFlow = financeRepository.getTransactionsFlow()

    private val factory = FinanceStoreFactory(
        storeFactory = storeFactory,
        executor = FinanceExecutor(
            goalsFlow = goalsFlow,
            transactionsFlow = transactionsFlow
        )
    )


    override val store: FinanceStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    val goalsComponent = GoalsComponent(
        componentContext = childContext("goals"),
        storeFactory = storeFactory,
        executor = GoalsExecutor(
            financeRepository = financeRepository,
            goalsFlow = goalsFlow
        )
    )
    val transactionsComponent = TransactionsComponent(
        componentContext = childContext("transactions"),
        storeFactory = storeFactory,
        executor = TransactionsExecutor(
            financeRepository = financeRepository,
            goalsFlow = goalsFlow,
            transactionsFlow = transactionsFlow
        )
    )

}