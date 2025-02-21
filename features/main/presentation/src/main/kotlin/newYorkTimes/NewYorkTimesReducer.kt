package newYorkTimes

import com.arkivanov.mvikotlin.core.store.Reducer
import newYorkTimes.NewYorkTimesStore.State
import newYorkTimes.NewYorkTimesStore.Message

object NewYorkTimesReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.ColorsUpdated -> copy(
                backgroundColor = msg.backgroundColor,
                textColor = msg.textColor,
                highlightedTextColor = msg.highlightedTextColor
            )

            is Message.BoundedChanged -> copy(isBounded = msg.isBounded)
        }
    }
}