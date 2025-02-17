import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    fun getActiveGoalsFlow(): Flow<List<Goal>>
    fun getCompletedGoalsFlow(): Flow<List<Goal>>

    suspend fun insertGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)


    fun getTransactionsFlow(goalsIds: List<Long>): Flow<List<Transaction>>

    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}