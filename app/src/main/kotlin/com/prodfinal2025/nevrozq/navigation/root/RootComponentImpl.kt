package com.prodfinal2025.nevrozq.navigation.root

import PostNewsData
import SearchComponent
import SearchExecutor
import SocialComponentImpl
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import bitmap
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Child
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Child.FinanceChild
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Child.MainChild
import com.prodfinal2025.nevrozq.navigation.root.RootComponent.Config
import finance.FinanceComponent
import main.MainComponent
import managePost.default
import newYorkTimes.NewYorkTimesComponent
import socialFeed.SocialFeedComponent
import java.io.ByteArrayOutputStream
import kotlin.reflect.KClass

class RootComponentImpl(
    private val activity: ComponentActivity,
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : RootComponent, ComponentContext by componentContext {
    private val nav = StackNavigation<Config>()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main,
        childFactory = ::child,
    )

    override val stack = _stack

    private fun child(config: Config, childContext: ComponentContext): Child =
        when (config) {
            Config.Main ->
                MainChild(
                    retainedInstance {
                        MainComponent(
                            childContext,
                            storeFactory,
                            instanceKeeper,
                            output = ::onMainOutput
                        )
                    }
                )


            Config.Finance -> FinanceChild(
                retainedInstance {
                    FinanceComponent(childContext, storeFactory, instanceKeeper)
                }
            )

            is Config.Social -> {
                Child.SocialChild(
                    SocialComponentImpl(
                        childContext,
                        storeFactory = storeFactory,
                        instanceKeeper = instanceKeeper,
                        post = config.post,
                        fromFeedToMainOutput = ::onSocialFeedOuput
                    )
                )
            }

            is Config.NewYorkTimes ->
                Child.NewYorkTimesChild(
                    NewYorkTimesComponent(
                        componentContext = childContext,
                        storeFactory = storeFactory,
                        initialUrl = config.initialUrl,
                        icon = config.icon,
                        id = config.id,
                        title = config.title,
                        output = ::onNewYorkTimesOutput,
                    )
                )

            is Config.Search -> Child.SearchChild(
                SearchComponent(
                    componentContext = childContext,
                    storeFactory = storeFactory,
                    tickersComponent = config.tickersComponent,
                    output = ::onSearchOutput
                )
            )
        }

    private fun onSearchOutput(
        output: SearchComponent.Output
    )
            : Unit =
        when (output) {
            SearchComponent.Output.NavigateBack -> popOnce(Child.SearchChild::class)
        }

    private fun onSocialFeedOuput(output: SocialFeedComponent.Output): Unit =
        when (output) {
            is SocialFeedComponent.Output.NavigateToManagePost -> {} // not our otvetstvennost' -> sdelat' nescolko sealed classes
            is SocialFeedComponent.Output.NavigateToNewsSite -> {
                nav.bringToFront(
                    Config.NewYorkTimes(
                        output.newsData.url,
                        id = output.newsData.id,
                        icon = output.newsData.icon,
                        title = output.newsData.title
                    )
                )
            }
        }

    private fun onMainOutput(output: MainComponent.Output): Unit =
        when (output) {
            is MainComponent.Output.NavigateToNewsSite -> {
                val outputStream = ByteArrayOutputStream()
                output.image.bitmap?.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)

                nav.bringToFront(
                    Config.NewYorkTimes(
                        output.url,
                        id = output.id,
                        icon = outputStream.toByteArray(),
                        title = output.title
                    )
                )
            }

            is MainComponent.Output.NavigateToSearch -> {
                nav.bringToFront(Config.Search(
                    tickersComponent = output.tickersComponent,
                    mainComponent = output.mainComponent
                ))
            }
        }

    private fun onNewYorkTimesOutput(
        output: NewYorkTimesComponent.Output
    )
            : Unit =
        when (output) {
            NewYorkTimesComponent.Output.NavigateBack -> popOnce(Child.NewYorkTimesChild::class)
            is NewYorkTimesComponent.Output.RepostNews -> nav.bringToFront(
                Config.Social(
                    ManagePostDTO.default.copy(
                        newsData = PostNewsData(
                            id = output.id,
                            title = output.title,
                            icon = output.icon,
                            url = output.url
                        )
                    )
                )
            )
        }


    override fun onBackClicked() {
        popOnce(child = stack.active.instance::class)
    }

    private fun popOnce(
        child: KClass<out Child>
    ) {
        val currentConfig = stack.active.configuration
        if (currentConfig is Config.Main)
            activity.finish()
        else
            nav.pop()

    }

    override fun onOutput(child: KClass<out Child>, config: Config) {
        val existingConfig = stack.items.firstOrNull { child.isInstance(it.instance) }?.configuration
        nav.pushToFront(existingConfig ?: config)
    }
}