import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import base.CTopAppBar
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import main.MainComponent
import tickers.TickersStore
import view.theme.Paddings

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
    val model by component.model.subscribeAsState()
    val networkModel by component.networkStateManager.networkModel.subscribeAsState()

    val tickersComponent = component.tickersComponent

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CTopAppBar(
                title = "Главная"
            ) {
                SearchRow()
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            item {
                Spacer(Modifier.height(Paddings.medium))
            }
            item {
                TickerContent(tickersComponent)
            }
            items(model.news) {
                NewsItemContent(it)
            }
            item {
                Spacer(Modifier.height(Paddings.bottomScrollPadding))
            }
        }
    }
}