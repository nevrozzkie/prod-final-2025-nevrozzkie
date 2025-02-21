package newYorkTimes

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import newYorkTimes.NewYorkTimesStore.Intent
import newYorkTimes.NewYorkTimesStore.Label
import newYorkTimes.NewYorkTimesStore.State

class NewYorkTimesStoreFactory(
    private val storeFactory: StoreFactory,
    private val executor: NewYorkTimesExecutor
) {

    fun create(): NewYorkTimesStore {
        return NewYorkTimesStoreImpl()
    }

    private inner class NewYorkTimesStoreImpl :
        NewYorkTimesStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "NewYorkTimesStore",
            initialState = State(),
            executorFactory = ::executor,
            reducer = NewYorkTimesReducer,
            bootstrapper = SimpleBootstrapper(Unit)
        )
}