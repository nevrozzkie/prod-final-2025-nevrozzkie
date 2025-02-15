package socialFeed

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import socialFeed.SocialFeedStore.Intent
import socialFeed.SocialFeedStore.Label
import socialFeed.SocialFeedStore.State
import socialFeed.SocialFeedStore.Message

class SocialFeedStoreFactory(
    private val storeFactory: StoreFactory,
    val executor: SocialFeedExecutor
) {

    fun create(): SocialFeedStore {
        return SocialFeedStoreImpl()
    }

    private inner class SocialFeedStoreImpl :
        SocialFeedStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "SocialFeedStore",
            initialState = State,
            executorFactory = ::executor,
            reducer = SocialFeedReducer
        )
}