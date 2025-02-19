package transactions

import FinanceRepository
import Goal
import Transaction
import com.arkivanov.mvikotlin.core.store.Store
import goals.GoalsStore.Message
import koin.Inject
import kotlinx.coroutines.flow.Flow
import transactions.TransactionsStore.Intent
import transactions.TransactionsStore.Label
import transactions.TransactionsStore.State

interface TransactionsStore : Store<Intent, State, Label> {
    data class State(

        val editingTransaction: Transaction? = null
    )

    data class Flows(
        val transactions: Flow<List<Transaction>> = Inject.instance<FinanceRepository>()
            .getTransactionsFlow(
                listOf()
            ),


    )

    sealed interface Intent {
        data class ChangeEditTransactionFromId(val fromId: Long) : Intent
        data class ChangeEditTransactionToId(val toId: Long) : Intent
        data class ChangeEditTransactionAmount(val amount: Int) : Intent
        data class ChangeEditTransactionComment(val comment: String) : Intent
    }

    sealed interface Message {
        data class EditingTransactionChanged(
            val transaction: Transaction?
        ) : Message
    }

    sealed interface Label

}
