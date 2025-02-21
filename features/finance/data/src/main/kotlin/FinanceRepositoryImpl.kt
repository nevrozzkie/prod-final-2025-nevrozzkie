import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import room.RoomFinanceLocalDataSource
import room.goals.mapToGoals
import room.transactions.mapToTransactions

class FinanceRepositoryImpl(
    private val localDataSource: RoomFinanceLocalDataSource
) : FinanceRepository {
    override fun getGoalsFlow(): Flow<List<Goal>> =
        localDataSource.getGoalsWithSavedAmountEntities().mapToGoals()


    override suspend fun upsertGoal(goal: Goal) {
        localDataSource.upsertGoal(goal)
    }

    override suspend fun deleteGoal(goal: Goal) {
        localDataSource.deleteGoal(goal)
    }

    override fun getTransactionsFlow(): Flow<List<Transaction>> =
        localDataSource.getTransactions().mapToTransactions()

    override suspend fun upsertTransaction(transaction: Transaction) {
        localDataSource.upsertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        localDataSource.deleteTransaction(transaction)
    }

}