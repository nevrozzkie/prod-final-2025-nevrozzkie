import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round

@Composable
fun LongPressContainer(
    isEnabled: Boolean,
    onLongPress: (DpOffset) -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val view = LocalView.current

    var pressed by remember { mutableStateOf(false) }
    var height by remember { mutableIntStateOf(0) }
    Box(
        Modifier
            .scale(
                animateFloatAsState(
                    targetValue = if (pressed) 0.99f else 1f,
                    tween(250),
                    label = "GoalPressAnimation"
                ).value
            )
            .onSizeChanged {
                height = it.height
            }
            .pointerInput(isEnabled) {
                if (isEnabled) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            tryAwaitRelease()
                            pressed = false
                        },
                        onLongPress = {
                            // на моём телефоне только так заработало... LocalHapticFeedback совсем нет, а через view только с флагом
                            view.performHapticFeedback(
                                HapticFeedbackConstants.LONG_PRESS,
                                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                            )
                            onLongPress(
                                with(density) {
                                    DpOffset(
                                        x = it.x.toDp(),
                                        y = (it.y - height).toDp()
                                    )
                                }
                            )
                        }
                    )
                }
            }
    ) {
        content()
    }
}