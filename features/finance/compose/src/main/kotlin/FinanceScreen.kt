import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.asLongState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import base.CTopAppBar
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.getValue
import finance.FinanceComponent
import goals.GoalsStore
import view.theme.Paddings

@Composable
fun FinanceScreen(
    component: FinanceComponent
) {
    FinanceContent(component)
    FinanceOverlay(component)
}

@Composable
private fun FinanceOverlay(
    component: FinanceComponent
) {
    val goalsComponent = component.goalsComponent

    val goalsModel by goalsComponent.model.subscribeAsState()
    GoalsPlannedDatePicker(
        model = goalsModel,
        component = goalsComponent
    )
}

@Composable
private fun FinanceContent(
    component: FinanceComponent
) {
    val model by component.model.subscribeAsState()
    val goalsComponent = component.goalsComponent
    val transactionsComponent = component.transactionsComponent

    val goalsModel by goalsComponent.model.subscribeAsState()
    val activeGoals by goalsComponent.flowers.activeGoals.collectAsState(emptyList())
    val completedGoals by goalsComponent.flowers.completedGoals.collectAsState(emptyList())
    val goalMaxId by goalsComponent.flowers.maxId.collectAsState(0).asLongState()
    val transactions by transactionsComponent.flows.transactions.collectAsState(emptyList())

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CTopAppBar(
                title = "Финансы",
                titlePaddings = PaddingValues(bottom = Paddings.medium)
            ) { }
        }
    ) { paddings ->
        Box(Modifier.padding(paddings).padding(horizontal = Paddings.hMainContainer), contentAlignment = Alignment.Center) {
            LazyColumn(Modifier.fillMaxSize()) {
                goalsContent(
                    model = goalsModel,
                    activeGoals = activeGoals,
                    completedGoals = completedGoals,
                    component = goalsComponent,
                    transactions = transactions,
                    maxId = goalMaxId
                )
            }

            if (goalMaxId == 0L) {
                Column {
                    Text("Вы ещё не ставили для себя никаких целей")
                    Button(
                        onClick = {
                            goalsComponent.onEvent(
                                GoalsStore.Intent.StartCreatingOrEditing(
                                    id = 1,
                                    name = "",
                                    amount = 0,
                                    createdDate = null,
                                    plannedDate = null
                                )
                            )
                        }
                    ) { }
                }
            }
        }

    }
}