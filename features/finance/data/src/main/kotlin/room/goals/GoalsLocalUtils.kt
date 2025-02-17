package room.goals

import Goal
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import room.FinanceDatabaseNames

@Entity(tableName = FinanceDatabaseNames.Tables.GOALS)
internal data class GoalEntity(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "target_amount") val targetAmount: Long,

    @ColumnInfo(name = "created_date") val createdDate: String,
    @ColumnInfo(name = "planned_date") val plannedDate: String,
    @ColumnInfo(name = "completed_date") val completedDate: String?
) {
    fun toGoal() = Goal(
        id = id,
        name = name,
        targetAmount = targetAmount,
        createdDate = createdDate,
        plannedDate = plannedDate,
        completedDate = completedDate
    )
}

internal fun Goal.toEntity() = GoalEntity(
    id = id,
    name = name,
    targetAmount = targetAmount,
    createdDate = createdDate,
    plannedDate = plannedDate,
    completedDate = completedDate
)

internal fun Flow<List<GoalEntity>>.mapToGoals() =
    this.map { list -> list.map { it.toGoal() } }