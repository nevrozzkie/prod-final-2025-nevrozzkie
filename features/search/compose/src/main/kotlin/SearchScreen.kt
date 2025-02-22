import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import base.CTopAppBar
import base.LazyColumnWithTopShadow
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import decompose.isLoading
import main.MainComponent
import tickers.TickersStore
import view.theme.Paddings

@Composable
fun SearchScreen(
    component: SearchComponent
) {
    val searchModel by component.model.subscribeAsState()

    val tickersComponent = component.tickersComponent
    val tickersNetworkModel by tickersComponent.networkStateManager.networkModel.subscribeAsState()
    val tickersModel by tickersComponent.model.subscribeAsState()

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CTopAppBar(
                title = "Поиск",
                action = {
                    Row {
                        androidx.compose.animation.AnimatedVisibility(
                            tickersNetworkModel.isLoading && tickersModel.searchTickers.isNotEmpty()
                        ) {
                            CircularProgressIndicator()
                        }
                        MoneySwitch(tickersModel, tickersComponent)
                    }
                }
            ) {
                SearchRow(
                    query = searchModel.query,
                    onQueryChange = {
                        component.onEvent(SearchStore.Intent.InputSearch(it))
                    },
                    onTrailingIconClick = {
                        component.onOutput(SearchComponent.Output.NavigateBack)
                    }
                )
            }
        }
    ) { paddings ->
        LazyColumnWithTopShadow(
            topPadding = paddings.calculateTopPadding(),
            isShadowAlways = false,
            contentPadding = PaddingValues(horizontal = Paddings.hMainContainer)
        ) {
            item(key = "tickers") {
                SearchTickerContent(tickersComponent, searchModel.query)
            }
            items(searchModel.news, key = { it.id}) {
                NewsItemContent(it) { }
            }
//            newsItemsContent(mainModel, component, networkModel)
        }
    }
}