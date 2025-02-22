package tickers

import MainRepository
import android.content.Context
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prodfinal2025.nevrozq.R
import decompose.DefaultCoroutineExecutor
import decompose.NetworkStateManager
import koin.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tickers.TickersStore.Intent
import tickers.TickersStore.Label
import tickers.TickersStore.State
import tickers.TickersStore.Message
import java.io.File
import java.io.FileNotFoundException
import kotlin.coroutines.cancellation.CancellationException

class TickersExecutor(
    private val mainRepository: MainRepository = Inject.instance(),
    private val networkStateManager: NetworkStateManager,
    private val androidContext: Context = Inject.instance()
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var fetchTickersJob: Job? = null
    override fun executeAction(action: Unit) {
        parseTickersIds()
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.FetchMainTickers -> fetchTickers(ids = state().mainTickersIds, true)
            is Intent.LoadTickers -> fetchTickers(ids = intent.ids, isMain = false)
        }
    }

    private fun parseTickersIds() {
        try {
            val inputStream = androidContext.resources.openRawResource(com.prodfinal2025.nevrozq.features.main.presentation.R.raw.symbols)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val symbols: List<String> =
                Json.decodeFromString(jsonString)
            dispatch(Message.MainTickersIdsFetched(symbols))

            fetchTickers(ids = symbols, true)
        } catch (e: Throwable) {
            println("Ошибка при десериализации JSON: ${e.message}")
        }
    }

    private fun fetchTickers(ids: List<String>, isMain: Boolean) {
        fetchTickersJob?.cancel()
        fetchTickersJob = scope.launch {
            try {
                networkStateManager.nStartLoading()
                val tickers = mainRepository.fetchTickers(
                    ids,
                    canBeErrors = !isMain
                )
                if (isMain) {
                    dispatch(Message.MainTickersFetched(tickers))
                } else {
                    dispatch(Message.SearchTickersFetched(tickers))
                }
                networkStateManager.nSuccess()
            } catch (_: CancellationException) {

            } catch (e: Throwable) {
                println("MMM: ${e.message}")
                networkStateManager.nError(
                    errorTitle = "Не удалось загрузить тикеры",
                    errorDesc = ""
                ) {
                    fetchTickers(ids, isMain)
                }
            }
        }
    }
}
