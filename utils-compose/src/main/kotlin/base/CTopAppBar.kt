package base

import android.graphics.drawable.Icon
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseInQuint
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.serialization.Serializable
import view.theme.Paddings

@Composable
fun CTopAppBar(

    title: String,
    navigation: @Composable () -> Unit = {},
    action: @Composable () -> Unit = {},
    bottomPadding: Dp = 0.dp,
    horizontalPaddings: Dp = Paddings.hTopBar,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    isCentre: Boolean = false,
    extraContent: @Composable () -> Unit
) {
    Column(
        Modifier.background(
            backgroundColor
        )
    ) {
        Row(Modifier
            .padding(top = Paddings.medium)
            .fillMaxWidth()
            .padding(horizontal = horizontalPaddings),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isCentre) Arrangement.SpaceBetween else Arrangement.Start
        ) {
            navigation()
            Text(
                title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
            if (!isCentre) Spacer(Modifier.weight(1f))
            action()
        }
        extraContent()
        Spacer(Modifier.height(bottomPadding))
    }
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
