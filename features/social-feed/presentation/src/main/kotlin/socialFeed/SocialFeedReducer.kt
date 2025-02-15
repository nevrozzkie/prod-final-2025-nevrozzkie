package socialFeed

import com.arkivanov.mvikotlin.core.store.Reducer
import socialFeed.SocialFeedStore.State
import socialFeed.SocialFeedStore.Message

object SocialFeedReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}