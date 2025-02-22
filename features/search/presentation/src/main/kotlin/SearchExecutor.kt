import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import SearchStore.Intent
import SearchStore.Label
import SearchStore.State
import SearchStore.Message
import decompose.NetworkStateManager
import koin.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tickers.TickersComponent
import kotlin.coroutines.cancellation.CancellationException

class SearchExecutor(
    val networkStateManager: NetworkStateManager,
    val tickersComponent: TickersComponent,
    private val searchRepository: SearchRepository = Inject.instance()
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {


    private var searchJob: Job? = null

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.InputSearch -> performSearch(intent.query)
        }
    }

    private fun performSearch(query: String) {

        dispatch(Message.QueryChanged(query))
        searchJob?.cancel()
        if (query.isNotEmpty()) {
            searchJob = scope.launch {
                try {
                    networkStateManager.nStartLoading()
                    // ne xvatilo(
//                    bindOnNews(query)
                    val ids = searchRepository.findTickers(query).take(5)
                    tickersComponent.loadTickers(ids)
                    networkStateManager.nSuccess()
                } catch (_: CancellationException) {

                } catch (e: Throwable) {
                    networkStateManager.nError("Не удалось начать поиск", errorDesc = "") {
                        performSearch(query)
                    }
                }
            }
        }
    }

    private fun bindOnNews(query: String) {
        scope.launch {
            searchRepository.findNews(query).collectLatest {
                dispatch(Message.NewsUpdated(it))
            }
        }
    }
}
