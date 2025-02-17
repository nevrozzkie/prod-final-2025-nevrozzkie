import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LazyItemScope.AnimateColumnItem(content: @Composable () -> Unit) {
    Column(Modifier.animateItem()) {
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
fun AnimatedVerticalColumn(isShowing: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        isShowing,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun AnimatedBox(isShowing: Boolean, content: @Composable () -> Unit) {
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