package finance

import Goal
import com.arkivanov.mvikotlin.core.store.Store
import finance.FinanceStore.Intent
import finance.FinanceStore.Label
import finance.FinanceStore.State
import kotlinx.coroutines.flow.Flow

interface FinanceStore : Store<Intent, State, Label> {
    data object State

    sealed interface Intent

    sealed interface Message

    sealed interface Label

}
