import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tickers.TickersStore
import utils.Ticker
import view.LocalThemeColors
import view.theme.Colors
import view.theme.Paddings
import view.themeColors

@Composable
fun TickerContent(
    model: TickersStore.State,
    isConverted: Boolean
) {
    LazyRow(Modifier) {
        item {
            Spacer(Modifier.width(Paddings.hMainContainer))
        }
        items(model.searchTickers.ifEmpty { model.mainTickers }) { ticker ->
            HorizontalTicker(ticker, isConverted)
            Spacer(Modifier.width(10.dp))
        }
        item {
            Spacer(Modifier.width(Paddings.hMainContainer - 10.dp))
        }
    }
}

@Composable
private fun HorizontalTicker(
    ticker: Ticker,
    isConverted: Boolean
) {

    val icon =
        ticker.logo?.asImageBitmap()
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
    val priceText = "${price.roundTo(1)} $currency"


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
            Text(
                buildAnnotatedString {
                    append(priceText)
                    if (!isDeltaSame) {
                        append(" (")
                        if (isDeltaUp) append("+")
                        append("${ticker.percentageDelta.roundTo(2)}%)")
                    }
                },
                fontWeight = FontWeight.SemiBold
            )
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
