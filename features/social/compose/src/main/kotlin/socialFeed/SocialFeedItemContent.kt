package socialFeed

import PostNewsData
import ExpandableTextWithCustomOverflow
import RightImportantLayout
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import base.CBasicTextFieldDefaults
import base.TonalCard
import bottomInfoTextStyle
import common.PinnedNewsContent
import common.TagItem
import common.TagsGrid
import view.theme.Paddings

@Composable
fun SocialFeedItemContent(
    images: List<ImageBitmap>,
    text: String,
    tags: List<Int>,
    newsData: PostNewsData?,
    allTags: Map<Int, String>,
    date: String,
    time: String,
    isEdited: Boolean,
    component: SocialFeedComponent
) {

    TonalCard(
        Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(Paddings.medium)) {
            // images
            ExpandableTextWithCustomOverflow(
                text,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.padding(Paddings.verySmall))
            ExpandableTagsPreview(
                tags = tags,
                allTags = allTags
            )

            if (newsData != null) {
                Spacer(Modifier.padding(Paddings.verySmall))
                PinnedNewsContent(
                    backgroundColor = MaterialTheme.colorScheme.background,
                    trailingIconColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    newsData = newsData
                ) {
                    component.onOutput(SocialFeedComponent.Output.NavigateToNewsSite(newsData))
                }
                Spacer(Modifier.height(Paddings.semiMedium))
            }

            CompositionLocalProvider(
                LocalTextStyle provides bottomInfoTextStyle
            ) {
                RightImportantLayout(
                    leftSide = {
                        if (isEdited) {
                            Text(
                                "Изменено",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                ) {
                    Text(
                        "$date в $time",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

    }
}