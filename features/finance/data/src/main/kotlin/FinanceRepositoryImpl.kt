import kotlinx.coroutines.flow.Flow
import room.RoomFinanceLocalDataSource

class FinanceRepositoryImpl(
    private val localDataSource: RoomFinanceLocalDataSource
) : FinanceRepository {
    override fun getActiveGoalsFlow(): Flow<List<Goal>> =
        localDataSource.getActualGoals()

    override fun getCompletedGoalsFlow(): Flow<List<Goal>> =
        localDataSource.getCompletedGoals()

    override suspend fun insertGoal(goal: Goal) {
        localDataSource.insertGoal(goal)
    }

    override suspend fun updateGoal(goal: Goal) {
        localDataSource.updateGoal(goal)
    }

    override suspend fun deleteGoal(goal: Goal) {
        localDataSource.deleteGoal(goal)
    }

    override fun getTransactionsFlow(goalsIds: List<Long>): Flow<List<Transaction>> =
        localDataSource.getTransactions(goalsIds)

    override suspend fun insertTransaction(transaction: Transaction) {
        localDataSource.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        localDataSource.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        localDataSource.deleteTransaction(transaction)
    }

}