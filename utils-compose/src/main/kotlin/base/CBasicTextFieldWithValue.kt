package base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.launch


/*
Это так называемый "костыль" из-за всеобъемлющих возможностей Jetpack Compose!
Проблема: курсор уезжает за клавиатуру, а экран не следует за ним
Решение: отслеживать положение курсора и использовать bringToView
Проблема2: чтобы отслеживать положение курсора нужно использовать TextFieldValue
Проблема2 -> Проблема3: текст в ui перестаёт быть связан с state (точнее state больше не влияет на ui)
Решение(2,3): забить, но иметь это в виду
Жить можно, т. к. используется только 1 раз, но нужно придумать что-нибудь по-лучше, т.к. этот вариант без валидации
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CBasicTextFieldWithValue(
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
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val textState = remember { mutableStateOf(TextFieldValue(value)) }



    BasicTextField(
        value = textState.value,
        onValueChange = { text ->
            onValueChange(text.text)
            textState.value = text
        },
        onTextLayout = {
//             https://stackoverflow.com/questions/78132849/how-to-keep-cursor-in-view-in-jetpack-compose-textfield
            val cursorRect = it.getCursorRect(textState.value.selection.end)
            coroutineScope.launch {
                bringIntoViewRequester.bringIntoView(cursorRect.copy(bottom = cursorRect.bottom
                        // for some padding between keyboard and cursor
                        +140f))
            }

        },
        modifier = modifier.bringIntoViewRequester(bringIntoViewRequester),
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
        singleLine = false,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}