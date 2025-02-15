package socialFeed

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.DefaultCoroutineExecutor
import socialFeed.SocialFeedStore.Intent
import socialFeed.SocialFeedStore.Label
import socialFeed.SocialFeedStore.State
import socialFeed.SocialFeedStore.Message

class SocialFeedExecutor : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            else -> TODO()
        }
    }
}
