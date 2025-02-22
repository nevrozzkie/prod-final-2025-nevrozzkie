package managePost

import com.arkivanov.mvikotlin.core.store.Reducer
import managePost.ManagePostStore.State
import managePost.ManagePostStore.Message

object ManagePostReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.TagsChanged -> copy(
                pickedTags = msg.tags,
                isReady = isManagePostReadyToSaveCheck(
                    text = text,
                    tags = msg.tags
                )
            )
            is Message.ImagesChanged -> copy(images = msg.images)
            is Message.TextChanged -> copy(
                text = msg.text,
                isReady = isManagePostReadyToSaveCheck(
                    text = msg.text,
                    tags = pickedTags
                )
            )

            Message.UnbindNews -> copy(
                newsData = null
            )
        }
    }

}

fun isManagePostReadyToSaveCheck(text: String, tags: List<Int>) = text.isNotBlank() && tags.isNotEmpty()