package main

import MainRepository
import NewsItem
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.store.Store
import koin.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.Serializable
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State

interface MainStore : Store<Intent, State, Label> {
    data class State(
        val newsFlow: Flow<List<NewsItem>> = Inject.instance<MainRepository>().getNewsFlow()
    )

    sealed interface Intent {
        data object OnInit : Intent
    }

    sealed interface Message {
//        data class NewsFetched(val news: List<NewsItem>) : Message
    }

    sealed interface Label
}
