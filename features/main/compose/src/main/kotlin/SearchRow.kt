import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirst
import base.EditableText
import base.TonalCard
import view.theme.Paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRow() {
    Row {
        CustomSearchBar(
            modifier = Modifier
                .padding(horizontal = Paddings.hMainContainer).padding(top = Paddings.medium)
                .height(40.dp)
                .fillMaxWidth(),
            query = "",
            onQueryChange = {},

            )
    }
}


private object SearchRowLayoutIds {
    const val TEXT_FIELD = "textfield"
    const val LEADING_ICON = "leadingIcon"
    const val TRAILING_ICON = "trailingIcon"
}


@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    leadingIcon: ImageVector = Icons.Rounded.Search,
    trailingIcon: ImageVector = Icons.Rounded.Mic,
    onTrailingIconClick: () -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    contentColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    shape: Shape = MaterialTheme.shapes.medium,
    modifier: Modifier,
    placeholderText: String = "Поиск",
) {
    val iconsSize = 24.dp
    val iconsBoxSize = 48.dp
    TonalCard (
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Layout(
            content = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .layoutId(SearchRowLayoutIds.LEADING_ICON)
                        .padding(horizontal = (iconsBoxSize - iconsSize) / 2)
                        .size(iconsSize)

                )
                EditableText(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .layoutId(SearchRowLayoutIds.TEXT_FIELD),
                    textStyle = textStyle,
                    placeholderText = placeholderText
                )


                IconButton(
                    onClick = onTrailingIconClick,
                    modifier = Modifier
                        .size(iconsBoxSize)
                        .layoutId(SearchRowLayoutIds.TRAILING_ICON),
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(iconsSize)
                    )
                }

            },
        ) { measurables, constraints ->
            val leadingIconPlaceable = measurables
                .fastFirst { it.layoutId == SearchRowLayoutIds.LEADING_ICON }
                .measure(constraints.copy(minWidth = 0))

            val trailingIconPlaceable =
                measurables.fastFirst { it.layoutId == SearchRowLayoutIds.TRAILING_ICON }
                    .measure(constraints.copy(minWidth = 0))
            val textFieldPlaceable = measurables
                .fastFirst { it.layoutId == SearchRowLayoutIds.TEXT_FIELD }
                .measure(
                    constraints.copy(
                        minWidth = constraints.maxWidth - leadingIconPlaceable.width
                                - trailingIconPlaceable.width,
                        minHeight = 0
                    )
                )


            val totalWidth =
                leadingIconPlaceable.width + textFieldPlaceable.width + trailingIconPlaceable.width

            val totalHeight = maxOf(
                leadingIconPlaceable.height,
                textFieldPlaceable.height,
                trailingIconPlaceable.height,
                constraints.maxHeight
            )

            // Layout children
            layout(totalWidth, totalHeight) {
                leadingIconPlaceable.placeRelative(
                    x = 0,
                    y = (totalHeight - (leadingIconPlaceable.height)) / 2,
                )

                // Place text field
                textFieldPlaceable.placeRelative(
                    x = leadingIconPlaceable.width,
                    y = (totalHeight - textFieldPlaceable.height) / 2,
                )

                trailingIconPlaceable.placeRelative(
                    x = leadingIconPlaceable.width + textFieldPlaceable.width,
                    y = (totalHeight - (trailingIconPlaceable.height)) / 2,
                )
            }
        }
    }

}