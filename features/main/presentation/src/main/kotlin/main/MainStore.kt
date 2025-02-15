package main

import com.arkivanov.mvikotlin.core.store.Store
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State
import utils.NewsItem

interface MainStore : Store<Intent, State, Label> {
    data class State(
        val news: List<NewsItem> = emptyList(),
        val isConverted: Boolean = false
    )

    sealed interface Intent {
        data object FetchRecentNews : Intent
    }

    sealed interface Message {
        data class NewsFetched(val news: List<NewsItem>) : Message
    }

    sealed interface Label
}
