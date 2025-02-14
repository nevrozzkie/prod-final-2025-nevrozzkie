package tickers

import MainRepository
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
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
                val r = mainRepository.fetchTickers(
                    TickersDefaults.mainTickers.map {
                        it
                    }
                )
                dispatch(Message.MainTickersUpdated(r))
                networkStateManager.nSuccess()
            } catch (e: Throwable) {
                println("MMM: ${e.message}")
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
