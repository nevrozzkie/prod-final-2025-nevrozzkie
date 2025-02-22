import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import base.CTopAppBar
import base.LazyColumnWithTopShadow
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import decompose.isLoading
import main.MainComponent
import main.MainStore
import tickers.TickersStore

@Composable
fun MainScreen(
    component: MainComponent
) {
    MainContent(component)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    component: MainComponent
) {
    val mainModel by component.model.subscribeAsState()
    val networkModel by component.networkStateManager.networkModel.subscribeAsState()


    val tickersComponent = component.tickersComponent
    val tickersNetworkModel by tickersComponent.networkStateManager.networkModel.subscribeAsState()

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CTopAppBar(
                title = "Главная"
            ) {
                SearchRow(
                    trailingIcon = Icons.Rounded.Newspaper
                ) {
                    component.onOutput(
                        MainComponent.Output.NavigateToSearch(
                            tickersComponent = tickersComponent,
                            mainComponent = component
                        )
                    )
                }
            }
        }
    ) { paddings ->
        PullToRefreshBox(
            isRefreshing = (networkModel.isLoading || tickersNetworkModel.isLoading),
            onRefresh = {
                tickersComponent.onEvent(TickersStore.Intent.FetchMainTickers)
                component.onEvent(MainStore.Intent.Refresh)

            },
            modifier = Modifier.padding(top = paddings.calculateTopPadding())
        ) {
            LazyColumnWithTopShadow(
                isShadowAlways = false
            ) {
                item(key = "tickers") {
                    TickerContent(tickersComponent)
                }
                newsItemsContent(mainModel, component, networkModel)
            }
        }
    }

}