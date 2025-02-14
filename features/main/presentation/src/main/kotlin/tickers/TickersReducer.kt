package tickers

import com.arkivanov.mvikotlin.core.store.Reducer
import tickers.TickersStore.State
import tickers.TickersStore.Message

object TickersReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.MainTickersUpdated -> copy(mainTickers = msg.tickers)
            is Message.SearchTickersUpdated -> copy(searchTickers = msg.tickers)
        }
    }
}