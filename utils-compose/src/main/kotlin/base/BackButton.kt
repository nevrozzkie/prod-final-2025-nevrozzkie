package base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(Icons.Rounded.ArrowBackIosNew, null)
    }
}