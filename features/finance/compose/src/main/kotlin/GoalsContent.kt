import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import base.CBasicTextFieldDefaults
import base.EditableText
import base.TonalCard
import goals.GoalsComponent
import goals.GoalsStore
import kotlinx.coroutines.delay
import view.theme.Paddings
import view.themeColors
import visualTransformations.DateVisualTransformation
import visualTransformations.MoneyOnlyLongVisualTransformation
import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun LazyListScope.goalsContent(
    model: GoalsStore.State,
    maxId: Long,
    activeGoals: List<Goal>,
    completedGoals: List<Goal>,
    transactions: List<Transaction>,
    component: GoalsComponent
) {
    item {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(
                onClick = {
                    component.onEvent(
                        GoalsStore.Intent.StartCreatingOrEditing(
                            id = maxId + 1,
                            name = "",
                            amount = 0,
                            createdDate = null,
                            plannedDate = null
                        )
                    )
                }
            ) {
                Icon(Icons.Rounded.Add, null)
            }
        }
    }
    val creatingItem = listOfNotNull(
        if (model.editingGoal != null
            && model.editingGoal!!.id > maxId
        ) model.editingGoal else null
    )
    itemsIndexed(
        activeGoals + creatingItem,
        key = { _, item -> "${item.id}${item.isEditing}" }) { index, item ->
        AnimateColumnItem {
            if (index != 0) {
                Spacer(Modifier.height(Paddings.medium))
            }
            GoalCard(
                model = model,
                originalGoal = item,
                component = component,
                transactions = transactions.filter { it.toGoalId == item.id }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GoalCard(
    originalGoal: Goal,
    model: GoalsStore.State,
    transactions: List<Transaction>,
    component: GoalsComponent
) {
    val keyboardController =
        LocalSoftwareKeyboardController.current

    val editingGoal = model.editingGoal
    val isEditable = editingGoal?.id == originalGoal.id
    val goal = if (isEditable) editingGoal!! else originalGoal

    var animationsStarted by remember { mutableStateOf(false) }

    val savedAmount = goal.targetAmount / 1.5f //transactions.sumOf { it.amount }
    val progress by animateFloatAsState(
        if (animationsStarted && goal.targetAmount > 0) savedAmount / (goal.targetAmount * 1f) else 0f,
        label = "progressAnimation",
        animationSpec = tween(700, easing = LinearOutSlowInEasing)
    )
    val progressPercent = progress * 100

    val animatedColor = lerp(MaterialTheme.colorScheme.error, Color.Green, progress)

    val isSaveButtonEnabled = isEditable
            && model.isEditingGoalPlannedDateValid
            && model.isEditingGoalAmountValid
            && model.isEditingGoalNameValid

    val focusRequester = remember { FocusRequester() }

    DropdownMenuContainer(
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
                                (goal.targetAmount - savedAmount).roundToLong()
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
                        .padding(bottom = Paddings.large)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
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
                                .weight(1f, false)
                                .then(if (isEditMode) Modifier.focusRequester(focusRequester) else Modifier),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isTextField = isEditMode
                        )
                        if (isEditMode) {
                            AnimatedContent(
                                targetState = if (goal.name.isEmpty()) "и" else "за",
                                label = "AndOrForTextAnimation"
                            ) { text ->
                                Text(
                                    text = text,
                                    fontWeight = FontWeight.Medium,
                                    color = CBasicTextFieldDefaults.placeholderColor,
                                    modifier = Modifier.padding(horizontal = Paddings.small)
                                )
                            }
                        }
                        EditableText(
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
                            placeholderText = "Сколько нужно?",
                            textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.End),
                            modifier = Modifier
                                .animateContentSize()
                                .weight(1f, false),
                            visualTransformation = MoneyOnlyLongVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isTextField = isEditMode
                        )
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
                            TextButton(
                                onClick = {
                                    component.onEvent(GoalsStore.Intent.PerformCreateOrEdit)
                                },
                                contentPadding = Paddings.buttonPaddingValues,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = themeColors.green,
                                    disabledContentColor = if (
                                        !model.isEditingGoalPlannedDateValid && model.editingGoalPlannedDate.length == 10 &&
                                        model.isEditingGoalNameValid && model.isEditingGoalAmountValid
                                    ) MaterialTheme.colorScheme.error else Color.Unspecified
                                ),
                                enabled = isSaveButtonEnabled

                            ) {
                                Text("Сохранить")
                            }
                        }
                    }

                    if (!isEditMode) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LinearProgressIndicator(
                                progress = { progress },
                                color = animatedColor,
                                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                modifier = Modifier
                                    .fillMaxWidth(.87f)
                                    .height(10.dp),
                            )
                            Text(
                                "${progressPercent.roundToInt()}%",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (isEditMode) {
                    IconButton(
                        onClick = {
                            component.onEvent(GoalsStore.Intent.CancelCreatingOrEditing)
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .size(18.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            null,
                            tint = CBasicTextFieldDefaults.placeholderColor
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(isEditable) {
        if (isEditable) focusRequester.requestFocus()
        // magic android delay
        delay(200)
        keyboardController?.show()
    }

    LaunchedEffect(Unit) {
        animationsStarted = true
    }

}