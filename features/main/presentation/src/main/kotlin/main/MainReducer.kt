package main

import com.arkivanov.mvikotlin.core.store.Reducer
import main.MainStore.State
import main.MainStore.Message

object MainReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.NewsGot -> copy(news = msg.news)
        }
    }
}