package goals

import FinanceRepository
import Goal
import decompose.DefaultCoroutineExecutor
import formatWithoutDotsToDate
import goals.GoalsStore.Intent
import goals.GoalsStore.Label
import goals.GoalsStore.State
import goals.GoalsStore.Message
import koin.Inject
import kotlinx.coroutines.launch
import kotlinx.datetime.format
import parseToLocalDateSafely
import rusFormat

class GoalsExecutor(
    val financeRepository: FinanceRepository = Inject.instance()
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
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

    private fun createOrEdit() {
        if (state().isEditingGoalAmountValid && state().isEditingGoalPlannedDateValid && state().isEditingGoalNameValid) {
            scope.launch {
                state().editingGoal?.let { goal ->
                    dispatch(Message.EditingGoalChanged(null))
                    financeRepository.insertGoal(
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
                    isEditing = true
                )
            )
        )
        dispatch(
            Message.EditingGoalPlannedDateChanged(
                this.plannedDate?.format(rusFormat) ?: ""
            )
        )
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
        val amountText = notFormattedText.replace(Regex("\\D"), "")
        if (amountText.length <= 999_999_999_999.toString().length) {
            val amount = if (amountText.isEmpty()) {
                0
            } else {
                amountText.toLongOrNull()
            }
            amount?.let {
                if (amount >= 0) {
                    dispatch(
                        Message.EditingGoalChanged(
                            state().editingGoal?.copy(targetAmount = amount)
                        )
                    )
                }
            }
        }
    }
}
