package goals

import Goal
import com.arkivanov.mvikotlin.core.store.Store

import goals.GoalsStore.Intent
import goals.GoalsStore.Label
import goals.GoalsStore.State
import kotlinx.datetime.LocalDate
import utils.getCurrentLocalDateTime

interface GoalsStore : Store<Intent, State, Label> {
    data class State(
        val maxId: Long = 0L,


        val editingGoal: Goal? = null,

        val isEditingGoalNameValid: Boolean = false,
        val isEditingGoalAmountValid: Boolean = false,
        val isEditingGoalPlannedDateValid: Boolean = true,


        val editingGoalPlannedDate: String = "",
        val isPlannedDatePickerShowing: Boolean = false,

        val today: LocalDate = getCurrentLocalDateTime().date
    )

    sealed interface Intent {

        data object CancelCreatingOrEditing : Intent

        data object PerformCreateOrEdit : Intent

        data class StartCreatingOrEditing(
            val id: Long,
            val name: String,
            val amount: Long,
            val createdDate: LocalDate?,
            val plannedDate: LocalDate?,
        ) : Intent

        data class ChangeEditGoalName(val name: String) : Intent
        data class ChangeEditGoalAmount(val amount: String) : Intent
        data class ChangeEditGoalDate(val date: String) : Intent
    }

    sealed interface Message {
        data class EditingGoalChanged(
            val goal: Goal?
        ) : Message

        data class PlannedDatePickerShowingChanged(val isShowing: Boolean) : Message
        data class EditingGoalPlannedDateChanged(val date: String) : Message

        data class MaxIdChanged(
            val maxId: Long
        ) : Message
    }

    sealed interface Label {
        data object ScrollToCreatedGoal : Label
    }

}
