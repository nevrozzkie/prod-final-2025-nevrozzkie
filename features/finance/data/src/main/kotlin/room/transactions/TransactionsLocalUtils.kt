package room.transactions

import Transaction
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import room.FinanceDatabaseNames
import utils.toTimestamp


@Entity(tableName = FinanceDatabaseNames.Tables.TRANSACTIONS)
internal data class TransactionEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "goal_id") val goalId: Long,
    val amount: Long,
    val comment: String,
    @ColumnInfo(name = "created_date") val createdDate: String,
    @ColumnInfo(name = "updated_timestamp") val updatedTimestamp: Long,
) {
    fun toTransaction(fromGoalId: Long?, fromGoal: String, toGoal: String) = Transaction(
        id = id,
        fromGoalId = fromGoalId,
        toGoalId = goalId,
        amount = amount.toString(),
        comment = comment,
        createdDate = createdDate,
        isEditing = false,
        fromGoal = fromGoal,
        toGoal = toGoal
    )
}

internal fun Transaction.toEntities() =
    buildList {
        val currentTimestamp = Clock.System.now().toTimestamp()
        val amountL = amount.toLong()
        val newestTransaction = TransactionEntity(
            id = id,
            goalId = toGoalId!!,
            amount = amountL,
            comment = comment,
            createdDate = createdDate,
            updatedTimestamp = currentTimestamp
        )
        add(newestTransaction)
        if (fromGoalId != null) add(
            newestTransaction.copy(
                id = id - 1,
                amount = -amountL,
                goalId = fromGoalId!!,
                comment = ""
            )
        )

    }


internal data class TransactionEntityWithGoal(
    @Embedded val transaction: TransactionEntity,
    @ColumnInfo(name = "goal_name") val goalName: String?
)

internal fun Flow<List<TransactionEntityWithGoal>>.mapToTransactions() = this.map { list ->
    val output = mutableListOf<Transaction>()
    var i = 0

    while (i < list.size) {
        val currentItem = list[i]
        val currentTransaction = currentItem.transaction

        if (i + 1 < list.size) {
            val nextItem = list[i + 1]
            val nextTransaction = nextItem.transaction


            val isOtherGoal = nextTransaction.goalId != currentTransaction.goalId
            val isSameTime =
                isOtherGoal && (nextTransaction.updatedTimestamp - currentTransaction.updatedTimestamp) <= 5 * 60 * 1000
            val isItTransfer =
                isSameTime && nextTransaction.amount > 0 && nextTransaction.amount == -currentTransaction.amount

            if (isItTransfer) {
                output.add(
                    nextTransaction.toTransaction(
                        fromGoalId = currentTransaction.goalId,
                        fromGoal = currentItem.goalName ?: "",
                        toGoal = nextItem.goalName ?: ""
                    )
                )
                i += 2
            } else {
                output.add(
                    currentTransaction.toTransaction(
                        fromGoalId = null,
                        fromGoal = "",
                        toGoal = currentItem.goalName ?: ""
                    )
                )
                i++
            }
        } else {
            output.add(
                currentTransaction.toTransaction(
                    fromGoalId = null,
                    fromGoal = "",
                    toGoal = currentItem.goalName ?: ""
                )
            )
            i++
        }
    }

    output
}
