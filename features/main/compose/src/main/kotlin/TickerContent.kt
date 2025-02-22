import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import base.NetworkCrossfade
import base.TonalCard
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import decompose.isNotError
import tickers.TickersComponent
import tickers.TickersStore
import utils.formatLikeAmount
import utils.roundTo
import view.LocalViewManager
import view.ThemeTint
import view.theme.Paddings
import view.themeColors
import widgets.ErrorCard
import widgets.shimmerAnimation
import wrapContainers.AnimateRowItem
import wrapContainers.AnimatedVerticalColumn

@Composable
fun TickerContent(
    component: TickersComponent
) {
    val viewManager = LocalViewManager.current

    val model by component.model.subscribeAsState()
    val networkModel by component.networkStateManager.networkModel.subscribeAsState()

    val tickers = model.mainTickers


    Spacer(Modifier.height(Paddings.small))

    AnimatedVerticalColumn(tickers.isEmpty()) {
        Spacer(Modifier.height(Paddings.medium))
    }
    Crossfade(
        networkModel.isNotError, label = "tickerNetworkStateAnimation"
    ) { isNotError ->
        if (isNotError) {
            Column {
                AnimatedVerticalColumn(
                    tickers.isNotEmpty()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Paddings.hMainContainer),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {


                        IconButton(
                            onClick = {
                                val theme = when (viewManager.tint.value) {
                                    ThemeTint.Auto -> ThemeTint.Dark
                                    ThemeTint.Dark -> ThemeTint.Light
                                    ThemeTint.Light -> ThemeTint.Auto
                                }
                                changeTheme(
                                    theme = theme,
                                    viewManager = viewManager,
                                    prefs = component.sharedPreferences
                                )
                            }
                        ) {
                            AnimatedContent(
                                when (viewManager.tint.value) {
                                    ThemeTint.Auto -> Icons.Rounded.AutoAwesome
                                    ThemeTint.Dark -> Icons.Rounded.DarkMode
                                    ThemeTint.Light -> Icons.Rounded.LightMode
                                },
                                label = "animateThemeButton"
                            ) { icon ->
                                Icon(icon, null)
                            }
                        }

                        Spacer(Modifier.weight(1f))


                        NetworkCrossfade(
                            networkModel,
                            modifier = Modifier.size(48.dp),
                            label = "syncTickersCrossfade",
                            isFullScreen = false
                        ) {
                            IconButton(
                                onClick = {
                                    component.onEvent(TickersStore.Intent.FetchMainTickers)
                                },
                            ) {
                                Icon(Icons.Rounded.Sync, null)
                            }
                        }

                        MoneySwitch(model, component)


                    }
                }

                LazyRow(Modifier) {
                    item {
                        Spacer(Modifier.width(Paddings.hMainContainer))
                    }
                    if (tickers.isNotEmpty()) {

                        items(tickers, key = { it.title }) { ticker ->
                            AnimateRowItem {
                                HorizontalTicker(ticker, model.isConverted, modifier = Modifier
                                    .widthIn(min = 200.dp)
                                    .height(100.dp)
                                    .padding(Paddings.medium))
                                Spacer(Modifier.width(Paddings.semiMedium))
                            }
                        }
                    } else {
                        items(model.mainTickersIds.size) {
                            AnimateRowItem {
                                HorizontalTickerPlaceholder()
                                Spacer(Modifier.width(Paddings.semiMedium))
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.width(Paddings.hMainContainer - 10.dp))
                    }
                }
            }
        } else {
            ErrorCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Paddings.hMainContainer)
                    .height(100.dp),
                networkModel = networkModel
            )
        }
    }
}

@Composable
fun MoneySwitch(
    model: TickersStore.State,
    component: TickersComponent
) {
    Switch(
        checked = model.isConverted,
        onCheckedChange = {
            component.onDispatch(TickersStore.Message.IsConvertedChanged(it))
        },
        thumbContent = {
            Text(
                if (model.isConverted) "₽" else "$",
            )
        },
        colors = SwitchDefaults.colors(
            uncheckedThumbColor = MaterialTheme.colorScheme.surface,
            uncheckedBorderColor = MaterialTheme.colorScheme.surfaceContainer,
            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@Composable
private fun HorizontalTickerPlaceholder() {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(
                shimmerAnimation()
            )
            .width(250.dp)
            .height(100.dp)
    ) {
    }
}

@Composable
fun HorizontalTicker(
    ticker: Ticker,
    isConverted: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
) {

    val icon =
        ticker.logoBitmap?.asImageBitmap()
    icon?.prepareToDraw()

    val isDeltaUp = ticker.percentageDelta > 0
    val isDeltaSame = ticker.percentageDelta == 0f
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
    val priceText = "${price.roundTo(2).formatLikeAmount(isConverted)} $currency"


    val deltaText = buildAnnotatedString {
        append(" (")
        if (isDeltaUp) append("+")
        append("${ticker.percentageDelta.roundTo(2)}%)")

    }

    val priceFontWeight = FontWeight.SemiBold
    TonalCard(
        contentColor = priceColor
    ) {
        Row(
            modifier = modifier,
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
