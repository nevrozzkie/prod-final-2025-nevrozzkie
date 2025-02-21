package finance

import Goal
import Transaction
import decompose.DefaultCoroutineExecutor
import finance.FinanceStore.Intent
import finance.FinanceStore.Label
import finance.FinanceStore.State
import finance.FinanceStore.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FinanceExecutor(
    private val transactionsFlow: Flow<List<Transaction>>,
    private val goalsFlow: Flow<List<Goal>>,
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {


    override fun executeAction(action: Unit) {
        bindGoals()
        bindTransactions()
    }

    override fun executeIntent(intent: Intent) {

    }

    private fun bindGoals() {
        scope.launch {
            goalsFlow.collectLatest { goals ->
                val activeGoals = goals.filter { it.completedDate == null }
                dispatch(Message.GoalsGot(
                    active= activeGoals,
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
