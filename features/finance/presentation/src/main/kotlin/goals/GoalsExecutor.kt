package goals

import FinanceRepository
import Goal
import decompose.DefaultCoroutineExecutor
import goals.GoalsStore.Intent
import goals.GoalsStore.Label
import goals.GoalsStore.State
import goals.GoalsStore.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.format
import utils.formatWithoutDotsToDate
import utils.parseToLocalDateSafely
import utils.rusFormat


class GoalsExecutor(
    private val financeRepository: FinanceRepository,
    private val goalsFlow: Flow<List<Goal>>

) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeAction(action: Unit) {
        updateMaxId()
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeEditGoalAmount -> editGoalAmount(intent.amount)

            is Intent.ChangeEditGoalDate -> editGoalDate(formatWithoutDotsToDate(intent.date))
            is Intent.ChangeEditGoalName -> {
                val goal =
                    state().editingGoal?.copy(name = intent.name)
                dispatch(Message.EditingGoalChanged(goal))
            }

            Intent.PerformCreateOrEdit -> createOrEdit()

            is Intent.StartCreatingOrEditing -> intent.startCreatingOrEditing()
            Intent.CancelCreatingOrEditing -> dispatch(Message.EditingGoalChanged(null))
        }
    }

    private fun updateMaxId() {
        scope.launch {
            goalsFlow.collectLatest { goals ->
                dispatch(Message.MaxIdChanged(goals.maxOfOrNull { it.id } ?: 0L))
            }
        }
    }

    private fun createOrEdit() {
        if (state().isEditingGoalAmountValid && state().isEditingGoalPlannedDateValid && state().isEditingGoalNameValid) {
            scope.launch {
                state().editingGoal?.let { goal ->
                    dispatch(Message.EditingGoalChanged(null))
                    financeRepository.upsertGoal(
                        goal.copy(
                            plannedDate = state().editingGoalPlannedDate.parseToLocalDateSafely(
                                rusFormat
                            )
                        )
                    )
                }
            }
        }
    }

    private fun Intent.StartCreatingOrEditing.startCreatingOrEditing() {
        dispatch(
            Message.EditingGoalChanged(
                Goal(
                    id = this.id,
                    name = this.name,
                    targetAmount = this.amount,
                    createdDate = this.createdDate ?: state().today,
                    plannedDate = null,
                    completedDate = null,
                    isEditing = true,
                    savedAmount = 0
                )
            )
        )
        dispatch(
            Message.EditingGoalPlannedDateChanged(
                this.plannedDate?.format(rusFormat) ?: ""
            )
        )
        publish(Label.ScrollToCreatedGoal)
    }

    private fun editGoalDate(date: String) {
        //                                         rus
        val dateText = date.replace("ю", ".").replace(Regex("[^0-9.]+"), "")
        if (dateText.length <= 10) {
            dispatch(
                Message.EditingGoalPlannedDateChanged(dateText)
            )
            dispatch(
                Message.PlannedDatePickerShowingChanged(false)
            )
        }
    }

    private fun editGoalAmount(notFormattedText: String) {
        val regex = Regex("\\d{0,12}$")
        if (notFormattedText.matches(regex) || notFormattedText.isEmpty()) {
            dispatch(
                GoalsStore.Message.EditingGoalChanged(
                    state().editingGoal?.copy(targetAmount = notFormattedText.toLongOrNull() ?: 0)
                )
            )
        }
    }
}
