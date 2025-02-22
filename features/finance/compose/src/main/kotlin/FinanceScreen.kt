import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import base.CTopAppBar
import base.LazyColumnWithTopShadow
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import finance.FinanceComponent
import goals.GoalsStore
import utils.GoalsPlannedDatePicker
import view.theme.Paddings
import widgets.goalsContent
import widgets.summaryContent
import widgets.transactionsContent
import wrapContainers.AnimateColumnItem
import utils.subscribeOnLabels

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
    val goalsComponent = component.goalsComponent
    val transactionsComponent = component.transactionsComponent


    val model by component.model.subscribeAsState()
    val goalsModel by goalsComponent.model.subscribeAsState()
    val transactionsModel by transactionsComponent.model.subscribeAsState()

    val transactions = model.transactions
    val activeGoals = model.activeGoals
    val completedGoals = model.completedGoals

    val goalsLazyStateIndex = 2
    val transactionsLazyStateIndex = goalsLazyStateIndex + activeGoals.size + 1


    val lazyState = rememberLazyListState()
    goalsComponent.subscribeOnLabels {
        when (it) {
            GoalsStore.Label.ScrollToCreatedGoal -> {
                val itemIndex =
                    activeGoals.indexOfFirst { it.id == goalsModel.editingGoal?.id }
                val indexToScroll =
                    if (itemIndex != -1) goalsLazyStateIndex + itemIndex else transactionsLazyStateIndex - 1
                lazyState.animateScrollToItem(indexToScroll)
            }
        }
    }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            AnimatedContent(
                when (remember { derivedStateOf { lazyState.firstVisibleItemIndex } }.value) {
                    in 0..goalsLazyStateIndex -> "Финансы"
                    in goalsLazyStateIndex..transactionsLazyStateIndex -> "Цели"
                    else -> "Операции"
                },
                label = "financeTopAppBarTextAnimation",
                transitionSpec = {
                    fadeIn(animationSpec = tween(300, delayMillis = 100))
                        .togetherWith(fadeOut(animationSpec = tween(300)))
                }
            ) { title ->
                CTopAppBar(
                    title = title,
                    bottomPadding = Paddings.verySmall
                ) { }
            }
        }
    ) { paddings ->
        LazyColumnWithTopShadow(
            Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = Paddings.hMainContainer),
            topPadding = paddings.calculateTopPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyState
        ) {
            item {
                AnimateColumnItem {
                    Spacer(Modifier.height(Paddings.vNoShadow))
                }
            }
            if ((activeGoals + completedGoals).isNotEmpty() || goalsModel.editingGoal != null) {
                summaryContent(model, component)

                goalsContent(
                    model = goalsModel,
                    component = goalsComponent,
                    activeGoals = activeGoals,
                    financeComponent = component
                )
                if (activeGoals.isNotEmpty()) {
                    transactionsContent(
                        model = transactionsModel,
                        transactions = transactions,
                        component = transactionsComponent,
                        goals = activeGoals
                    )
                }
            } else {
                item {
                    Column(
                        Modifier.fillMaxSize().padding(top = Paddings.bottomScrollPadding*3),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Отслеживайте свои финансы уже сегодня!", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Button(
                            onClick = {
                                goalsComponent.onEvent(GoalsStore.Intent.StartCreatingOrEditing(
                                    id = goalsModel.maxId + 1,
                                    name = "",
                                    amount = 0,
                                    createdDate = null,
                                    plannedDate = null
                                ))
                                println("TEST: ${goalsModel.editingGoal}")
                            }
                        ) {
                            Text("Начать")
                        }
                    }
                }
            }
        }


//        if (goalMaxId == 0L) {
//            Column {
//                Text("Вы ещё не ставили для себя никаких целей")
//                Button(
//                    onClick = {
//                        goalsComponent.onEvent(
//                            GoalsStore.Intent.StartCreatingOrEditing(
//                                id = 1,
//                                name = "",
//                                amount = 0,
//                                createdDate = null,
//                                plannedDate = null
//                            )
//                        )
//                    }
//                ) { }
//            }
//        }

    }
}