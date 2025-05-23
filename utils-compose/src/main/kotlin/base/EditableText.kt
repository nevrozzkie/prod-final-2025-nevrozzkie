package base

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun EditableText(
    value: String,
    onValueChange: (String) -> Unit,
    valueIfText: AnnotatedString = buildAnnotatedString { append(value) },
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Medium,
    placeholderText: String,
    placeholderAlignment: Alignment = Alignment.CenterStart,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isTextField: Boolean = true
) {
    if (isTextField) {
        CBasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            textStyle = textStyle,
            fontWeight = fontWeight,
            placeholderText = placeholderText,
            placeholderColor = placeholderColor,
            placeholderAlignment = placeholderAlignment,
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions
        )
    } else {
        BasicText(
            text = valueIfText,
            modifier = modifier,
            style = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = fontWeight
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,

            )
    }
}