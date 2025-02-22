package room

import androidx.room.Database
import androidx.room.RoomDatabase
import room.goals.GoalEntity
import room.goals.GoalsDao
import room.transactions.TransactionEntity
import room.transactions.TransactionsDao

@Database(entities = [GoalEntity::class, TransactionEntity::class], version = 9)
internal abstract class FinanceDatabase : RoomDatabase() {
    abstract fun goalsDao(): GoalsDao
    abstract fun transactionsDao(): TransactionsDao
}