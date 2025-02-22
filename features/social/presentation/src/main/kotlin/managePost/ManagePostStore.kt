package managePost

import ManagePostDTO
import PostNewsData
import android.graphics.Bitmap
import com.arkivanov.mvikotlin.core.store.Store
import managePost.ManagePostStore.Intent
import managePost.ManagePostStore.Label
import managePost.ManagePostStore.State

val ManagePostDTO.Companion.default: ManagePostDTO
    get() {
        return ManagePostDTO(
            id = null,
            images = emptyList(),
            tags = emptyList(),
            creationDate = null,
            creationTime = null,
            newsData = null,
            text = ""
        )
    }

val allPostsTags = listOf(
    "Бизнес", "Финансы", "Политика",
    "Жизнь", "Аксолотли", "Будущее",
    "IT", "PROD", "Т-Банк",
    "Москва", "Крым", "Яхонты",
)
    .mapIndexed { index, s -> index to s }
    .associate { it.first to it.second }

interface ManagePostStore : Store<Intent, State, Label> {
    data class State(
        val allTags: Map<Int, String> = allPostsTags,

        val id: Long?,
        val images: List<Bitmap>,
        val pickedTags: List<Int>,
        val text: String,
        val newsData: PostNewsData?,
        val isReady: Boolean
    )

    sealed interface Intent {
        data object Save : Intent


        data class AddImage(val byteArray: ByteArray) : Intent
        data class DeleteImage(val imageId: Int) : Intent


        data class AddTag(val tag: Int) : Intent
        data class DeleteTag(val tag: Int) : Intent

    }

    sealed interface Message {
        data class ImagesChanged(val images: List<Bitmap>) : Message
        data class TagsChanged(val tags: List<Int>) : Message

        data class TextChanged(val text: String) : Message

        data object UnbindNews : Message
    }

    sealed interface Label

}
