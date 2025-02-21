import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import decompose.DefaultMVIComponent
import kotlinx.serialization.Serializable
import managePost.ManagePostComponent
import socialFeed.SocialFeedComponent

interface SocialComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class FeedChild(val component: SocialFeedComponent) : Child()
        data class ManagePostChild(val component: ManagePostComponent) : Child()
    }


    @Serializable
    sealed interface Config {
        @Serializable
        data object Feed : Config
        @Serializable
        data class ManagePost(val post: ManagePostDTO) : Config
    }

    fun onBackClicked()

    fun onOutput(config: Config)
}