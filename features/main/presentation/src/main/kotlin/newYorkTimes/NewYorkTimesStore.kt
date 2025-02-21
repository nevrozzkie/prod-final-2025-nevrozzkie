package newYorkTimes

import com.arkivanov.mvikotlin.core.store.Store
import newYorkTimes.NewYorkTimesStore.Intent
import newYorkTimes.NewYorkTimesStore.Label
import newYorkTimes.NewYorkTimesStore.State

interface NewYorkTimesStore : Store<Intent, State, Label> {

    data class State(
        val backgroundColor: String = "#121212",
        val textColor: String = "#ffffff",
        val highlightedTextColor: String = "#ffeb3b",
        val isBounded: Boolean = false
    )

    sealed interface Intent {
        data class UpdateColors(
            val backgroundColor: String,
            val textColor: String,
            val highlightedTextColor: String
        ) : Intent
    }

    sealed interface Message {
        data class ColorsUpdated(
            val backgroundColor: String,
            val textColor: String,
            val highlightedTextColor: String
        ) : Message

        data class BoundedChanged(val isBounded: Boolean) : Message
    }

    sealed interface Label {
        data object UpdateColorsFromUI : Label
    }

}
