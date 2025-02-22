package tickers

import com.arkivanov.mvikotlin.core.store.Reducer
import tickers.TickersStore.State
import tickers.TickersStore.Message

object TickersReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.MainTickersFetched -> copy(mainTickers = msg.tickers)
            is Message.SearchTickersFetched -> copy(searchTickers = msg.tickers)
            is Message.IsConvertedChanged -> copy(isConverted = msg.isConverted)
            is Message.MainTickersIdsFetched -> copy(mainTickersIds = msg.ids)
        }
    }
}