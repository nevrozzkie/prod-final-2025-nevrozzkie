package widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import base.TonalCard
import finance.FinanceComponent
import finance.FinanceStore
import utils.formatLikeAmount
import utils.progressWithColor
import view.theme.Paddings
import wrapContainers.AnimateColumnItem
import kotlin.math.roundToInt

fun LazyListScope.summaryContent(
    model: FinanceStore.State,
    component: FinanceComponent
) {
    item("FinanceSummary") {
        AnimateColumnItem {
            CircularPercentBar(model.totalSavedAmount, model.totalNeededAmount)

            Spacer(Modifier.height(Paddings.medium))
            Text(
                "${model.totalSavedAmount.formatLikeAmount(addRuble = true)}\nвсего ",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Paddings.verySmall))
            Text(
                "из ${model.totalNeededAmount.formatLikeAmount(addRuble = true)}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Paddings.small))
            Row(Modifier.fillMaxWidth()) {
                TonalCard(
                    modifier = Modifier.fillMaxWidth(.33f)
                ) {
                    Column(Modifier.padding(Paddings.medium)) {
                        Text("Выполнено", fontWeight = FontWeight.Medium)
                        Text(
                            model.completedGoals.size.toString(), modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.End
                        )
                    }
                }
                Spacer(Modifier.width(Paddings.medium))
                TonalCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(Paddings.medium)) {
                        Text("Осталось накопить", fontWeight = FontWeight.Medium)
                        Text(
                            "${(model.totalNeededAmount- model.totalSavedAmount).coerceAtLeast(0).formatLikeAmount()} ₽", modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CircularPercentBar(
    totalSavedAmount: Long,
    totalNeededAmount: Long
) {
    val (progress, progressPercent, animatedColor) = progressWithColor(
        totalSavedAmount,
        totalNeededAmount
    )
    Box(contentAlignment = Alignment.Center) {
        Text(
            "${progressPercent.roundToInt().coerceAtMost(500)}%",
            fontWeight = FontWeight.Black,
            style = MaterialTheme.typography.displayMedium
        )
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .size(180.dp)
                .rotate(-90f),
            color = animatedColor,
            strokeWidth = 10.dp,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    }
}