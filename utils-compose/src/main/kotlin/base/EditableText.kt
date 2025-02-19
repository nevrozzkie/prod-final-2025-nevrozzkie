package base

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow

data object CBasicTextFieldDefaults {
    val placeholderColor
        @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
}

@Composable
fun EditableText(
    value: String,
    onValueChange: (String) -> Unit,
    valueIfText: AnnotatedString = buildAnnotatedString { append(value) },
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Medium,
    placeholderText: String,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isTextField: Boolean = true
) {
    val text = TextFieldValue(text = value, selection = TextRange(value.length))

    if (isTextField) {
        BasicTextField(
            value = text,
            onValueChange = { onValueChange(it.text) },
            modifier = modifier,
            textStyle = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = fontWeight
            ),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholderText,
                        style = textStyle,
                        fontWeight = fontWeight,
                        color = placeholderColor
                    )
                }
                innerTextField()
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
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