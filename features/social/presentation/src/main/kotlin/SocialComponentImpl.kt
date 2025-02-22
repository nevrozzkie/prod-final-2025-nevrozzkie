import SocialComponent.Child
import SocialComponent.Config
import SocialComponent.Config.*
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.store.StoreFactory
import managePost.ManagePostComponent
import managePost.default
import socialFeed.SocialFeedComponent
import kotlin.reflect.KClass

class SocialComponentImpl(
    componentContext: ComponentContext,
    val post: ManagePostDTO?,
    private val storeFactory: StoreFactory,
    val fromFeedToMainOutput: (SocialFeedComponent.Output) -> Unit,
    override val instanceKeeper: InstanceKeeper
) : SocialComponent, ComponentContext by componentContext {


    private val nav = StackNavigation<Config>()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = if (post != null) ManagePost(post) else Feed,
        childFactory = ::child,
    )


    override val stack: Value<ChildStack<*, Child>> = _stack


    private fun child(config: Config, childContext: ComponentContext): Child =
        when (config) {
            Feed -> Child.FeedChild(
                SocialFeedComponent(
                    childContext,
                    storeFactory,
                    ::onSocialFeedOutput
                )
            )

            is ManagePost -> Child.ManagePostChild(
                ManagePostComponent(
                    childContext,
                    storeFactory,
                    post = config.post,
                    output = ::onManagePostOutput
                )
            )
        }

    private fun onManagePostOutput(output: ManagePostComponent.Output) {
        when (output) {
            ManagePostComponent.Output.NavigateBack -> popOnce(Child.ManagePostChild::class)
            ManagePostComponent.Output.NavigateToFeed -> nav.replaceAll(Config.Feed)
        }
    }

    private fun onSocialFeedOutput(output: SocialFeedComponent.Output): Unit =
        when (output) {
            is SocialFeedComponent.Output.NavigateToManagePost -> {
                val postDTO = output.post ?: ManagePostDTO.default

                nav.bringToFront(
                    ManagePost(
                        post = postDTO
                    )
                )
            }

            is SocialFeedComponent.Output.NavigateToNewsSite -> fromFeedToMainOutput(output)
        }


    override fun onBackClicked() {
        popOnce(child = stack.active.instance::class)
    }

    override fun onOutput(config: Config) {
        nav.pushToFront(config)
    }

    private fun popOnce(child: KClass<out Child>) {
        nav.pop()
    }
}