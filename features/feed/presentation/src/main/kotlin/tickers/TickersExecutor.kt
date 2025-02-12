package tickers

import FeedRepository
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.NetworkStateManager
import koin.Inject
import kotlinx.coroutines.launch
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State
import tickers.TickersStore.Message
import utils.RFetchTickerRequest

class TickersExecutor(
    private val networkStateManager: NetworkStateManager,
    val feedRepository: FeedRepository = Inject.instance()
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.LoadMainTickers -> loadMainTickers()
        }
    }

    private fun loadMainTickers() {
        scope.launch {
            try {
                networkStateManager.nStartLoading()
                val r = feedRepository.fetchTickers(
                    listOf("APPL").map {
                        RFetchTickerRequest(it)
                    }
                )
                dispatch(Message.MainTickersUpdated(r))
                networkStateManager.nSuccess()
            } catch (e: Throwable) {
                networkStateManager.nError(
                    errorTitle = "Не удалось загрузить тикеры",
                    errorDesc = ""
                ) {
                    loadMainTickers()
                }
            }
        }
    }
}
