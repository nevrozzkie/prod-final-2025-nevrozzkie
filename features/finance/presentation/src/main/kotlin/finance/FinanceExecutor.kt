package finance

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.DefaultCoroutineExecutor
import finance.FinanceStore.Intent
import finance.FinanceStore.Label
import finance.FinanceStore.State
import finance.FinanceStore.Message

class FinanceExecutor : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            else -> TODO()
        }
    }
}
