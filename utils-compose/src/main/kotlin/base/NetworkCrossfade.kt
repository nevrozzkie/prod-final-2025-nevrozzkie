package base

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import decompose.NetworkState
import decompose.NetworkStateManager

@Composable
fun NetworkCrossfade(
    networkModel: NetworkStateManager.NetworkModel,
    label: String,
    modifier: Modifier = Modifier,
    animationDuration: Int = 300,
    isFullScreen: Boolean = true,
    errorBlock: @Composable () -> Unit = {},
    loadingBlock: (@Composable () -> Unit)? = null,
    noneBlock: @Composable () -> Unit
) {
    Crossfade(networkModel.state, animationSpec = tween(animationDuration), label = label) { state ->
        Box(modifier, contentAlignment = Alignment.Center) {
            when (state) {
                NetworkState.Error -> errorBlock()
                NetworkState.Loading -> if (loadingBlock != null) loadingBlock() else {
                    if (isFullScreen) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                NetworkState.None -> noneBlock()
            }
        }
    }
}