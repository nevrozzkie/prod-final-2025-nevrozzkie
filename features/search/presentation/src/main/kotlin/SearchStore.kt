import com.arkivanov.mvikotlin.core.store.Store
import SearchStore.Intent
import SearchStore.Label
import SearchStore.State

interface SearchStore : Store<Intent, State, Label> {
    data class State(
        val query: String = "",
        val news: List<NewsItem> = emptyList()
    )

    sealed interface Intent {
        data class InputSearch(val query: String) : Intent
    }

    sealed interface Message {
        data class QueryChanged(val query: String) : Message
        data class NewsUpdated(val news: List<NewsItem>) : Message
    }

    sealed interface Label

}
