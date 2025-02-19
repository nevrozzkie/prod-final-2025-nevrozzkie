import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import base.CBasicTextFieldDefaults
import base.EditableText
import base.TonalCard
import view.theme.Paddings
import view.themeColors
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
fun DropdownMenuContainer(
    isEnabled: Boolean,
    dropdownContent: @Composable (MutableState<Boolean>) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    var isDropdownExpanded = remember { mutableStateOf(false) }
    var offset: DpOffset by remember { mutableStateOf(DpOffset.Zero) }

    Box(contentAlignment = Alignment.TopStart) {
        LongPressContainer(
            isEnabled = isEnabled,
            onLongPress = {
                offset = it
                isDropdownExpanded.value = true
            }
        ) {
            content()
        }
        // maybe it will be more optimized
        if (isDropdownExpanded.value) {
            DropdownMenu(
                isEnabled,
                onDismissRequest = {
                    isDropdownExpanded.value = false
                },
                offset = offset,
                shape = MaterialTheme.shapes.large
            ) {
                dropdownContent(isDropdownExpanded)
            }
        }
    }
}