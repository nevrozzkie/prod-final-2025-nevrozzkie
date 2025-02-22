package widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

@Composable
fun ExpandableTextWithCustomOverflow(
    isExpanded: MutableState<Boolean>,
    text: String,
    maxLines: Int = 3,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Medium,
    overflowIndicator: String = "  ещё",
    overflowColor: Color = MaterialTheme.colorScheme.tertiary
) {
    AnimatedContent(
        isExpanded.value,
        transitionSpec = { fadeIn(animationSpec = tween(250))
            .togetherWith(fadeOut(animationSpec = tween(250))) },
        label = "ExpandableTextWithCustomOverflowAnimation"
    ) { isExpandedAnimated ->
        var lastVisibleOffset by remember { mutableStateOf<Int?>(null) }
        Text(
            text = buildAnnotatedString {
                if (lastVisibleOffset != null) {
                    append(text.subSequence(0, lastVisibleOffset!! - overflowIndicator.length))
                    withStyle(SpanStyle(overflowColor, fontWeight = FontWeight.SemiBold)) {
                        append(overflowIndicator)
                        append(text.subSequence(lastVisibleOffset!!, text.length - 1))
                    }
                } else {
                    append(text)
                }
            },
            style = textStyle,
            maxLines = if (isExpandedAnimated) Int.MAX_VALUE else maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                lastVisibleOffset = if (textLayoutResult.hasVisualOverflow) {
                    textLayoutResult.getLineEnd(maxLines - 1, true)
                } else {
                    null
                }
            },
            fontWeight = fontWeight,
            softWrap = true,
            modifier = modifier
        )
    }


}