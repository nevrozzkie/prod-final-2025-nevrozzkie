package tickers

import com.arkivanov.mvikotlin.core.store.Store
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State
import Ticker

interface TickersStore : Store<Intent, State, Label> {
    data class State(
        val isConverted: Boolean = false,
        val mainTickersIds: List<String> = emptyList(),
        val mainTickers: List<Ticker> = emptyList(),
        val searchTickers: List<Ticker> = emptyList(),
    )

    sealed interface Intent {
        data object FetchMainTickers : Intent
        data class LoadTickers(val ids: List<String>) : Intent
    }

    sealed interface Message {
        data class MainTickersFetched(val tickers: List<Ticker>) : Message
        data class MainTickersIdsFetched(val ids: List<String>) : Message
        data class SearchTickersFetched(val tickers: List<Ticker>) : Message
        data class IsConvertedChanged(val isConverted: Boolean) : Message
    }

    sealed interface Label

}
