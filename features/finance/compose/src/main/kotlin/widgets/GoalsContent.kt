package widgets

import AnimateColumnItem
import CategoryHeaderWithIconButton
import DefaultSmallCloseButton
import DropdownMenuOnLongPressContainer
import Goal
import RightImportantLayout
import SaveTextButton
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import base.CBasicTextFieldDefaults
import base.EditableText
import base.TonalCard
import formatLikeAmount
import goals.GoalsComponent
import goals.GoalsStore
import utils.EnterAndDisplayMoneyLayout
import utils.progressWithColor
import view.theme.Paddings
import utils.visualTransformations.DateVisualTransformation
import kotlin.math.roundToInt

fun LazyListScope.goalsContent(
    model: GoalsStore.State,
    activeGoals: List<Goal>,
    component: GoalsComponent
) {
    item {
        AnimateColumnItem {
            CategoryHeaderWithIconButton("Цели") {
                component.onEvent(
                    GoalsStore.Intent.StartCreatingOrEditing(
                        id = model.maxId + 1,
                        name = "",
                        amount = 0,
                        createdDate = null,
                        plannedDate = null
                    )
                )
            }
        }
    }
    val creatingItem = listOfNotNull(
        if (model.editingGoal != null
            && model.editingGoal!!.id > model.maxId
        ) model.editingGoal else null
    )
    itemsIndexed(
        activeGoals + creatingItem,
        key = { _, item -> "goal${item.id}${item.isEditing}" }) { index, item ->
        AnimateColumnItem {
            if (index != 0) {
                Spacer(Modifier.height(Paddings.medium))
            }
            GoalCard(
                model = model,
                originalGoal = item,
                component = component
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GoalCard(
    originalGoal: Goal,
    model: GoalsStore.State,
    component: GoalsComponent
) {
    val editingGoal = model.editingGoal
    val isEditable = editingGoal?.id == originalGoal.id
    val goal = if (isEditable) editingGoal!! else originalGoal


    val (progress, progressPercent, animatedColor) =  progressWithColor(goal.savedAmount, goal.targetAmount)
    val isSaveButtonEnabled = isEditable
            && model.isEditingGoalPlannedDateValid
            && model.isEditingGoalAmountValid
            && model.isEditingGoalNameValid




    DropdownMenuOnLongPressContainer(
        !isEditable,
        dropdownContent = { isDropdownExpanded ->
            DropdownMenuItem(
                text = {
                    Text("Отменить")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Close, null)
                },
                onClick = {
                    isDropdownExpanded.value = false
                }
            )


            DropdownMenuItem(
                text = {
                    Text("Редактировать")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Edit, null)
                },
                onClick = {
                    component.onEvent(
                        GoalsStore.Intent.StartCreatingOrEditing(
                            id = goal.id,
                            name = goal.name,
                            amount = goal.targetAmount,
                            createdDate = goal.createdDate,
                            plannedDate = goal.plannedDate
                        )
                    )
                    isDropdownExpanded.value = false
                }
            )


            if (progress >= 1) {
                DropdownMenuItem(
                    text = {
                        Text("Выполнить")
                    },
                    leadingIcon = {
                        Icon(Icons.Rounded.Done, null)
                    },
                    onClick = {
                        isDropdownExpanded.value = false
                    }
                )
            } else {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Осталось накопить ${
                                (goal.targetAmount - goal.savedAmount)
                                    .formatLikeAmount()
                            } ₽"
                        )
                    },
                    enabled = false,
                    onClick = {}
                )
            }
        }
    ) {

        TonalCard() {
            Crossfade(
                isEditable,
                modifier = Modifier.animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                label = "goalCardAnimation"
            ) { isEditMode ->
                Column(
                    Modifier
                        .padding(horizontal = Paddings.large)
                        .padding(top = Paddings.large)
                        .padding(bottom = if (isEditMode) Paddings.small else Paddings.large)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        EnterAndDisplayMoneyLayout(
                            leftSide = {
                                EditableText(
                                    value = goal.name,
                                    onValueChange = { name ->
                                        component.onEvent(GoalsStore.Intent.ChangeEditGoalName(name))
                                    },
                                    placeholderText = "Название цели",
                                    textStyle = MaterialTheme.typography.titleMedium.copy(),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .animateContentSize()
                                        .weight(1f, false),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    isTextField = isEditMode
                                )
                            },
                            separator = {
                                if (isEditMode) {
                                    Text(
                                        text = "за",
                                        fontWeight = FontWeight.Medium,
                                        color = CBasicTextFieldDefaults.placeholderColor,
                                        modifier = Modifier.padding(horizontal = Paddings.medium)
                                    )
                                }
                            },
                            value = if (goal.targetAmount != 0L)
                                goal.targetAmount.toString() else "",
                            valueIfText = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                    append("за")
                                }
                                append(" ${goal.targetAmount.formatLikeAmount()} ₽")
                            },
                            onValueChange = { text ->
                                component.onEvent(GoalsStore.Intent.ChangeEditGoalAmount(text))
                            },
                            placeholderText = "Сколько?",
                            imeAction = ImeAction.Next,
                            isTextField = isEditMode
                        )
//                        EditableText(
//                            textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.End),
//                            modifier = Modifier
//                                .animateContentSize()
//                                .weight(1f, false),
//                            visualTransformation = MoneyOnlyLongVisualTransformation(),
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Number,
//                            ),
//                        )
                    }
                    if (isEditMode) {
                        Spacer(Modifier.height(Paddings.medium))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EditableText(
                                value = model.editingGoalPlannedDate.replace(".", ""),
                                onValueChange = { date ->
                                    component.onEvent(GoalsStore.Intent.ChangeEditGoalDate(date))
                                },
                                placeholderText = "Дата выполнения",
                                textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                                modifier = Modifier,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                visualTransformation = DateVisualTransformation()
                            )
                            Spacer(Modifier.width(Paddings.medium))
                            IconButton(
                                onClick = {
                                    component.onDispatch(
                                        GoalsStore.Message.PlannedDatePickerShowingChanged(
                                            true
                                        )
                                    )
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Rounded.CalendarToday, null)
                            }
                            Spacer(Modifier.weight(1f))
                            SaveTextButton(
                                onClick = {
                                    component.onEvent(GoalsStore.Intent.PerformCreateOrEdit)
                                },
                                disabledContentColor = if (
                                    !model.isEditingGoalPlannedDateValid && model.editingGoalPlannedDate.length == 10 &&
                                    model.isEditingGoalNameValid && model.isEditingGoalAmountValid
                                ) MaterialTheme.colorScheme.error else Color.Unspecified,
                                enabled = isSaveButtonEnabled
                            )
                        }
                    }

                    if (!isEditMode) {
                        RightImportantLayout(
                            leftSide = {
                                LinearProgressIndicator(
                                    progress = { progress },
                                    color = animatedColor,
                                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp),
                                )
                            }
                        ) {

                            Spacer(Modifier.width(Paddings.medium))

                            Text(
                                "${progressPercent.roundToInt()}%",
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
                if (isEditMode) {
                    DefaultSmallCloseButton {
                        component.onEvent(GoalsStore.Intent.CancelCreatingOrEditing)
                    }
                }
            }
        }
    }
}