package common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import view.theme.Paddings

@Composable
fun TagItem(
    selected: Boolean,
    name: String,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = Paddings.small,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = verticalPadding),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = FilterChipDefaults.elevatedFilterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.tertiary),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    )
}


@Composable
fun TagsGrid(
    content: LazyStaggeredGridScope.() -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(100.dp),
        modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(
            Paddings.semiMedium,
            alignment = Alignment.CenterHorizontally
        ),
    ) {
        content()
    }
}