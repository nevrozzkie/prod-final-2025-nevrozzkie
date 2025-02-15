package finance

import com.arkivanov.mvikotlin.core.store.Store
import finance.FinanceStore.Intent
import finance.FinanceStore.Label
import finance.FinanceStore.State

interface FinanceStore : Store<Intent, State, Label> {
    object State

    sealed interface Intent

    sealed interface Message

    sealed interface Label

}
