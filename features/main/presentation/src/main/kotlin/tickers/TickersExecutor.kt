package tickers

import MainRepository
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.DefaultCoroutineExecutor
import decompose.NetworkStateManager
import koin.Inject
import kotlinx.coroutines.launch
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State
import tickers.TickersStore.Message

class TickersExecutor(
    private val mainRepository: MainRepository = Inject.instance(),
    private val networkStateManager: NetworkStateManager
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.FetchMainTickers -> fetchMainTickers()
        }
    }

    private fun fetchMainTickers() {
        scope.launch {
            try {
                networkStateManager.nStartLoading()
                val r = mainRepository.fetchTickers(
                    TickersDefaults.mainTickers.map {
                        it
                    }
                )
                dispatch(Message.MainTickersFetched(r))
                networkStateManager.nSuccess()
            } catch (e: Throwable) {
                println("MMM: ${e.message}")
                networkStateManager.nError(
                    errorTitle = "Не удалось загрузить тикеры",
                    errorDesc = ""
                ) {
                    fetchMainTickers()
                }
            }
        }
    }
}
