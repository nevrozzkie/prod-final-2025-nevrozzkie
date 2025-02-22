package managePost

import wrapContainers.AnimateColumnItem
import wrapContainers.AnimatedSmoothTransition
import widgets.CategoryHeaderWithIconButton
import widgets.DefaultSmallCloseButton
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import base.TonalCard
import view.theme.Paddings

@Composable
fun ImagesContent(
    bitmaps: List<Bitmap>,
    component: ManagePostComponent,
    onAddImageButtonClicked: () -> Unit
) {
    val images = bitmaps.map {
        val image = it.asImageBitmap()
        image.prepareToDraw()
        image
    }

    AnimatedSmoothTransition(images.isNotEmpty()) {
        CategoryHeaderWithIconButton(
            "Изображения",
            paddingValues =
            PaddingValues(start = Paddings.small, bottom = 0.dp, top = Paddings.medium),
            icon = Icons.Rounded.Add
        ) {
            onAddImageButtonClicked()
        }
    }

    LazyColumn(
        Modifier.heightIn(max = 10_000.dp)
    ) {
        itemsIndexed(images, key = { _, image -> image.asAndroidBitmap().generationId }) { index, image ->
            AnimateColumnItem {
                Spacer(Modifier.height(Paddings.medium))
                TonalCard(modifier = Modifier.aspectRatio(1.5f)) {
                    Box(Modifier.fillMaxSize()) {
                        Image(
                            image, null,
                            modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.large),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(10.dp))
                        DefaultSmallCloseButton {
                            component.onEvent(ManagePostStore.Intent.DeleteImage(index))
                        }
                    }
                }
            }
        }
    }
}