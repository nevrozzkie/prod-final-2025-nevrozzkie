package finance

import com.arkivanov.mvikotlin.core.store.Reducer
import finance.FinanceStore.State
import finance.FinanceStore.Message

object FinanceReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}