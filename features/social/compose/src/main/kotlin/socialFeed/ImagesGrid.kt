package socialFeed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import view.theme.Paddings


@Composable
fun DefaultImage(image: ImageBitmap, modifier: Modifier) {
    Image(image, null, modifier = modifier.clip(MaterialTheme.shapes.large), contentScale = ContentScale.Crop)
}

@Composable
fun ImagesGrid(images: List<ImageBitmap>, modifier: Modifier = Modifier) {
    if (images.isNotEmpty() && images.size <= 3) {
        Box(modifier) {
            when (images.size) {
                1 -> DefaultImage(images.first(), modifier = Modifier.fillMaxSize())
                2 -> Row(Modifier.fillMaxSize()) {
                    DefaultImage(images.first(), modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(.5f)
                        .padding(end = Paddings.verySmall))
                    DefaultImage(images[1], modifier = Modifier
                        .fillMaxSize()
                        .padding(start = Paddings.verySmall))
                }
                3 -> Column {
                    Row(
                        Modifier
                            .fillMaxHeight(.5f)
                            .padding(bottom = Paddings.small)) {
                        DefaultImage(images[1], modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(.5f)
                            .padding(end = Paddings.verySmall))
                        DefaultImage(images[2], modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(start = Paddings.verySmall))
                    }
                    DefaultImage(images.first(), modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight())
                }
            }
        }
    } else if (images.size > 3) {
        val isEven = images.size % 2 == 0

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().heightIn(max = 10000.dp),
            contentPadding = PaddingValues(Paddings.verySmall)
        ) {
            items(if (isEven) images.size else images.size-1) { index ->
                DefaultImage(
                    images[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(Paddings.verySmall)
                )
            }
        }
        if (!isEven) {
            DefaultImage(
                images[images.size-1],
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(Paddings.verySmall)
            )
        }
    }
}
