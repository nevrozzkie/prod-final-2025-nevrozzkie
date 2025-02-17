package room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NewsDao {
    @Query("DELETE FROM news_table")
    suspend fun deleteAllNews()

    @Insert
    suspend fun insertNews(newsEntity: List<NewsEntity>)

    @Transaction
    suspend fun refreshNews(newNews: List<NewsEntity>) {
        deleteAllNews()
        insertNews(newNews)
    }

    @Update
    suspend fun updateNewsItem(newsEntity: NewsEntity)

    @Query("SELECT * FROM news_table")
    fun getAllNewsEntities() : Flow<List<NewsEntity>>
}