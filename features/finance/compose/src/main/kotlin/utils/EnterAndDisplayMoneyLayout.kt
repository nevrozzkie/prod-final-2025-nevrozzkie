package utils

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.fastFirst
import base.EditableText
import view.theme.Paddings
import utils.visualTransformations.MoneyOnlyLongVisualTransformation


private object EnterAndDisplayMoneyLayoutIds {
    const val LEFT_SIDE = "leftSide"
    const val SEPARATOR = "separator"
    const val MONEY = "money"
}

@Composable
fun EnterAndDisplayMoneyLayout(
    leftSide: @Composable RowScope.() -> Unit,
    separator: @Composable (() -> Unit) = {},
    value: String,
    valueIfText: AnnotatedString,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    imeAction: ImeAction,
    isTextField: Boolean
) {
    Layout(
        content = {
            Row(Modifier.layoutId(EnterAndDisplayMoneyLayoutIds.LEFT_SIDE).padding(end = Paddings.medium), verticalAlignment = Alignment.CenterVertically) {
                leftSide()
            }
            Box(Modifier.layoutId(EnterAndDisplayMoneyLayoutIds.SEPARATOR)) {
                separator()
            }
            EditableText(
                value = value,
                valueIfText = valueIfText,
                onValueChange = onValueChange,
                placeholderText = placeholderText,
                placeholderAlignment = Alignment.CenterEnd,
                textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.End),
                modifier = Modifier
                    .layoutId(EnterAndDisplayMoneyLayoutIds.MONEY)
                    .animateContentSize(),
                visualTransformation = MoneyOnlyLongVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction
                ),
                isTextField = isTextField
            )
        }
    ) { measurables, constraints ->
        val moneyPlaceable = measurables
            .fastFirst { it.layoutId == EnterAndDisplayMoneyLayoutIds.MONEY }
            .measure(
                constraints.copy(
                    minWidth = 0,
                    maxWidth = (constraints.maxWidth) - (constraints.maxWidth / 4)
                )
            )

        val separatorPlaceable = measurables
            .fastFirst { it.layoutId == EnterAndDisplayMoneyLayoutIds.SEPARATOR }
            .measure(
                constraints.copy(
                    minWidth = 0
                )
            )

        val leftSidePlaceable = measurables
            .fastFirst { it.layoutId == EnterAndDisplayMoneyLayoutIds.LEFT_SIDE }
            .measure(constraints.copy(
                maxWidth = constraints.maxWidth-moneyPlaceable.width - separatorPlaceable.width
            ))
        val totalHeight = maxOf(
            leftSidePlaceable.height,
            moneyPlaceable.height
        )

        layout(width = constraints.maxWidth, height = totalHeight) {
            val moneyX = constraints.maxWidth - moneyPlaceable.width
            val separatorX = leftSidePlaceable.width + ((moneyX - leftSidePlaceable.width - separatorPlaceable.width) / 2)

            leftSidePlaceable.placeRelative(
                x = 0,
                y = (totalHeight - leftSidePlaceable.height) / 2
            )

            separatorPlaceable.placeRelative(
                x = separatorX,
                y = (totalHeight - leftSidePlaceable.height) / 2
            )

            moneyPlaceable.placeRelative(
                x = moneyX,
                y = (totalHeight - moneyPlaceable.height) / 2
            )
        }
    }
}