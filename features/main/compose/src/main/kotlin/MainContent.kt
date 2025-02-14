import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import base.CTopAppBar
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import main.MainComponent
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
    val tickersComponent = component.tickersComponent
    val tickersModel by tickersComponent.model.subscribeAsState()
    val tickersNetworkModel by tickersComponent.networkStateManager.networkModel.subscribeAsState()
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CTopAppBar(
                title = "Главная"
            ) {
                SearchRow()
            }
        },
        floatingActionButton = {
            Button(onClick = {
                component.tickersComponent.onEvent(TickersStore.Intent.LoadMainTickers)
            }) {
                Text(tickersNetworkModel.state.toString())
            }
        },
        floatingActionButtonPosition = FabPosition.Start
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            item {
                TickerContent(tickersModel, isConverted = false)
            }
        }
    }
}