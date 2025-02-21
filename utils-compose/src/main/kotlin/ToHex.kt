import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle

val bottomInfoTextStyle: TextStyle
    @Composable get() {
        return MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),

        )
    }

fun Color.toHex(): String {
    val bBuf = this.toArgb().toUInt().toString(16)
    return "#" + bBuf.substring(bBuf.length - 6)
}