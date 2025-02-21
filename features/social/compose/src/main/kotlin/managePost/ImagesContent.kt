package managePost

import AnimateColumnItem
import AnimatedSmoothTransition
import CategoryHeader
import CategoryHeaderWithIconButton
import DefaultSmallCloseButton
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import base.CBasicTextFieldDefaults
import base.TonalCard
import view.theme.Paddings
import java.io.ByteArrayOutputStream

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