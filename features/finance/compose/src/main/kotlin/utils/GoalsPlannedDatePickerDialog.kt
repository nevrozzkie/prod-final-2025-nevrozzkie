package utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goals.GoalsComponent
import goals.GoalsStore
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.plus
import parseToLocalDateSafely
import rusFormat
import toLocalDate
import toTimestamp
import kotlin.math.max


// Plus day cuz it works like <, not like ≤
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsPlannedDatePicker(
    model: GoalsStore.State,
    component: GoalsComponent,
) {
    val editingGoal = model.editingGoal
    if (editingGoal != null && model.isPlannedDatePickerShowing) {
        val alreadyPickedDate = model.editingGoalPlannedDate.parseToLocalDateSafely(rusFormat)
        val minSelectableYear = model.today.year
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (alreadyPickedDate != null && alreadyPickedDate.year >= minSelectableYear)
                alreadyPickedDate.plus(DatePeriod(days = 1)).toTimestamp() else null,
            selectableDates = getSelectableDates(model.today),
            yearRange = IntRange(minSelectableYear, max(model.today.year + 50, alreadyPickedDate?.year ?: 0))
        )

        DatePickerDialog(
            onDismissRequest = {
                component.onDispatch(
                    GoalsStore.Message.PlannedDatePickerShowingChanged(false)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (datePickerState.selectedDateMillis != null) {
                            val date = datePickerState.selectedDateMillis?.toLocalDate()
                            date?.let {
                                component.onEvent(
                                    GoalsStore.Intent.ChangeEditGoalDate(
                                        date.format(rusFormat)
                                    )
                                )
                            }
                        }
                    }
                ) {
                    Text("Ок")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        component.onDispatch(
                            GoalsStore.Message.PlannedDatePickerShowingChanged(
                                false
                            )
                        )
                    }
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = {
                    Text(
                        "Выберите дату",
                        modifier = Modifier.padding(
                            top = 15.dp,
                            start = 20.dp
                        )
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
private fun getSelectableDates(today: LocalDate): SelectableDates {
    return object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val date = (today.plus(DatePeriod(days = 1))).toTimestamp()
            return utcTimeMillis >= date
        }

        override fun isSelectableYear(year: Int): Boolean {
            return year >= today.year
        }
    }
}