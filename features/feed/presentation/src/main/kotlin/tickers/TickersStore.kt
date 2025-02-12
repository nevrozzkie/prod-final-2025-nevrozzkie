package tickers

import com.arkivanov.mvikotlin.core.store.Store
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State
import utils.Ticker

interface TickersStore : Store<Intent, State, Label> {
    data class State(
        val mainTickers: List<Ticker> = emptyList(),
        val searchTickers: List<Ticker> = emptyList(),
    )

    sealed interface Intent {
        data object LoadMainTickers : Intent
    }

    sealed interface Message {
        data class MainTickersUpdated(val tickers: List<Ticker>) : Message
        data class SearchTickersUpdated(val tickers: List<Ticker>) : Message
    }

    sealed interface Label

}
