import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import base.CTopAppBar
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import finance.FinanceComponent
import view.theme.Paddings

@Composable
fun FinanceScreen(
    component: FinanceComponent
) {
    FinanceContent(component)
}

@Composable
private fun FinanceContent(
    component: FinanceComponent
) {
    val model by component.model.subscribeAsState()

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CTopAppBar(
                title = "Финансы",
                titlePaddings = PaddingValues(bottom = Paddings.medium)
            ) { }
        }
    ) { paddings ->
        LazyColumn(Modifier.padding(paddings)) {

        }
    }
}