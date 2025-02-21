package main

import NewsItem
import com.arkivanov.mvikotlin.core.store.Store
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State

interface MainStore : Store<Intent, State, Label> {
    data class State(
        val news: List<NewsItem> = listOf()
    )

    sealed interface Intent {
    }

    sealed interface Message {
        data class NewsGot(val news: List<NewsItem>) : Message
    }

    sealed interface Label


}
