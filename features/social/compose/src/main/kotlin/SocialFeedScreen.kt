import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import base.CTopAppBar
import base.LazyColumnWithTopShadow
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import managePost.ManagePostComponent
import managePost.allPostsTags
import socialFeed.SocialFeedComponent
import socialFeed.SocialFeedItemContent
import view.theme.Paddings

@Composable
fun SocialFeedScreen(component: SocialFeedComponent) {
    val model by component.model.subscribeAsState()


    Scaffold(
        topBar = {
            CTopAppBar(
                title = "Лента",
                action = {
                    IconButton(
                        onClick = {
                            component.onOutput(
                                SocialFeedComponent.Output.NavigateToManagePost(
                                    post = null
                                )
                            )
                        }
                    ) {
                        Icon(Icons.Rounded.BookmarkAdd, null)
                    }
                },
            ) { }
        }
    ) { paddings ->
        if (model.posts.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        component.onOutput(
                            SocialFeedComponent.Output.NavigateToManagePost(
                                post = null
                            )
                        )
                    }
                ) {
                    Text("Создать первый пост")
                }
            }
        } else {
            LazyColumnWithTopShadow(
                topPadding = paddings.calculateTopPadding(),
                contentPadding = PaddingValues(horizontal = Paddings.hMainContainer)
            ) {
                item {
                    Spacer(Modifier.height(Paddings.large))
                }
                items(model.posts.reversed(), key = { "feedItemN${it.id}" }) { item ->
                    Spacer(Modifier.height(Paddings.semiLarge))
                    SocialFeedItemContent(
                        id = item.id,
                        images = item.images.mapNotNull { it.bitmap?.asImageBitmap() },
                        text = item.text,
                        tags = item.tags,
                        newsData = item.newsData,
                        component = component,
                        allTags = allPostsTags,
                        date = item.creationDate,
                        time = item.creationTime,
                        isEdited = item.edited,
                        isFavourite = item.isFavourite
                    )
                }
            }
        }
    }
}