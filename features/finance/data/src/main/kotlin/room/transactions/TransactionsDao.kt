package room.transactions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionsDao {
    @Insert
    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM transactions_table WHERE ((:goalsIds) IS NULL OR goal_id IN (:goalsIds))")
     fun getTransactionEntitiesInternal(goalsIds: List<Long>?) : Flow<List<TransactionEntity>>

    fun getTransactionEntities(goalsIds: List<Long>) : Flow<List<TransactionEntity>> {
        return if (goalsIds.isEmpty()) {
            getTransactionEntitiesInternal(null)
        } else getTransactionEntitiesInternal(goalsIds)
    }
}