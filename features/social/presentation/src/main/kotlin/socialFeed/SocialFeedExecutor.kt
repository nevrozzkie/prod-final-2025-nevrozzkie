package socialFeed

import SocialRepository
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.DefaultCoroutineExecutor
import koin.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import socialFeed.SocialFeedStore.Intent
import socialFeed.SocialFeedStore.Label
import socialFeed.SocialFeedStore.State
import socialFeed.SocialFeedStore.Message

class SocialFeedExecutor(
    private val socialRepository: SocialRepository = Inject.instance()
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeAction(action: Unit) {
        super.executeAction(action)
        bindPosts()
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            else -> TODO()
        }
    }

    private fun bindPosts() {
        scope.launch {
            socialRepository.fetchPosts().collectLatest { posts ->
                dispatch(Message.PostsUpdated(posts))
            }
        }
    }
}
