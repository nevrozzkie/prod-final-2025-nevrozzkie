package widgets

import AnimateColumnItem
import AnimatedBox
import AnimatedVerticalColumn
import CategoryHeaderWithIconButton
import DefaultSmallCloseButton
import DropdownMenuContainer
import DropdownMenuOnLongPressContainer
import Goal
import RightImportantLayout
import SaveTextButton
import Transaction
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.zIndex
import base.CBasicTextFieldDefaults
import base.EditableText
import base.TonalCard
import formatLikeAmount
import isValid
import transactions.TransactionsComponent
import transactions.TransactionsStore
import utils.EnterAndDisplayMoneyLayout
import view.theme.Paddings
import view.themeColors

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.transactionsContent(
    model: TransactionsStore.State,
    transactions: List<Transaction>,
    goals: List<Goal>,
    component: TransactionsComponent
) {
    item {
        AnimateColumnItem {
            CategoryHeaderWithIconButton(
                title = "Операции"
            ) {
                component.onEvent(
                    TransactionsStore.Intent.StartCreatingOrEditing(
                        id = model.maxId + 1,
                        fromGoalId = null,
                        toGoalId = null,
                        amount = "",
                        comment = "",
                        createdDate = null,
                        fromGoal = "",
                        toGoal = ""
                    )
                )
            }
        }
    }

    val creatingItem = listOfNotNull(
        if (model.editingTransaction != null
            && model.editingTransaction!!.id > model.maxId
        ) model.editingTransaction else null
    )
    val items = creatingItem + (transactions).reversed()


    itemsIndexed(items,
        key = { _, item -> "transaction${item.id}${item.isEditing}" }) { index, item ->
        val isFirstItemWithDate =
            index == items.indexOfFirst { it.createdDate == item.createdDate }

        val isLastMonthDay =
            index == items.indexOfFirst { it.createdDate.drop(2) == item.createdDate.drop(2) }


        AnimateColumnItem {
            AnimatedVerticalColumn(isFirstItemWithDate) {
                if (index != 0) {
                    Spacer(Modifier.height(Paddings.large))
                }
                RightImportantLayout(
                    leftSide = {
                        Text(
                            text = item.createdDate,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = Paddings.medium),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }
                ) {
                    if (isLastMonthDay) {
                        val monthAmount = remember {
                            derivedStateOf {
                                transactions.filter {
                                    it.createdDate.drop(2) == item.createdDate.drop(
                                        2
                                    ) && it.fromGoalId == null
                                }
                                    .sumOf { it.amount.toLong() ?: 0 }
                            }
                        }
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                                    )
                                ) {
                                    append("за месяц")
                                }
                                append("  ${monthAmount.value.formatLikeAmount()} ₽")
                            },
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            TransactionCard(item, model, goals, component)
        }
    }
}




