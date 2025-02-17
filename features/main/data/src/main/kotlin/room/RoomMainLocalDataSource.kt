package room

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import ktor.RNewsItem
import withDatabaseContext

internal object MainDatabaseNames {
    object Tables {
        const val NEWS = "news_table"
    }
    const val MAIN_DB = "main_db"
}

class RoomMainLocalDataSource(
    androidContext: Context
) {
    private var newsDao: NewsDao

    init {
        val db = Room.databaseBuilder(
            context = androidContext,
            klass = NewsDatabase::class.java,
            MainDatabaseNames.MAIN_DB
        ).fallbackToDestructiveMigration().build()
        newsDao = db.newsDao()
    }

    internal suspend fun refreshWithoutImages(responseNews: List<RNewsItem>, cachedNews: List<NewsEntity>) {
        val entitiesWithoutImage = responseNews.map { rNewsItem ->
            // if we already cached image -> it won't be null
            val cachedImage = cachedNews
                .firstOrNull { it.id == rNewsItem.id }?.imageByteArray
            rNewsItem.toEntity(
                rNewsItem.id,
                imageByteArray = cachedImage,
                isImageLoading = cachedImage == null
            )
        }
        withDatabaseContext {
            newsDao.refreshNews(entitiesWithoutImage)
        }
    }

    internal suspend fun updateForImage(newsItem: RNewsItem, imageByteArray: ByteArray?) {
        withDatabaseContext {
            val entity = newsItem.toEntity(
                id = newsItem.id,
                imageByteArray = imageByteArray,
                isImageLoading = false
            )
            newsDao.updateNewsEntity(entity)
        }
    }

    internal fun getNewsEntities(): Flow<List<NewsEntity>> =
        newsDao.getAllNewsEntities()


}