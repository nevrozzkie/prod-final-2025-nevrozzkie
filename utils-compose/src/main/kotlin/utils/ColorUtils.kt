package utils

import androidx.compose.animation.core.Easing
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


fun Color.toHex(): String {
    val bBuf = this.toArgb().toUInt().toString(16)
    return "#" + bBuf.substring(bBuf.length - 6)
}


// code from PansionApp via Haze!!
fun Brush.Companion.easedVerticalGradient(
    color: Color,
    easing: Easing,
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY,
    numStops: Int = 8,
    isReversed: Boolean = false
): Brush = easedGradient(
    color = color,
    easing = easing,
    numStops = numStops,
    start = Offset(x = 0f, y = startY),
    end = Offset(x = 0f, y = endY),
    isReversed = isReversed
)

fun Brush.Companion.easedGradient(
    color: Color,
    easing: Easing,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite,
    numStops: Int = 8,
    isReversed: Boolean
): Brush {
    val colors = List(numStops) { i ->
        val x = i * 1f / (numStops - 1)
        color.copy(alpha = 1f - easing.transform(x))
    }

    return linearGradient(
        colors = if (isReversed) colors.reversed() else colors,
        start = start,
        end = end
    )
}
