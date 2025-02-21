package socialFeed

import ManagePostDTO
import PostNewsData
import android.icu.util.Output
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent

class SocialFeedComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (Output) -> Unit
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

    fun onOutput(output: Output) {
        output(output)
    }


    sealed class Output() {
        data class NavigateToManagePost(val post: ManagePostDTO?) : Output()
        data class NavigateToNewsSite(
            val newsData: PostNewsData
        ) : Output()
    }
}