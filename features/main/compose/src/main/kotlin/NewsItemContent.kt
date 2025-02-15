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
import com.prodfinal2025.nevrozq.R
import decompose.NetworkStateManager
import decompose.isError
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import main.MainStore
import utils.NewsItem
import view.theme.Paddings


fun LazyListScope.newsItemsContent(
    model: MainStore.State,
    networkModel: NetworkStateManager.NetworkModel,
) {
    if (networkModel.isError) {
        item {
            Spacer(Modifier.height(Paddings.large))
            ErrorCard(networkModel = networkModel)
        }
    }

    val newsItems = model.news
    if (newsItems.isNotEmpty()) {
        items(newsItems) {
            NewsItemContent(it)
        }
    } else {
        items(5) {
            NewsItemPlaceholder()
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

    Spacer(Modifier.height(Paddings.large))
    Surface(
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier.padding(
            horizontal = Paddings.hMainContainer
        )
    ) {
        Column(Modifier.padding(Paddings.medium)) {
            Image(
                bitmap = image ?: painterResource(R.drawable.prod_placeholder).toImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(
                        MaterialTheme.shapes.large
                    ),
                contentScale = ContentScale.FillWidth

            )

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