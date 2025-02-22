package widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import view.theme.Paddings

@Composable
fun CategoryHeader(
    title: String,
    paddingValues: PaddingValues = PaddingValues(start = Paddings.small, bottom = Paddings.small, top = Paddings.medium),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    endContent: @Composable () -> Unit
) {
    Row(Modifier.fillMaxWidth().padding(paddingValues), verticalAlignment = verticalAlignment, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            title,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineMedium
        )
        endContent()
    }
}

@Composable
fun CategoryHeaderWithIconButton(
    title: String,
    paddingValues: PaddingValues = PaddingValues(start = Paddings.small, bottom = Paddings.small, top = Paddings.medium),
    icon: ImageVector = Icons.Rounded.Add,
    onClick: () -> Unit,
) {
    CategoryHeader(title, paddingValues) {
        IconButton(onClick = onClick) {
            Icon(icon, null)
        }
    }
}