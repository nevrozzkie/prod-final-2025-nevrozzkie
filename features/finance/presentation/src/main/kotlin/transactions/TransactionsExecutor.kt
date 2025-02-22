package transactions

import FinanceRepository
import Goal
import Transaction
import decompose.DefaultCoroutineExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.format
import transactions.TransactionsStore.Intent
import transactions.TransactionsStore.Label
import transactions.TransactionsStore.Message
import transactions.TransactionsStore.State
import utils.getCurrentLocalDateTime
import utils.rusFormat

class TransactionsExecutor(
    private val financeRepository: FinanceRepository,
    private val goalsFlow: Flow<List<Goal>>,
    private val transactionsFlow: Flow<List<Transaction>>,
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeAction(action: Unit) {
        updateMaxId()
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeEditTransactionAmount -> editGoalAmount(intent.amount)
            is Intent.ChangeEditTransactionComment -> {
                dispatch(Message.EditingTransactionChanged(state().editingTransaction?.copy(comment = intent.comment)))
            }

            is Intent.ChangeEditTransactionFromId -> changeTransactionId(
                isItFrom = true,
                intent.fromId
            )

            is Intent.ChangeEditTransactionToId -> changeTransactionId(
                isItFrom = false,
                intent.toId
            )

            is Intent.StartCreatingOrEditing -> intent.startCreatingOrEditing()
            Intent.PerformCreateOrEdit -> createOrEdit()
            is Intent.Repeat -> repeat(intent.transaction)
            Intent.CancelCreatingOrEditing -> dispatch(Message.EditingTransactionChanged(null))
            is Intent.Delete -> delete(intent.transaction)
        }
    }

    private fun updateMaxId() {
        scope.launch {
            transactionsFlow.collectLatest { goals ->
                dispatch(Message.MaxIdChanged(goals.maxOfOrNull { it.id } ?: 0L))
            }
        }
    }


    private fun delete(transaction: Transaction) {
        scope.launch {
            financeRepository.deleteTransaction(transaction)
        }
    }

    private fun repeat(transaction: Transaction) {
        scope.launch {
            dispatch(
                Message.EditingTransactionChanged(
                    Transaction(
                        id = state().maxId + 1,
                        fromGoal = transaction.fromGoal,
                        fromGoalId = transaction.fromGoalId,
                        toGoalId = transaction.toGoalId,
                        toGoal = transaction.toGoal,
                        amount = transaction.amount,
                        comment = transaction.comment,
                        createdDate = transaction.createdDate,
                        isEditing = true
                    )
                )
            )
//
//            financeRepository.upsertTransaction(
//                transaction.copy(
//                    id = state().maxId + 1,
//                    createdDate = getCurrentLocalDateTime().date.format(rusFormat)
//                )
//            )
        }
    }

    private fun createOrEdit() {

        scope.launch {
            state().editingTransaction?.let { transaction ->
                if (transaction.toGoalId != null && transaction.amount.toLongOrNull() != null) {
                    dispatch(Message.EditingTransactionChanged(null))
                    financeRepository.upsertTransaction(
                        transaction.copy(
                            // if fromGoalId != null we add two transactions so +1 to id
                            id = if (transaction.fromGoalId != null && transaction.id > state().maxId) transaction.id + 1 else transaction.id
                        )
                    )
                }
            }
        }
    }

    private fun changeTransactionId(isItFrom: Boolean, id: Long?) {
        scope.launch {
            val newTransaction = if (id == null) {
                if (isItFrom) state().editingTransaction?.copy(
                    fromGoal = "",
                    fromGoalId = null
                ) else state().editingTransaction?.copy(
                    toGoal = "",
                    toGoalId = null
                )
            } else {
                val goals = goalsFlow.firstOrNull() ?: emptyList()
                val chosenGoal = goals.firstOrNull { it.id == id }
                if (chosenGoal != null) {
                    if (isItFrom) state().editingTransaction?.copy(
                        fromGoal = chosenGoal.name,
                        fromGoalId = chosenGoal.id
                    ) else state().editingTransaction?.copy(
                        toGoal = chosenGoal.name,
                        toGoalId = chosenGoal.id
                    )

                } else null
            }
            newTransaction?.let {
                dispatch(
                    Message.EditingTransactionChanged(
                        newTransaction
                    )
                )
            }
        }
    }

    private fun Intent.StartCreatingOrEditing.startCreatingOrEditing() {
        dispatch(
            Message.EditingTransactionChanged(
                Transaction(
                    id = this.id,
                    fromGoal = this.fromGoal,
                    toGoal = this.toGoal,
                    amount = this.amount,
                    comment = this.comment,
                    createdDate = this.createdDate ?: getCurrentLocalDateTime().date.format(
                        rusFormat
                    ),
                    isEditing = true,
                    toGoalId = this.toGoalId,
                    fromGoalId = this.fromGoalId
                )
            )
        )
    }

    private fun editGoalAmount(notFormattedText: String) {
        val regex = Regex("^-?\\d{0,12}$")
        if (notFormattedText.matches(regex) || notFormattedText.isEmpty()) {
            dispatch(
                Message.EditingTransactionChanged(
                    state().editingTransaction?.copy(amount = notFormattedText)
                )
            )
        }
    }
}
