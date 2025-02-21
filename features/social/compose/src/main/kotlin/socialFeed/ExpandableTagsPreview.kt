package socialFeed

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import common.TagItem
import common.TagsGrid
import view.theme.Paddings

@Composable
fun ExpandableTagsPreview(
    tags: List<Int>,
    allTags: Map<Int, String>
) {

    var isExpandedTags by remember { mutableStateOf(false) }

    Crossfade(
        isExpandedTags || tags.size <= 3,
        label = "ExpandTagsAnimation",
        modifier = Modifier.animateContentSize()
    ) { isExpandedTagsAnimated ->
        if (isExpandedTagsAnimated) {
            TagsGrid {
                items(tags, key = { it }) { tag ->
                    TagItem(
                        selected = false,
                        allTags[tag] ?: "???",
                        verticalPadding = Paddings.verySmall
                    ) {
                        isExpandedTags = false
                    }
                }
            }
        } else {
            Row(
                Modifier
                    .padding(end = Paddings.semiMedium)
                    .clickable {
                        isExpandedTags = true
                    }.clip(MaterialTheme.shapes.large),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Paddings.semiMedium,
                    alignment = Alignment.CenterHorizontally)
            ) {
                tags.subList(0, 2).forEach { tag ->
                    TagItem(
                        modifier = Modifier
                            .weight(.33f),
//                                    .padding(end = Paddings.semiMedium),
                        selected = false,
                        name = allTags[tag] ?: "???",
                        verticalPadding = Paddings.verySmall
                    ) { isExpandedTags = true }
                }
                Spacer(Modifier.width(Paddings.small))
                Text(
                    "Все теги",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}