@Composable
private fun TransactionCard(
    originalTransaction: Transaction,
    model: TransactionsStore.State,
    goals: List<Goal>,
    component: TransactionsComponent
) {
    val editingTransaction = model.editingTransaction
    val isEditable = editingTransaction?.id == originalTransaction.id

    val transaction = if (isEditable) editingTransaction!! else originalTransaction

    val isGoalPicked = transaction.toGoalId != null
    val isAmountValid = transaction.amount.toLongOrNull().isValid(true)

    val isSaveButtonEnabled = isGoalPicked && isAmountValid


    DropdownMenuOnLongPressContainer(
        !isEditable,
        dropdownContent = { isDropdownExpanded ->
            DropdownMenuItem(
                text = {
                    Text("Удалить")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Close, null)
                },
                onClick = {
                    component.onEvent(TransactionsStore.Intent.Delete(transaction))
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
                    with(transaction) {
                        component.onEvent(
                            TransactionsStore.Intent.StartCreatingOrEditing(
                                id = id,
                                fromGoalId = fromGoalId,
                                toGoalId = toGoalId,
                                fromGoal = fromGoal,
                                toGoal = toGoal,
                                amount = amount,
                                comment = comment,
                                createdDate = createdDate
                            )
                        )
                    }
                    isDropdownExpanded.value = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("Повторить")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Repeat, null)
                },
                onClick = {
                    component.onEvent(
                        TransactionsStore.Intent.Repeat(transaction)
                    )
                    isDropdownExpanded.value = false
                }
            )
        }
    ) {
        Box {
            Crossfade(
                isEditable,
                modifier = Modifier.animateContentSize(),
                label = "transactionCardAnimation"
            ) { isEditMode ->


                val isTransfer =
                    (isEditMode && transaction.id > model.maxId) || originalTransaction.fromGoalId != null
                val isMinus = (transaction.amount.toLongOrNull()
                    ?: 0) < 0

                Column(
                    Modifier
                        .padding(horizontal = Paddings.medium)
                        .padding(top = Paddings.large)
                ) {
                    EnterAndDisplayMoneyLayout(
                        leftSide = {
                            AnimatedBox(isTransfer) {
                                GoalPicker(
                                    currentValue = transaction.fromGoal,
                                    currentId = transaction.fromGoalId,
                                    isEditable = isEditMode,
                                    ifEmpty = "...",
                                    prohibitedGoalId = transaction.toGoalId,
                                    goals = goals,
                                    modifier = Modifier.padding(end = Paddings.small)

                                ) {
                                    component.onEvent(
                                        TransactionsStore.Intent.ChangeEditTransactionFromId(
                                            it
                                        )
                                    )
                                }
                            }

                            Icon(
                                when {
                                    isTransfer -> Icons.Rounded.ArrowForward
                                    isMinus -> Icons.Rounded.Remove

                                    else -> Icons.Rounded.Add
                                }, null,
                                tint = when {
                                    isTransfer -> CBasicTextFieldDefaults.placeholderColor
                                    isMinus -> MaterialTheme.colorScheme.error

                                    else -> themeColors.green
                                }
                            )
                            GoalPicker(
                                currentValue = transaction.toGoal,
                                currentId = transaction.toGoalId,
                                ifEmpty = "Куда?",
                                isEditable = isEditMode,
                                prohibitedGoalId = transaction.fromGoalId,
                                goals = goals,
                                modifier = Modifier.padding(start = Paddings.small)
                            ) {
                                component.onEvent(
                                    TransactionsStore.Intent.ChangeEditTransactionToId(
                                        it
                                    )
                                )
                            }
                        },
                        separator = {
                            if (isEditMode) {
                                Text(
                                    text = "и",
                                    fontWeight = FontWeight.Medium,
                                    color = CBasicTextFieldDefaults.placeholderColor,
                                    modifier = Modifier.padding(horizontal = Paddings.medium)
                                )
                            }
                        },
                        value = transaction.amount,
                        onValueChange = { amount ->
                            component.onEvent(
                                TransactionsStore.Intent.ChangeEditTransactionAmount(
                                    amount
                                )
                            )
                        },
                        isTextField = isEditMode,
                        placeholderText = "Сколько?",
                        imeAction = ImeAction.Done,
                        valueIfText = buildAnnotatedString {
                            append(" ${transaction.amount.formatLikeAmount()} ₽")
                        }
                    )
                    if (isEditMode) {

                        Spacer(Modifier.height(Paddings.medium))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EditableText(
                                value = transaction.comment,
                                onValueChange = { s ->
                                    component.onEvent(
                                        TransactionsStore.Intent.ChangeEditTransactionComment(
                                            s
                                        )
                                    )
                                },
                                placeholderText = "Комментарий",
                                modifier = Modifier.weight(1f, false)
                            )
                            SaveTextButton(
                                modifier = Modifier.weight(.4f, false),
                                onClick = {
                                    component.onEvent(TransactionsStore.Intent.PerformCreateOrEdit)
                                },
                                enabled = isSaveButtonEnabled
                            )
                        }
                    } else if (transaction.comment.isNotBlank()) {
                        Spacer(Modifier.height(Paddings.small))
                        TonalCard(Modifier.align(Alignment.End)) {
                            Text(
                                text = transaction.comment,
                                modifier = Modifier.padding(Paddings.medium)
                            )
                        }
                        Spacer(Modifier.height(Paddings.small))
                    }
                }
                if (isEditMode) {
                    DefaultSmallCloseButton {
                        component.onEvent(TransactionsStore.Intent.CancelCreatingOrEditing)
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalPicker(
    currentValue: String,
    currentId: Long?,
    ifEmpty: String,
    isEditable: Boolean,
    goals: List<Goal>,
    prohibitedGoalId: Long?,
    modifier: Modifier = Modifier,
    onGoalPick: (Long?) -> Unit
) {
    val isDropdownExpanded = remember { mutableStateOf(false) }
    DropdownMenuContainer(
        modifier = modifier,
        isEnabled = isEditable,
        isDropdownExpanded = isDropdownExpanded,
        dropdownContent = { _ ->
            goals.forEach { goal ->
                val isPicked = currentId == goal.id
                val isProhibited = prohibitedGoalId == goal.id
                DropdownMenuItem(
                    text = { Text(goal.name) },
                    onClick = {

                        onGoalPick(if (isPicked) null else goal.id)
                        isDropdownExpanded.value = false
                    },
                    trailingIcon = {
                        if (isPicked) Icon(Icons.Rounded.Done, null)
                    },
                    enabled = !isProhibited
                )
            }
        }
    ) {
        Text(
            currentValue.ifBlank { ifEmpty },
            color = if (currentValue.isBlank()) CBasicTextFieldDefaults.placeholderColor else MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .animateContentSize()
                .clip(MaterialTheme.shapes.medium)
                .then(
                    if (isEditable) Modifier.clickable {
                        isDropdownExpanded.value = true
                    } else Modifier
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
