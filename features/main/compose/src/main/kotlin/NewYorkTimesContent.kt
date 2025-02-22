
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import base.BackButton
import base.BarShadow
import base.CTopAppBar
import base.NetworkCrossfade
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.prodfinal2025.nevrozq.features.main.compose.R
import decompose.isOk
import newYorkTimes.NewYorkTimesComponent
import newYorkTimes.NewYorkTimesStore
import utils.subscribeOnLabels
import utils.toHex
import view.theme.Paddings
import wrapContainers.AnimateSlideVertically


// https://www.geeksforgeeks.org/webview-in-android-using-jetpack-compose/?ysclid=m7dic6n0ry291296611
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NewsWebView(
    component: NewYorkTimesComponent
) {
    val model by component.model.subscribeAsState()
    val networkModel by component.networkStateManager.networkModel.subscribeAsState()
    val webView = component.webView

    val colors = MaterialTheme.colorScheme


    component.subscribeOnLabels {
        when (it) {
            NewYorkTimesStore.Label.UpdateColorsFromUI -> component.onEvent(
                NewYorkTimesStore.Intent.UpdateColors(
                    backgroundColor = colors.background.toHex(),
                    textColor = colors.onBackground.toHex(),
                    highlightedTextColor = colors.primary.toHex()
                )
            )
        }
    }

    // updateColors! cuz it's (js in webView) not declarative ui =/
    if (model.isBounded) {
        LaunchedEffect(colors) {
            component.onEvent(
                NewYorkTimesStore.Intent.UpdateColors(
                    backgroundColor = colors.background.toHex(),
                    textColor = colors.onBackground.toHex(),
                    highlightedTextColor = colors.primary.toHex()
                )
            )
        }
    }

    Scaffold(
        topBar = {
            CTopAppBar(
                title = "New York Times",
                isCentre = true,
                navigation = {
                    BackButton { component.onOutput(NewYorkTimesComponent.Output.NavigateBack) }
                },
                action = {
                    Icon(
                        painterResource(R.drawable.nyt_t_logo), null,
                        modifier = Modifier.size(48.dp).scale(0.7f),
                    )
                },
                horizontalPaddings = Paddings.hTopBarWithNavigation
            ) {}
        },
        floatingActionButton = {
            AnimateSlideVertically(networkModel.isOk) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        component.onOutput(
                            NewYorkTimesComponent.Output.RepostNews(
                                url = component.initialUrl,
                                id = component.id,
                                icon = component.icon,
                                title = component.title
                            )
                        )
                    }
                ) {
                    Icon(Icons.Rounded.Share, null)
                }
            }
        }
    ) { p ->
        Box(Modifier.padding(top = p.calculateTopPadding())) {
            NetworkCrossfade(
                networkModel = networkModel,
                label = "networkStatesWebViewAnimation",
                animationDuration = 700,
                errorBlock = {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(networkModel.errorTitle)
                        Spacer(Modifier.height(Paddings.medium))
                        Button(
                            onClick = {networkModel.onFixErrorClick()}
                        ) {
                            Text(networkModel.fixText)
                        }
                    }
                }
            ) {
                AndroidView(
                    factory = {
                        webView.apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    update = {}
                )
            }
            BarShadow()
        }
    }
}