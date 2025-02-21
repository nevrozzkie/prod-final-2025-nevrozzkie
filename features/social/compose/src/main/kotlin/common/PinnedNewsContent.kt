package common

import CategoryHeader
import CategoryHeaderWithIconButton
import PostNewsData
import RightImportantLayout
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import base.TonalCard
import bitmap
import view.theme.Paddings

@Composable
fun PinnedNewsContent(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    trailingIconColor: Color,
    newsData: PostNewsData,
    onClick: () -> Unit
) {
    val icon = newsData.icon?.bitmap?.asImageBitmap()

    TonalCard(
        containerColor = backgroundColor,
        onClick = onClick
    ) {
        RightImportantLayout(
            leftSide = {
                if (icon != null) {
                    Image(icon, null,
                        modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop,
                        )
                }
                Text(newsData.title, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = Paddings.semiMedium))
            },
            modifier = Modifier.padding(horizontal = Paddings.medium).padding(vertical = Paddings.semiMedium)
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowForwardIos, null, tint = trailingIconColor)
        }
    }
}