import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prodfinal2025.nevrozq.features.compose.R.drawable.prod_placeholder
import decompose.NetworkStateManager
import decompose.isError
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import androidx.compose.ui.graphics.Color
import view.theme.Paddings


fun LazyListScope.newsItemsContent(
    newsItems: List<NewsItem>,
    networkModel: NetworkStateManager.NetworkModel,
) {
    item {
        AnimatedVerticalColumn(networkModel.isError) {
            Spacer(Modifier.height(Paddings.large))
            ErrorCard(networkModel = networkModel)
        }
    }

    if (newsItems.isNotEmpty()) {
        items(newsItems, key = { it.id }) {
            AnimateColumnItem {
                NewsItemContent(it)
            }
        }
    } else {
        items(5) {
            AnimateColumnItem {
                NewsItemPlaceholder()
            }
        }
    }
}


@Composable
private fun NewsItemContent(
    newsItem: NewsItem
) {
    val image =
        newsItem.imageBitmap?.asImageBitmap()
    image?.prepareToDraw()


    val containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val contentColor = MaterialTheme.colorScheme.onSurface

    val bottomInfoTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f)
    )

    val imagePlaceholderShades = listOf(
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.9f),
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.9f)
    )

    Spacer(Modifier.height(Paddings.large))
    Surface(
        shape = MaterialTheme.shapes.large,
        contentColor = contentColor,
        modifier = Modifier.padding(
            horizontal = Paddings.hMainContainer
        ),
        color = containerColor
    ) {
        Column(Modifier.padding(Paddings.medium)) {
            Crossfade(newsItem.isImageLoading, label = "imageNewsItemAnimation") { isImageLoading ->
                if (isImageLoading) {
                    NewsItemImagePlaceholder(imagePlaceholderShades)
                } else {
                    Image(
                        bitmap = image ?: painterResource(prod_placeholder).toImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(
                                MaterialTheme.shapes.large
                            ),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }

            Text(
                newsItem.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if (newsItem.desc.isNotEmpty()) {
                Text(newsItem.desc)
                Spacer(Modifier.height(Paddings.semiMedium))
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides bottomInfoTextStyle
                ) {
                    Text(
                        buildAnnotatedString {
                            if (newsItem.source.isNotEmpty()) append(newsItem.source)
                            if (newsItem.source.isNotEmpty() && !newsItem.geo.isNullOrEmpty()) append(
                                " · "
                            )
                            if (!newsItem.geo.isNullOrEmpty()) append(newsItem.geo)
                        }
                    )
                    Text(
                        newsItem.date.format(
                            LocalDate.Format {
                                dayOfMonth()
                                char('.')
                                monthNumber()
                                char('.')
                                year()
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun NewsItemImagePlaceholder(shades: List<Color>) {
    Box(
        modifier = Modifier
            .clip(
                MaterialTheme.shapes.large
            )

            .background(
                shimmerAnimation(shades)
            )
            .fillMaxWidth()
            .height(200.dp)
    ) {
    }
}

@Composable
fun NewsItemPlaceholder() {
    Spacer(Modifier.height(Paddings.large))
    Box(
        modifier = Modifier
            .padding(horizontal = Paddings.hMainContainer)
            .clip(MaterialTheme.shapes.large)
            .background(
                shimmerAnimation()
            )
            .fillMaxWidth()
            .height(500.dp)
    ) {
    }
}