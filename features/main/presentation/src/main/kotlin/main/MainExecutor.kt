package main

import MainRepository
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.NetworkStateManager
import koin.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State
import main.MainStore.Message

class MainExecutor(
    private val mainRepository: MainRepository = Inject.instance(),
    private val networkStateManager: NetworkStateManager
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeAction(action: Unit) {
        getNewsFromDb()
        fetchRecentNews(false)
    }
    override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.Refresh -> fetchRecentNews(true)
            }
    }

    private fun getNewsFromDb() {
        scope.launch {
            mainRepository.getNewsFlow().collectLatest { news ->
                dispatch(Message.NewsGot(news = news))
            }
        }
    }

    private fun fetchRecentNews(mustBeFromInternet: Boolean) {
        scope.launch {
            try {
                networkStateManager.nStartLoading()
                mainRepository.fetchRecentNews(mustBeFromInternet)
//                dispatch(Message.NewsGot(news))
                networkStateManager.nSuccess()
            } catch (e: Throwable) {
                networkStateManager.nError(
                    errorTitle = "Не удалось обновить новости",
                    errorDesc = ""
                ) {
                    fetchRecentNews(mustBeFromInternet)
                }
            }
        }
    }
}
