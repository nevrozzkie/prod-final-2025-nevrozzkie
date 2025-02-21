package managePost

import AnimateItem
import AnimatedBox
import AnimatedSmoothTransition
import CategoryHeader
import DropdownMenuContainer
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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