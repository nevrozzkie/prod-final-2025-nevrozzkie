package room.goals


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GoalsDao {
    @Upsert
    suspend fun upsertGoal(goalEntity: GoalEntity)

    @Delete
    suspend fun deleteGoal(goalEntity: GoalEntity)

    @Query("""
        SELECT g.*, COALESCE(SUM(t.amount), 0) AS savedAmount
        FROM goals_table g
        LEFT JOIN transactions_table t ON g.id = t.goal_id
        GROUP BY g.id
    """)
    fun getGoalsWithSavedAmount(): Flow<List<GoalEntityWithSavedAmount>>
}