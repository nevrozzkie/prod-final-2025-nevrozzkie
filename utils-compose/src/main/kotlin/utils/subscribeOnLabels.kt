package utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import decompose.DefaultMVIComponent
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("ComposableNaming")
@Composable
fun <Intent : Any, State : Any, Label : Any> DefaultMVIComponent<Intent, State, Label>.subscribeOnLabels(
    action: suspend (Label) -> Unit
) {
    LaunchedEffect(this) {
        labels.collectLatest { label ->
            action(label)
        }
    }
}