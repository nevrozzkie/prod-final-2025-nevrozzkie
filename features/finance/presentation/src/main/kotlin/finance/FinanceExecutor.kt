package finance

import FinanceRepository
import Goal
import Transaction
import decompose.DefaultCoroutineExecutor
import finance.FinanceStore.Intent
import finance.FinanceStore.Label
import finance.FinanceStore.State
import finance.FinanceStore.Message
import koin.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class FinanceExecutor(
    private val transactionsFlow: Flow<List<Transaction>>,
    private val goalsFlow: Flow<List<Goal>>,
    private val financeRepository: FinanceRepository = Inject.instance()
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {


    override fun executeAction(action: Unit) {
        bindGoals()
        bindTransactions()
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.CompleteGoal -> {
                scope.launch {
                    val transactionMaxId = (transactionsFlow.firstOrNull() ?: listOf()).maxOfOrNull { it.id } ?: 0L
                    financeRepository.completeGoal(intent.goal, transactionMaxId)
                }
            }
        }
    }

    private fun bindGoals() {
        scope.launch {
            goalsFlow.collectLatest { goals ->
                val activeGoals = goals.filter { it.completedDate == null }
                dispatch(Message.GoalsGot(
                    active = activeGoals,
                    completed = (goals - activeGoals.toSet()),
                    totalNeededAmount = activeGoals.sumOf { it.targetAmount },
                    totalSavedAmount = activeGoals.sumOf { it.savedAmount }
                ))
            }
        }
    }

    private fun bindTransactions() {
        scope.launch {
            transactionsFlow.collectLatest { transactions ->
                dispatch(Message.TransactionsGot(transactions))
            }
        }
    }
}
