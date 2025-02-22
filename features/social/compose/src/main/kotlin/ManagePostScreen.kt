import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Publish
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import base.BackButton
import base.CBasicTextFieldDefaults
import base.CTopAppBar
import base.LazyColumnWithTopShadow
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.PinnedNewsContent
import managePost.ImagesContent
import managePost.ManagePostComponent
import managePost.ManagePostStore
import managePost.TagsContent
import managePost.TextFieldContent
import view.theme.Paddings
import widgets.CategoryHeaderWithIconButton
import wrapContainers.AnimateColumnItem
import wrapContainers.AnimateSlideVertically

@Composable
fun ManagePostScreen(
    component: ManagePostComponent
) {
    val context = LocalContext.current
    //https://stackoverflow.com/questions/76447182/load-image-from-gallery-and-show-it-with-jetpack-compose
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { result ->
        result?.let {
            val item = context.contentResolver.openInputStream(result)
            val bytes = item?.readBytes()
            item?.close()
            bytes?.let {
                component.onEvent(ManagePostStore.Intent.AddImage(bytes))
            }
        }
    }

    ManagePostContent(component = component) {
        launcher.launch("image/*")
    }

}


@Composable
private fun ManagePostContent(
    component: ManagePostComponent,
    onAddImageButtonClicked: () -> Unit
) {

    val model by component.model.subscribeAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CTopAppBar(
                title = if (model.id != null) "Редактировать" else "Создать пост",
                navigation = {
                    BackButton {
                        component.onOutput(ManagePostComponent.Output.NavigateBack)
                    }
                },
                action = {
                    IconButton(
                        onClick = onAddImageButtonClicked
                    ) {
                        Icon(Icons.Rounded.AttachFile, null)
                    }
                },
                horizontalPaddings = Paddings.hTopBarWithNavigation
            ) { }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            AnimateSlideVertically(model.isReady, multiplier = 2) {
                ExtendedFloatingActionButton(
                    text = { Text("Опубликовать") },
                    icon = { Icon(Icons.Rounded.Publish, null) },
                    onClick = {
                        component.onEvent(ManagePostStore.Intent.Save)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    // design
                    modifier = Modifier.offset(y = (15).dp)
                )

            }
        }
    ) { paddings ->
        LazyColumnWithTopShadow(
            Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = Paddings.hMainContainer),
            topPadding = paddings.calculateTopPadding()
        ) {
            item("managePostShadowSpacer") {

                AnimateColumnItem {
                    Spacer(Modifier.height(Paddings.vNoShadow))
                }
            }

            item("textField") {
                // here we can ignore animation cuz always static pos!
                TextFieldContent(
                    text = model.text,
                    component = component
                )

            }

            item("tags") {

                AnimateColumnItem {
                    TagsContent(
                        allTags = model.allTags,
                        pickedTags = model.pickedTags,
                        component = component
                    )
                }
            }
            if (model.newsData != null) {
                item("pinnedNews") {
                    AnimateColumnItem {
                        CategoryHeaderWithIconButton(
                            title = "Прикреплённая новость",
                            icon = Icons.Rounded.Close
                        ) {
                            component.onDispatch(ManagePostStore.Message.UnbindNews)
                        }
                        PinnedNewsContent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Paddings.hMainContainer),
                            backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                            trailingIconColor = CBasicTextFieldDefaults.placeholderColor,
                            newsData = model.newsData!!
                        ) { }
                    }
                }
            }

            item("images") {
                AnimateColumnItem {
                    ImagesContent(
                        model.images,
                        component = component,
                        onAddImageButtonClicked = onAddImageButtonClicked
                    )
                }

            }
        }

    }
}