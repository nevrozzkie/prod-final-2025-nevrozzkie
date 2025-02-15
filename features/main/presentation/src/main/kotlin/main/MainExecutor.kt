package main

import MainRepository
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.NetworkStateManager
import koin.Inject
import kotlinx.coroutines.launch
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State
import main.MainStore.Message
import tickers.TickersDefaults
import tickers.TickersStore

class MainExecutor(
    private val mainRepository: MainRepository = Inject.instance(),
    private val networkStateManager: NetworkStateManager
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.FetchRecentNews -> fetchRecentNews()
        }
    }

    private fun fetchRecentNews() {
        scope.launch {
            try {
                networkStateManager.nStartLoading()
                val r = mainRepository.fetchRecentNews()
                dispatch(Message.NewsFetched(r))
                networkStateManager.nSuccess()
            } catch (e: Throwable) {
                networkStateManager.nError(
                    errorTitle = "Не удалось обновить новости",
                    errorDesc = ""
                ) {
                    fetchRecentNews()
                }
            }
        }
    }
}
