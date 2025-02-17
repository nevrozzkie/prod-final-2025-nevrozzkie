package room

import Goal
import Transaction
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import room.goals.GoalsDao
import room.goals.mapToGoals
import room.goals.toEntity
import room.transactions.TransactionsDao
import room.transactions.mapToTransactions
import room.transactions.toEntity
import withDatabaseContext

internal data object FinanceDatabaseNames {
    data object Tables {
        const val GOALS = "goals_table"
        const val TRANSACTIONS = "transactions_table"
    }
    const val FINANCE_DB = "finance_db"
}

class RoomFinanceLocalDataSource(
    androidContext: Context
) {
    private var goalsDao: GoalsDao
    private var transactionsDao: TransactionsDao

    init {
        val db = Room.databaseBuilder(
            context = androidContext,
            klass = FinanceDatabase::class.java,
            FinanceDatabaseNames.FINANCE_DB
        ).fallbackToDestructiveMigration().build()
        goalsDao = db.goalsDao()
        transactionsDao = db.transactionsDao()
    }

    internal suspend fun insertTransaction(transaction: Transaction) {
        withDatabaseContext {
            transactionsDao.insertTransaction(transaction.toEntity())
        }
    }

    internal suspend fun updateTransaction(transaction: Transaction) {
        withDatabaseContext {
            transactionsDao.updateTransaction(transaction.toEntity())
        }
    }

    internal suspend fun deleteTransaction(transaction: Transaction) {
        withDatabaseContext {
            transactionsDao.deleteTransaction(transaction.toEntity())
        }
    }

    internal fun getTransactions(goalsIds: List<Long>): Flow<List<Transaction>>
        = transactionsDao.getTransactionEntities(goalsIds).mapToTransactions()

    // Goals
    internal suspend fun insertGoal(goal: Goal) {
        withDatabaseContext {
            goalsDao.insertGoal(goal.toEntity())
        }
    }
    internal suspend fun updateGoal(goal: Goal) {
        withDatabaseContext {
            goalsDao.updateGoal(goal.toEntity())
        }
    }
    internal suspend fun deleteGoal(goal: Goal) {
        withDatabaseContext {
            goalsDao.deleteGoal(goal.toEntity())
        }
    }
    internal fun getActualGoals(): Flow<List<Goal>> = goalsDao.getActualGoalEntities().mapToGoals()
    internal fun getCompletedGoals(): Flow<List<Goal>> = goalsDao.getCompletedGoalEntities().mapToGoals()


}