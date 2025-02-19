package transactions

import com.arkivanov.mvikotlin.core.store.Reducer
import transactions.TransactionsStore.State
import transactions.TransactionsStore.Message

object TransactionsReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.EditingTransactionChanged -> copy(editingTransaction = msg.transaction)
        }
    }
}