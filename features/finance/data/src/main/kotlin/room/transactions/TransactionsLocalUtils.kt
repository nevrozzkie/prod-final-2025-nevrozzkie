package room.transactions

import Transaction
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import room.FinanceDatabaseNames

@Entity(tableName = FinanceDatabaseNames.Tables.TRANSACTIONS)
internal data class TransactionEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "goal_id") val goalId: Long,
    val amount: Long,
    val comment: String,
    @ColumnInfo(name = "created_date") val createdDate: String,
) {
    fun toTransaction() = Transaction(
        id = id,
        fromGoalId = null,
        toGoalId = goalId,
        amount = amount,
        comment = comment,
        createdDate = createdDate,
        isEditing = false
    )
}

internal fun Transaction.toEntity() = TransactionEntity(
    id = id,
    goalId = toGoalId,
    amount = amount,
    comment = comment,
    createdDate = createdDate
)
internal fun Flow<List<TransactionEntity>>.mapToTransactions() =
    this.map { list -> list.map { it.toTransaction() } }