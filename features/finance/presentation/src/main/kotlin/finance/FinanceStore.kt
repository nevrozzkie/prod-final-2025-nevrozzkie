package finance

import Goal
import Transaction
import com.arkivanov.mvikotlin.core.store.Store
import finance.FinanceStore.Intent
import finance.FinanceStore.Label
import finance.FinanceStore.State

interface FinanceStore : Store<Intent, State, Label> {
    data class State(
        val transactions: List<Transaction> = emptyList(),
        val activeGoals: List<Goal> = emptyList(),
        val completedGoals: List<Goal> = emptyList(),

        val totalSavedAmount: Long = 0L,
        val totalNeededAmount: Long = 0L,
    )

    sealed interface Intent {
        data class CompleteGoal(val goal: Goal) : Intent
    }

    sealed interface Message {
        data class GoalsGot(val active: List<Goal>, val completed: List<Goal>, val totalNeededAmount: Long, val totalSavedAmount: Long) : Message
        data class TransactionsGot(val transactions: List<Transaction>) : Message
    }

    sealed interface Label

}
