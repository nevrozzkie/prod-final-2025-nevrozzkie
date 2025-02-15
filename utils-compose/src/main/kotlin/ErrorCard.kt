import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import decompose.NetworkStateManager
import view.theme.Paddings

@Composable
fun ErrorCard(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Paddings.hMainContainer),
    onClick: () -> Unit,
    text: String
) {
    Row(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick()
            }
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Rounded.ErrorOutline, null
        )

        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Paddings.small)
        )

        Icon(
            Icons.Rounded.Sync, null
        )
    }
}

@Composable
fun ErrorCard(
    networkModel: NetworkStateManager.NetworkModel,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Paddings.hMainContainer)
) {
    ErrorCard(modifier = modifier, networkModel.onFixErrorClick, networkModel.errorTitle)
}