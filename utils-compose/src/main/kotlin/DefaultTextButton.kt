import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import view.theme.Paddings
import view.themeColors

@Composable
fun SaveTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    disabledContentColor: Color = Color.Unspecified,
    enabled: Boolean,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        contentPadding = Paddings.buttonPaddingValues,
        colors = ButtonDefaults.textButtonColors(
            contentColor = themeColors.green,
            containerColor = themeColors.green.copy(alpha = .1f),
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContentColor.copy(alpha = .1f)
        ),
        enabled = enabled

    ) {
        Text("Сохранить", overflow = TextOverflow.Ellipsis, maxLines = 1)
    }

}