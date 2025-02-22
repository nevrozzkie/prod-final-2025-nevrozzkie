package wrapContainers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize

@Composable
fun LazyItemScope.AnimateColumnItem(
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable () -> Unit
) {
    Column(Modifier.animateItem(), horizontalAlignment = horizontalAlignment) {
        content()
    }
}

@Composable
fun LazyStaggeredGridItemScope.AnimateItem(
    content: @Composable () -> Unit
) {
    Box(
        Modifier.animateItem(
            fadeInSpec = spring(stiffness = Spring.StiffnessLow),
            fadeOutSpec = spring(stiffness = Spring.StiffnessLow),
            placementSpec = spring(stiffness = Spring.StiffnessLow),
        )
    ) {
        content()
    }
}

@Composable
fun LazyItemScope.AnimateRowItem(content: @Composable () -> Unit) {
    Row(Modifier.animateItem()) {
        content()
    }
}

@Composable
fun AnimateSlideVertically(
    isShowing: Boolean,
    multiplier: Int = 1,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isShowing,
        enter =
        slideInVertically { it * multiplier },
        exit =
        slideOutVertically { it * multiplier },
        modifier = modifier
    ) {
        content()
    }
}


@Composable
fun AnimatedVerticalColumn(
    isShowing: Boolean,
    modifier: Modifier = Modifier,
    isGoDown: Boolean = false,
    content: @Composable (AnimatedVisibilityScope) -> Unit
) {
    AnimatedVisibility(
        isShowing,
        enter = expandVertically(expandFrom = if (isGoDown) Alignment.Top else Alignment.Bottom) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = if (isGoDown) Alignment.Top else Alignment.Bottom) + fadeOut(),
        modifier = modifier,
    ) {
        Column {
            content(this@AnimatedVisibility)
        }
    }
}

@Composable
fun AnimatedSmoothTransition(
    isShowing: Boolean,
    modifier: Modifier = Modifier,
    isGoDown: Boolean = false,
    content: @Composable () -> Unit
) {
    val spec: FiniteAnimationSpec<IntSize> = tween(500)
    AnimatedVisibility(
        isShowing,
        modifier = modifier,
        enter = expandVertically(
            spec,
            expandFrom = if (isGoDown) Alignment.Top else Alignment.Bottom
        ),
        exit = shrinkVertically(
            spec,
            shrinkTowards = if (isGoDown) Alignment.Top else Alignment.Bottom
        )
    ) {
        content()
    }
}

@Composable
fun AnimatedBox(
    isShowing: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier) {
        AnimatedVisibility(
            isShowing,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                content()
            }
        }
    }
}