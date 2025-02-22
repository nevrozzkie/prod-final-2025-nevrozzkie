import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.format
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

    override suspend fun completeGoal(goal: Goal, transactionMaxId: Long) {
        localDataSource.upsertGoal(goal.copy(completedDate = getCurrentLocalDateTime().date))
        localDataSource.upsertTransaction(
            Transaction(
                id = transactionMaxId+1,
                fromGoal = "",
                toGoal = goal.name,
                fromGoalId = null,
                toGoalId = goal.id,
                amount = (-goal.savedAmount).toString(),
                comment = "Закрытие цели",
                createdDate = getCurrentLocalDateTime().date.format(rusFormat),
                isEditing = false
            )
        )
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