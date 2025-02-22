package base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation

data object CBasicTextFieldDefaults {
    val placeholderColor
        @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Medium,
    placeholderText: String,
    placeholderAlignment: Alignment = Alignment.CenterStart,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {



    BasicTextField(
        value = value,
        onValueChange = { text ->
            onValueChange(text)
        },
        modifier = modifier,
        textStyle = textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = fontWeight
        ),
        decorationBox = { innerTextField ->
            Box() {
                if (value.isEmpty()) {
                    Text(
                        text = placeholderText,
                        style = textStyle,
                        fontWeight = fontWeight,
                        color = placeholderColor,
                        maxLines = 1,
                        modifier = Modifier.align(placeholderAlignment)
                    )
                }
                innerTextField()
            }
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}