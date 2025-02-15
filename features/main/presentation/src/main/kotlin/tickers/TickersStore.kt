package tickers

import com.arkivanov.mvikotlin.core.store.Store
import main.MainStore.Message
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State
import utils.Ticker

interface TickersStore : Store<Intent, State, Label> {
    data class State(
        val isConverted: Boolean = false,
        val mainTickers: List<Ticker> = emptyList(),
        val searchTickers: List<Ticker> = emptyList(),
    )

    sealed interface Intent {
        data object FetchMainTickers : Intent
    }

    sealed interface Message {
        data class MainTickersFetched(val tickers: List<Ticker>) : Message
        data class SearchTODOTickersFetched(val tickers: List<Ticker>) : Message

        data class IsConvertedChanged(val isConverted: Boolean) : Message
    }

    sealed interface Label

}
