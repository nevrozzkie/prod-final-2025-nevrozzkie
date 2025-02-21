package managePost

import com.arkivanov.mvikotlin.core.store.Reducer
import managePost.ManagePostStore.State
import managePost.ManagePostStore.Message

object ManagePostReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.TagsChanged -> copy(
                pickedTags = msg.tags,
                isReady = isReadyCheck(
                    text = text,
                    tags = msg.tags
                )
            )
            is Message.ImagesChanged -> copy(images = msg.images)
            is Message.TextChanged -> copy(
                text = msg.text,
                isReady = isReadyCheck(
                    text = msg.text,
                    tags = pickedTags
                )
            )

            Message.UnbindNews -> copy(
                newsData = null
            )
        }
    }

    // here cuz it's simple
    private fun isReadyCheck(text: String, tags: List<Int>) = text.isNotBlank() && tags.isNotEmpty()
}