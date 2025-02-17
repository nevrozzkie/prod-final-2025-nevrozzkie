package room

import NewsItem
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal object DatabaseNames {
    const val NEWS = "news_table"
}

class RoomMainLocalDataSource(
    androidContext: Context
) {
    private var newsDao: NewsDao

    init {
        val db = Room.databaseBuilder(
            context = androidContext,
            klass = NewsDatabase::class.java,
            DatabaseNames.NEWS
        ).fallbackToDestructiveMigration().build()
        newsDao = db.newsDao()
    }

    internal suspend fun refreshNews(newNews: List<NewsEntity>) {
        withContext(Dispatchers.IO) {
            newsDao.refreshNews(newNews)
        }
    }

    internal suspend fun updateNewsItem(newsEntity: NewsEntity) {
        withContext(Dispatchers.IO) {
            newsDao.updateNewsItem(newsEntity)
        }
    }
    internal fun fetchNewsEntities(): Flow<List<NewsEntity>> =
        newsDao.getAllNewsEntities()

    fun fetchNewsItems(): Flow<List<NewsItem>> =
        newsDao.getAllNewsEntities().map { list ->
            list.map { it.mapToNewsItem() }
        }

}