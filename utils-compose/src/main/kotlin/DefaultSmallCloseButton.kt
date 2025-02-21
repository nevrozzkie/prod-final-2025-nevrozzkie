import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import base.CBasicTextFieldDefaults

@Composable
fun BoxScope.DefaultSmallCloseButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .size(18.dp)
            .align(Alignment.TopEnd)
    ) {
        Icon(
            Icons.Rounded.Close,
            null,
            tint = CBasicTextFieldDefaults.placeholderColor
        )
    }
}