package managePost

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import base.CBasicTextField
import base.CBasicTextFieldWithValue
import base.EditableText
import base.TonalCard
import view.theme.Paddings

@Composable
fun TextFieldContent(
    text: String,
    component: ManagePostComponent
) {
    TonalCard {
        CBasicTextFieldWithValue(
            value = text,
            onValueChange = {newText ->
                component.onDispatch(ManagePostStore.Message.TextChanged(newText.trimStart()))
            },
            placeholderText = "В начале было Слово...",
            modifier = Modifier.fillMaxWidth().padding(Paddings.medium)
                .animateContentSize()
        )
    }
}