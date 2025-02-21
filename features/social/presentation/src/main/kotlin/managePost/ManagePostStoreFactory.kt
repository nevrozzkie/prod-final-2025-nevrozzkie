package managePost

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import managePost.ManagePostStore.Intent
import managePost.ManagePostStore.Label
import managePost.ManagePostStore.State
import managePost.ManagePostStore.Message

class ManagePostStoreFactory(
    private val storeFactory: StoreFactory,
    val executor: ManagePostExecutor,
    private val state: State
) {

    fun create(): ManagePostStore {
        return ManagePostStoreImpl()
    }

    private inner class ManagePostStoreImpl :
        ManagePostStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ManagePostStore",
            initialState = state,
            executorFactory = ::executor,
            reducer = ManagePostReducer
        )
}