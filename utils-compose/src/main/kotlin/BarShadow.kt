import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import base.easedVerticalGradient


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