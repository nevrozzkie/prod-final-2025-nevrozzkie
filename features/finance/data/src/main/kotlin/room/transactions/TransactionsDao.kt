package room.transactions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionsDao {
    @Upsert
    suspend fun upsertTransaction(transactionEntity: TransactionEntity): Long

    @Delete
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    @Query("""
        SELECT t.*, g.name AS goal_name
        FROM transactions_table t
        LEFT JOIN goals_table g ON t.goal_id = g.id
    """)
    fun getTransactionEntities() : Flow<List<TransactionEntityWithGoal>>

//    @Query("SELECT MAX(id) FROM transactions_table")
//    fun getMaxId(): Long
}