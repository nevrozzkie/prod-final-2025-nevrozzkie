import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import base.NetworkCrossfade
import base.TonalCard
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import decompose.isLoading
import decompose.isNotError
import decompose.isOk
import tickers.TickersComponent
import tickers.TickersStore
import view.theme.Paddings

@Composable
fun SearchTickerContent(
    component: TickersComponent,
    query: String
) {
    val model by component.model.subscribeAsState()
    val networkModel by component.networkStateManager.networkModel.subscribeAsState()

    val tickers = if (query.isEmpty()) {
        model.mainTickers
    } else {
        model.searchTickers
    }
    Spacer(Modifier.height(Paddings.hTopBar))
    TonalCard(
        Modifier
    ) {
        Crossfade(
            networkModel, label = "tickerNetworkStateAnimation"
        ) { state ->
            when {
                state.isOk || tickers.isNotEmpty() -> {
                    Column {
                        tickers.forEach { ticker ->
                            HorizontalTicker(
                                ticker = ticker,
                                isConverted = model.isConverted,
                                modifier = Modifier
                                    .padding(Paddings.medium)
                                    .padding(horizontal = Paddings.verySmall)
                            )
                        }
                    }
                    if (tickers.isEmpty()) {

                            Text("Не удалось найти тикеры по этому запросу",
                                modifier = Modifier.fillMaxWidth().padding(Paddings.large), textAlign = TextAlign.Center)

                    }
                }

                state.isLoading -> {
                    Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}