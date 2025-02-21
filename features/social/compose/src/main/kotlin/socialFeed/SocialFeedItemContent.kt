package socialFeed

import DropdownMenuOnLongPressContainer
import PostNewsData
import ExpandableTextWithCustomOverflow
import RightImportantLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import base.TonalCard
import bottomInfoTextStyle
import common.PinnedNewsContent
import view.theme.Paddings

@Composable
fun SocialFeedItemContent(
    id: Long,
    images: List<ImageBitmap>,
    text: String,
    tags: List<Int>,
    newsData: PostNewsData?,
    allTags: Map<Int, String>,
    date: String,
    time: String,
    isEdited: Boolean,
    isFavourite: Boolean,
    component: SocialFeedComponent
) {

    val isTextExpanded = remember { mutableStateOf(false) }


    DropdownMenuOnLongPressContainer(
        isEnabled = true,
        onTap = { isTextExpanded.value = !isTextExpanded.value },
        dropdownContent = { isDropdownExpanded ->
            DropdownMenuItem(
                text = {
                    Text("Сохранить")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Favorite, null)
                },
                onClick = {
                    component.onEvent(SocialFeedStore.Intent.ClickOnFavouriteButton(id))
                    isDropdownExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text("Редактировать")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Edit, null)
                },
                onClick = {
                    isDropdownExpanded.value = false
                }
            )

        }
    ) {
        TonalCard(
            Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(Paddings.medium)) {
                // images
                ExpandableTextWithCustomOverflow(
                    isExpanded = isTextExpanded,
                    text = text,
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
                            if (isFavourite) {
                                Icon(Icons.Rounded.Favorite, null)
                            }
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
}