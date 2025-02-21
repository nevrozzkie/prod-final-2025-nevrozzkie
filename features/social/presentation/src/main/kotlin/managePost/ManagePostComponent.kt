package managePost

import ManagePostDTO
import bitmap
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import getCurrentLocalDateTime
import koin.Inject
import kotlinx.datetime.format
import kotlinx.serialization.Serializable
import rusFormat
import socialFeed.SocialFeedComponent
import time24Format





class ManagePostComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    post: ManagePostDTO,
    val output: (Output) -> Unit
) : ComponentContext by componentContext,
    DefaultMVIComponent<ManagePostStore.Intent, ManagePostStore.State, ManagePostStore.Label> {
    private val factory = ManagePostStoreFactory(
        storeFactory = storeFactory,
        executor = ManagePostExecutor(output = output),
        state = ManagePostStore.State(
            id = post.id,
            images = post.images.mapNotNull { it.bitmap },
            pickedTags = post.tags,
            newsData = post.newsData,
            text = post.text
        )
    )
    override val store: ManagePostStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    fun onDispatch(msg: ManagePostStore.Message) {
        factory.executor.onDispatch(msg)
    }

    fun onOutput(output: Output) {
        output(output)
    }

    sealed class Output() {
        data object NavigateBack : Output()
        data object NavigateToFeed : Output()
    }

}