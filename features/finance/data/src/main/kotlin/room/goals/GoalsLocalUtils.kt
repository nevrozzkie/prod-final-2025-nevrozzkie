package room.goals

import Goal
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dbFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.format
import parseToLocalDate
import room.FinanceDatabaseNames
import room.transactions.TransactionEntity

@Entity(tableName = FinanceDatabaseNames.Tables.GOALS)
internal data class GoalEntity(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "target_amount") val targetAmount: Long,

    @ColumnInfo(name = "created_date") val createdDate: String,
    @ColumnInfo(name = "planned_date") val plannedDate: String?,
    @ColumnInfo(name = "completed_date") val completedDate: String?
) {
    fun toGoal(savedAmount: Long) = Goal(
        id = id,
        name = name,
        targetAmount = targetAmount,
        createdDate = createdDate.parseToLocalDate(dbFormat),
        plannedDate = plannedDate?.parseToLocalDate(dbFormat),
        completedDate = completedDate?.parseToLocalDate(dbFormat),
        isEditing = false,
        savedAmount = savedAmount
    )
}

internal fun Goal.toEntity() = GoalEntity(
    id = id,
    name = name,
    targetAmount = targetAmount,
    createdDate = createdDate.format(dbFormat),
    plannedDate = plannedDate?.format(dbFormat),
    completedDate = completedDate?.format(dbFormat)
)

internal data class GoalEntityWithSavedAmount(
    @Embedded val goal: GoalEntity,
    val savedAmount: Long
)

internal fun Flow<List<GoalEntityWithSavedAmount>>.mapToGoals() = map { list ->
    list.map { it.goal.toGoal(it.savedAmount) }
}
//
//internal fun List<GoalEntity>.mapToGoals() =
//    this.map { list -> list.map { it.toGoal() } }