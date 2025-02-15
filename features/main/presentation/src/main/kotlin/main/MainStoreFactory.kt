package main

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State

class MainStoreFactory(
    private val storeFactory: StoreFactory,
    private val executor: MainExecutor
) {

    fun create(): MainStore {
        return MainStoreImpl()
    }

    private inner class MainStoreImpl :
        MainStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            initialState = MainStore.State(),
            executorFactory = ::executor,
            reducer = MainReducer
        )
}
