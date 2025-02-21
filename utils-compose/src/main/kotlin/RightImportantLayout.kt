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


private object RightImportantLayoutIds {
    const val LEFT_SIDE = "leftSide"
    const val RIGHT_SIDE = "rightSide"
}

@Composable
fun RightImportantLayout(
    modifier: Modifier = Modifier,
    leftSide: @Composable (RowScope) -> Unit,
    rightSide: @Composable (RowScope) -> Unit,
) {
    Layout(
        content = {
            Row(Modifier.layoutId(RightImportantLayoutIds.LEFT_SIDE), verticalAlignment = Alignment.CenterVertically) {
                leftSide(this)
            }
            Row(Modifier.layoutId(RightImportantLayoutIds.RIGHT_SIDE), verticalAlignment = Alignment.CenterVertically) {
                rightSide(this)
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val rightPlaceable = measurables
            .fastFirst { it.layoutId == RightImportantLayoutIds.RIGHT_SIDE }
            .measure(
                constraints.copy(
                    minWidth = 0,
                    maxWidth = (constraints.maxWidth) - (constraints.maxWidth / 5)
                )
            )

        val leftSidePlaceable = measurables
            .fastFirst { it.layoutId == RightImportantLayoutIds.LEFT_SIDE }
            .measure(
                constraints.copy(
                    minWidth = 0,
                    maxWidth = constraints.maxWidth - rightPlaceable.width
                )
            )
        val totalHeight = maxOf(
            leftSidePlaceable.height,
            rightPlaceable.height
        )

        layout(width = constraints.maxWidth, height = totalHeight) {
            leftSidePlaceable.placeRelative(
                x = 0,
                y = (totalHeight - leftSidePlaceable.height) / 2
            )

            rightPlaceable.placeRelative(
                x = constraints.maxWidth - rightPlaceable.width,
                y = (totalHeight - rightPlaceable.height) / 2
            )
        }

    }
}
