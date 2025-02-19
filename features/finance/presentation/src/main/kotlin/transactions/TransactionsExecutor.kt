package transactions

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.DefaultCoroutineExecutor
import goals.GoalsStore
import transactions.TransactionsStore.Intent
import transactions.TransactionsStore.Label
import transactions.TransactionsStore.State
import transactions.TransactionsStore.Message

class TransactionsExecutor : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeEditTransactionAmount -> TODO()
            is Intent.ChangeEditTransactionComment -> TODO()
            is Intent.ChangeEditTransactionFromId -> TODO()
            is Intent.ChangeEditTransactionToId -> TODO()
        }
    }

    private fun editGoalAmount(notFormattedText: String) {
        val amountText = notFormattedText.replace(Regex("\\D"), "")
        if (amountText.length <= 999_999_999_999.toString().length) {
            val amount = if (amountText.isEmpty()) {
                0
            } else {
                amountText.toLongOrNull()
            }
            amount?.let {
                if (amount >= 0) {
                    dispatch(
                        Message.EditingTransactionChanged(
                            state().editingTransaction?.copy(amount = amount)
                        )
                    )
                }
            }
        }
    }
}
