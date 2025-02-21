package utils

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

@Composable
fun progressWithColor(
    savedAmount: Long,
    targetAmount: Long
): Triple<Float, Float, Color> {

    var animationsStarted by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        (if (animationsStarted && targetAmount > 0) savedAmount / (targetAmount * 1f) else 0f)
            .coerceAtLeast(0f),
        label = "progressAnimation",
        animationSpec = tween(700, easing = LinearOutSlowInEasing)
    )
    val progressPercent = progress * 100

    val animatedColor = lerp(MaterialTheme.colorScheme.error, Color.Green, progress)
    LaunchedEffect(Unit) {
        animationsStarted = true
    }
    return Triple(progress, progressPercent, animatedColor)
}