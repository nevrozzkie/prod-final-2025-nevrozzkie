package socialFeed

import Post
import com.arkivanov.mvikotlin.core.store.Store
import managePost.ManagePostStore
import socialFeed.SocialFeedStore.Intent
import socialFeed.SocialFeedStore.Label
import socialFeed.SocialFeedStore.State

interface SocialFeedStore : Store<Intent, State, Label> {
    data class State(
        val posts: List<Post> = emptyList()
    )

    sealed interface Intent

    sealed interface Message {
        data class PostsUpdated(val posts: List<Post>) : Message
    }

    sealed interface Label

}
