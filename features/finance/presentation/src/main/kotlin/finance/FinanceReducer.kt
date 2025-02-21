package finance

import com.arkivanov.mvikotlin.core.store.Reducer
import finance.FinanceStore.State
import finance.FinanceStore.Message

object FinanceReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.GoalsGot -> copy(
                activeGoals = msg.active,
                completedGoals = msg.completed,
                totalNeededAmount = msg.totalNeededAmount,
                totalSavedAmount = msg.totalSavedAmount
            )

            is Message.TransactionsGot -> copy(
                transactions = msg.transactions,
            )
        }
    }
}