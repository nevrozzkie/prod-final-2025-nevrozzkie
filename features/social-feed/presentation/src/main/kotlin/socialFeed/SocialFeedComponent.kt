package socialFeed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent

class SocialFeedComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : ComponentContext by componentContext,
    DefaultMVIComponent<SocialFeedStore.Intent, SocialFeedStore.State, SocialFeedStore.Label> {
    private val factory = SocialFeedStoreFactory(
        storeFactory = storeFactory,
        executor = SocialFeedExecutor()
    )
    override val store: SocialFeedStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

}