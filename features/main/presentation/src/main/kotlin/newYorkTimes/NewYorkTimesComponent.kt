package newYorkTimes

import android.webkit.WebView
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import decompose.DefaultMVIComponent
import decompose.NetworkStateManager
import koin.Inject

class NewYorkTimesComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    val initialUrl: String,
    val id: String,
    val icon: ByteArray?,
    val title: String,
    private val output: (Output) -> Unit
) : ComponentContext by componentContext,
    DefaultMVIComponent<NewYorkTimesStore.Intent, NewYorkTimesStore.State, NewYorkTimesStore.Label> {
    val networkStateManager = NetworkStateManager()


    val webView = WebView(Inject.instance())

    private val factory = NewYorkTimesStoreFactory(
        storeFactory = storeFactory,
        executor = NewYorkTimesExecutor(
            webView = webView,
            initialUrl = initialUrl,
            networkStateManager = networkStateManager
        )
    )
    override val store: NewYorkTimesStore
        get() = instanceKeeper.getStore() {
            factory.create()
        }

    fun onOutput(output: Output) {
        output(output)
    }
    sealed class Output {
        data object NavigateBack : Output()
        data class RepostNews(
            val url: String,
            val id: String,
            val icon: ByteArray?,
            val title: String
        ) : Output()
    }
}