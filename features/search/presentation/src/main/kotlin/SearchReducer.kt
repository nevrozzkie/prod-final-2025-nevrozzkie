
import com.arkivanov.mvikotlin.core.store.Reducer
import SearchStore.State
import SearchStore.Message

object SearchReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.QueryChanged -> copy(query = msg.query)
            is Message.NewsUpdated -> copy(news = msg.news)
        }
    }
}