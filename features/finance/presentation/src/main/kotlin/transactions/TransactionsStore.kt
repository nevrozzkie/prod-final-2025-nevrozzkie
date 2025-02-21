package transactions

import FinanceRepository
import Goal
import Transaction
import com.arkivanov.mvikotlin.core.store.Store
import goals.GoalsStore
import goals.GoalsStore.Message
import koin.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.LocalDate
import org.koin.core.module.flatten
import transactions.TransactionsStore.Intent
import transactions.TransactionsStore.Label
import transactions.TransactionsStore.State

interface TransactionsStore : Store<Intent, State, Label> {
    data class State(
        val editingTransaction: Transaction? = null,
        val isAmountValid: Boolean = false,
        val maxId: Long = 0L
    )

    sealed interface Intent {

        data class StartCreatingOrEditing(
            val id: Long,
            val fromGoalId: Long?,
            val toGoalId: Long?,
            val fromGoal: String,
            val toGoal: String,
            val amount: String,
            val comment: String,
            val createdDate: String?
        ) : Intent

        data object CancelCreatingOrEditing : Intent

        data object PerformCreateOrEdit : Intent



        data class Repeat(val transaction: Transaction) : Intent
        data class Delete(val transaction: Transaction) : Intent


        data class ChangeEditTransactionFromId(val fromId: Long?) : Intent
        data class ChangeEditTransactionToId(val toId: Long?) : Intent
        data class ChangeEditTransactionAmount(val amount: String) : Intent
        data class ChangeEditTransactionComment(val comment: String) : Intent
    }

    sealed interface Message {
        data class EditingTransactionChanged(
            val transaction: Transaction?
        ) : Message
        data class MaxIdChanged(
            val maxId: Long
        ) : Message
    }

    sealed interface Label

}
