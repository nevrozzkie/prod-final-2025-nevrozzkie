package base

import wrapContainers.AnimatedSmoothTransition
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
import utils.easedVerticalGradient
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

val barShadowEasing = CubicBezierEasing(.4f, -0.07f, .34f, 1.02f)

@Composable
fun BarShadow(
    modifier: Modifier = Modifier,
    height: Dp = 80.dp,
    isReversed: Boolean = false
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(
                Brush.easedVerticalGradient(
                    MaterialTheme.colorScheme.background,
                    numStops = 12,
                    easing = barShadowEasing,
                    isReversed = isReversed
                )
            )
    )
}