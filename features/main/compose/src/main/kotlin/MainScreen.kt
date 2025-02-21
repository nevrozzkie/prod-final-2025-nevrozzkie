import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import base.CTopAppBar
import base.LazyColumnWithTopShadow
import base.easedVerticalGradient
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
    val mainModel by component.model.subscribeAsState()
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
    ) { paddings ->
        LazyColumnWithTopShadow(
            topPadding = paddings.calculateTopPadding(),
            isShadowAlways = false
        ) {
            item(key = "tickers") {
                TickerContent(tickersComponent)
            }
            newsItemsContent(mainModel, component, networkModel)
        }
    }

}