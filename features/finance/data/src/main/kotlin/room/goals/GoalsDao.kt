package room.goals


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goalEntity: GoalEntity)

    @Delete
    suspend fun deleteGoal(goalEntity: GoalEntity)

    @Query("SELECT * FROM goals_table WHERE completed_date is null")
    fun getActualGoalEntities() : Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals_table WHERE completed_date is not null")
    fun getCompletedGoalEntities() : Flow<List<GoalEntity>>
}