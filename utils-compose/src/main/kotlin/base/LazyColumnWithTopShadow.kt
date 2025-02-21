package base

import AnimatedSmoothTransition
import BarShadow
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import view.theme.Paddings

@Composable
fun LazyColumnWithTopShadow(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    isShadowAlways: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    Box(Modifier.padding(top = topPadding)) {
        LazyColumn(
            modifier, state = state, horizontalAlignment = horizontalAlignment,
            contentPadding = contentPadding
        ) {
            content()
            item {
                Spacer(Modifier.height(Paddings.bottomScrollPadding ))
            }
        }
        AnimatedSmoothTransition(
            state.canScrollBackward || isShadowAlways
        ) {
            BarShadow()
        }
    }
}