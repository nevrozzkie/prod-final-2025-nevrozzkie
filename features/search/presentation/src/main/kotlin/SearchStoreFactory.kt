import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import SearchStore.Intent
import SearchStore.Label
import SearchStore.State
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper

class SearchStoreFactory(
    private val storeFactory: StoreFactory,
    private val state: State,
    val executor: SearchExecutor
) {

    fun create(): SearchStore {
        return SearchStoreImpl()
    }

    private inner class SearchStoreImpl :
        SearchStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = state,
            executorFactory = { executor },
            reducer = SearchReducer
        )
}