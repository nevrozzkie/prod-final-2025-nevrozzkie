package goals

import com.arkivanov.mvikotlin.core.store.Reducer
import goals.GoalsStore.State
import goals.GoalsStore.Message
import isValid
import isValidRuDate

object GoalsReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.EditingGoalChanged -> {
                // here cuz it very simple
                val isNameValid = msg.goal?.name.isValid()
                val isAmountValid = msg.goal?.targetAmount.isValid(false)
                copy(
                    editingGoal = msg.goal,
                    isEditingGoalNameValid = isNameValid,
                    isEditingGoalAmountValid = isAmountValid
                )
            }

            is Message.PlannedDatePickerShowingChanged -> copy(isPlannedDatePickerShowing = msg.isShowing)
            is Message.EditingGoalPlannedDateChanged -> {
                val isDateValid = msg.date.isValidRuDate(today) || msg.date.isEmpty()
                copy(
                    editingGoalPlannedDate = msg.date,
                    isEditingGoalPlannedDateValid = isDateValid
                )
            }

            is Message.MaxIdChanged -> copy(maxId = msg.maxId)
        }
    }
}