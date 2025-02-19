package room.transactions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)
//
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