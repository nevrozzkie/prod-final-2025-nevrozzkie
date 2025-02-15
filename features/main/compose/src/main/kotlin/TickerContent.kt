import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import base.NetworkCrossfade
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import tickers.TickersComponent
import tickers.TickersStore
import utils.Ticker
import view.theme.Paddings
import view.themeColors

@Composable
fun TickerContent(
    component: TickersComponent
) {
    val model by component.model.subscribeAsState()
    val networkModel by component.networkStateManager.networkModel.subscribeAsState()

    val tickers = model.searchTickers.ifEmpty { model.mainTickers }

    AnimatedVisibility(
        true,
        enter = expandIn(expandFrom = Alignment.BottomStart)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.hMainContainer),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Switch(
                checked = model.isConverted,
                onCheckedChange = {
                    component.onDispatch(TickersStore.Message.IsConvertedChanged(it))
                },
                thumbContent = {
                    Text(
                        if (model.isConverted) "₽" else "$"
                    )
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = MaterialTheme.colorScheme.surfaceContainer,
                    uncheckedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            )

            NetworkCrossfade(
                networkModel,
                modifier = Modifier.size(48.dp),
                label = "syncTickersCrossfade"
            ) {
                IconButton(
                    onClick = {
                        component.onEvent(TickersStore.Intent.FetchMainTickers)
                    },
                ) {
                    Icon(Icons.Rounded.Sync, null)
                }
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = {},
                // cuz we buttonSize = 48.dp iconSize = 24.dp
                modifier = Modifier.offset(x = 12.dp)
            ) {
                Icon(Icons.Rounded.Menu, null)
            }
        }
    }

    LazyRow(Modifier) {
        item {
            Spacer(Modifier.width(Paddings.hMainContainer))
        }
        if (tickers.isNotEmpty()) {
            items(tickers) { ticker ->
                HorizontalTicker(ticker, model.isConverted)
                Spacer(Modifier.width(Paddings.semiMedium))
            }
        } else {
            items(3) {
                HorizontalTickerPlaceholder()
                Spacer(Modifier.width(Paddings.semiMedium))
            }
        }
        item {
            Spacer(Modifier.width(Paddings.hMainContainer - 10.dp))
        }
    }
}

@Composable
private fun HorizontalTickerPlaceholder() {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(
                shimmerAnimation())
            .width(250.dp)
            .height(100.dp)
    ) {
    }
}

@Composable
private fun HorizontalTicker(
    ticker: Ticker,
    isConverted: Boolean
) {

    val icon =
        ticker.logoBitmap?.asImageBitmap()
    icon?.prepareToDraw()

    val isDeltaUp = ticker.percentageDelta > 0
    val isDeltaSame = ticker.percentageDelta == 0f
    val containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val priceColor by animateColorAsState(
        if (isDeltaUp) themeColors.green
        else if (isDeltaSame) Color.Gray
        else MaterialTheme.colorScheme.error,
        label = "tickerContentColorAnimation"
    )

    val deltaArrowRotation by animateFloatAsState(
        if (isDeltaUp) -90f
        else 90f, label = "deltaArrowRotationAnimation"
    )

    val price = if (isConverted) ticker.rubles else ticker.price
    val currency = if (isConverted) "RUB" else ticker.currency
    val priceText = "${price.roundTo(2)} $currency"


    val deltaText = buildAnnotatedString {
        append(" (")
        if (isDeltaUp) append("+")
        append("${ticker.percentageDelta.roundTo(2)}%)")

    }

    val priceFontWeight = FontWeight.SemiBold

    Surface(
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        contentColor = priceColor
    ) {
        Row(
            modifier = Modifier
                .widthIn(min = 200.dp)
                .height(100.dp)
                .padding(Paddings.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Image(
                    icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
            Text(
                text = ticker.title,
                Modifier,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(20.dp))
            Spacer(Modifier.weight(1f))

            AnimatedContent(
                priceText, label = "animatePriceText"
            ) { text ->
                Text(
                    text,
                    fontWeight = priceFontWeight
                )
            }

            if (!isDeltaSame) {
                Text(deltaText, fontWeight = priceFontWeight)
            }

            if (!isDeltaSame) {
                Icon(
                    Icons.Rounded.ArrowForward,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(deltaArrowRotation),
                    contentDescription = null
                )
            }
        }
    }
}
