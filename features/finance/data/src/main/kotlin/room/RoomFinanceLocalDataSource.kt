package room

import Goal
import Transaction
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import room.goals.GoalEntityWithSavedAmount
import room.goals.GoalsDao
import room.goals.toEntity
import room.transactions.TransactionEntityWithGoal
import room.transactions.TransactionsDao
import room.transactions.toEntities
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

    internal suspend fun upsertTransaction(transaction: Transaction) {
        withDatabaseContext {

            val entities = transaction.toEntities()
            entities.forEach {
                transactionsDao.upsertTransaction(it)
            }
        }
    }

    internal suspend fun deleteTransaction(transaction: Transaction) {
        withDatabaseContext {
            transaction.toEntities().forEach {
                transactionsDao.deleteTransaction(it)
            }
        }
    }

    internal fun getTransactions(): Flow<List<TransactionEntityWithGoal>>
        = transactionsDao.getTransactionEntities()

    // Goals
    internal suspend fun upsertGoal(goal: Goal) {
        withDatabaseContext {
            goalsDao.upsertGoal(goal.toEntity())
        }
    }
    internal suspend fun deleteGoal(goal: Goal) {
        withDatabaseContext {
            goalsDao.deleteGoal(goal.toEntity())
        }
    }
    internal fun getGoalsWithSavedAmountEntities(): Flow<List<GoalEntityWithSavedAmount>> = goalsDao.getGoalsWithSavedAmount()


}