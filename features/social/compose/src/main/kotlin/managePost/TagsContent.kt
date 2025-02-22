package managePost

import wrapContainers.AnimateItem
import wrapContainers.AnimatedBox
import widgets.CategoryHeader
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.TagItem
import common.TagsGrid
import view.theme.Paddings

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsContent(
    allTags: Map<Int, String>,
    pickedTags: List<Int>,
    component: ManagePostComponent
) {
    Column {
        CategoryHeader("Тэги",
            paddingValues = PaddingValues(start = Paddings.small, bottom = 0.dp, top = Paddings.medium)
            , verticalAlignment = Alignment.Bottom) {
            AnimatedBox(pickedTags.isEmpty()) {
                Text("Выберите хотя бы один")
            }
        }
        Spacer(Modifier.height(Paddings.medium))

        TagsGrid {
            items(allTags.toList().sortedWith(compareBy( {it.first !in pickedTags}, {pickedTags.indexOf(it.first)} )), key = { it }) { (id, name) ->
                val isPicked = id in pickedTags
                AnimateItem {
                    AnimatedContent(
                        isPicked,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220))
                                .togetherWith(fadeOut(animationSpec = tween(220)))
                        },
                        label = "tagColorAfterPressAnimation"
                    ) { i ->
                        TagItem(
                            selected = i,
                            name = name,
                        ) {
                            component.onEvent(
                                if (isPicked) ManagePostStore.Intent.DeleteTag(id)
                                else ManagePostStore.Intent.AddTag(id)
                            )
                        }
                    }
                }
            }
        }


    }

}