import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    fun getGoalsFlow(): Flow<List<Goal>>

    suspend fun upsertGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)

    suspend fun completeGoal(goal: Goal, transactionMaxId: Long)

    fun getTransactionsFlow(): Flow<List<Transaction>>

    suspend fun upsertTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}