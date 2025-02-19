package goals

import Goal
import com.arkivanov.mvikotlin.core.store.Store
import getCurrentLocalDateTime
import goals.GoalsStore.Intent
import goals.GoalsStore.Label
import goals.GoalsStore.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.LocalDate

interface GoalsStore : Store<Intent, State, Label> {

    // Цветочки, но в душе coroutines...
    data class Flowers(
        val activeGoals: Flow<List<Goal>>,
        val completedGoals: Flow<List<Goal>>,
        val maxId: Flow<Long> = combine(activeGoals, completedGoals) { active, completed ->
            (active + completed).maxOfOrNull { it.id } ?: 0L
        },
    )

    data class State(
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

    }

    sealed interface Label

}
