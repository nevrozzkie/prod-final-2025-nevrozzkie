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
import androidx.compose.ui.text.style.TextOverflow
import base.TonalCard
import main.MainComponent
import main.MainStore
import view.theme.Paddings


fun LazyListScope.newsItemsContent(
    mainModel: MainStore.State,
    component: MainComponent,
    networkModel: NetworkStateManager.NetworkModel,
) {
    item {
        AnimatedVerticalColumn(networkModel.isError) {
            Spacer(Modifier.height(Paddings.large))
            ErrorCard(networkModel = networkModel)
        }
    }

    if (mainModel.news.isNotEmpty()) {
        items(mainModel.news, key = { it.id }) {
            AnimateColumnItem {
                NewsItemContent(it) {
                    component.onOutput(
                        MainComponent.Output.NavigateToNewsSite(
                            url = it.url,
                            id = it.id,
                            title = it.title,
                            image = it.imageBitmap?.toByteArray()
                        )
                    )
                }
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
    newsItem: NewsItem,
    onClick: () -> Unit
) {
    val image =
        newsItem.imageBitmap?.asImageBitmap()
    image?.prepareToDraw()




    val imagePlaceholderShades = listOf(
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.9f),
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.9f)
    )

    Spacer(Modifier.height(Paddings.large))
    TonalCard(
        modifier = Modifier.padding(
            horizontal = Paddings.hMainContainer
        ),
        onClick = onClick
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

            CompositionLocalProvider(
                LocalTextStyle provides bottomInfoTextStyle
            ) {
                RightImportantLayout(
                    modifier = Modifier.fillMaxWidth(),
                    leftSide = {
                        Text(
                            buildAnnotatedString {
                                if (newsItem.source.isNotEmpty()) append(newsItem.source)
                                if (newsItem.source.isNotEmpty() && !newsItem.geo.isNullOrEmpty()) append(
                                    " · "
                                )
                                if (!newsItem.geo.isNullOrEmpty()) append(newsItem.geo)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    rightSide = {
                        Text(
                            newsItem.date.format(rusFormat)
                        )
                    }
                )
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