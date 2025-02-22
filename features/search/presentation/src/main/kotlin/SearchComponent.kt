import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateManager
import main.MainComponent
import tickers.TickersComponent

class SearchComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    val output: (Output) -> Unit,
    val tickersComponent: TickersComponent
) : ComponentContext by componentContext,
    DefaultMVIComponent<SearchStore.Intent, SearchStore.State, SearchStore.Label> {
    val networkStateManager = NetworkStateManager()
    private val factory = SearchStoreFactory(
        storeFactory = storeFactory,
        executor = SearchExecutor(
            networkStateManager = networkStateManager,
            tickersComponent = tickersComponent
        ),
        state = SearchStore.State()
    )
    override val store: SearchStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }
    sealed class Output() {
        data object NavigateBack : Output()
    }
    fun onOutput(output: Output) {
        output(output)
    }
}