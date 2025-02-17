import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import ktor.KtorMainRemoteDataSource
import ktor.RNewsItem
import room.NewsEntity
import room.RoomMainLocalDataSource

class MainRepositoryImpl(
    private val remoteDataSource: KtorMainRemoteDataSource,
    private val localDataSource: RoomMainLocalDataSource
) : MainRepository {

    private val newsFlow = localDataSource.fetchNewsEntities()

    override suspend fun fetchTickers(ids: List<String>): List<Ticker> =
        coroutineScope {
            val tickerResponses = ids.map { id ->
                async {
                    remoteDataSource.fetchTickerData(id)
                }
            }.awaitAll()

            // fetch logo
            tickerResponses.map { (info, price) ->
                val exchangeRate =
                    remoteDataSource.fetchExchangeRateToRuble(info.currency).conversionRate
                val logo = remoteDataSource.fetchTickerLogoBitmap(info.logo)

                Ticker(
                    title = info.title,
                    price = price.price,
                    currency = info.currency,
                    rubles = price.price * exchangeRate,
                    percentageDelta = price.percentageDelta,
                    logoBitmap = logo
                )
            }
        }

    override suspend fun fetchRecentNews(mustBeFromInternet: Boolean) {
        val currentTimestamp = Clock.System.now().toTimestamp()

        val cachedNews = newsFlow.firstOrNull() ?: emptyList()

        val isCacheExpired = cachedNews.firstOrNull()?.let { news ->
            (currentTimestamp - news.insertInDBTimestamp) / 1000 > CacheLocalSeconds.News.RECENT_NEWS
        } ?: true

        if (
            mustBeFromInternet || isCacheExpired
        ) {
            val responseNews = remoteDataSource.fetchRecentNewsData()
                .news.filter { it.date != null }
            refreshNews(responseNews, cachedNews)
        }
    }


    private suspend fun refreshNews(response: List<RNewsItem>, cachedNews: List<NewsEntity>) {
        val entitiesWithoutImage = response.map { newsItem ->

            // if we already cached image -> it won't be null
            val cachedImage = cachedNews
                .firstOrNull { it.id == newsItem.id }?.imageByteArray

            newsItem.mapToEntity(
                newsItem.id,
                imageByteArray = cachedImage,
                isImageLoading = cachedImage == null
            )
        }
        localDataSource.refreshNews(entitiesWithoutImage)

        // fetch actual images
        response.forEach { newsItem ->
            val imageUrl = newsItem.media.maxByOrNull { it.height * it.width }?.url
            val image = imageUrl?.let { remoteDataSource.fetchNewsImageByteArray(it) }

            val entity = newsItem.mapToEntity(
                id = newsItem.id,
                imageByteArray = image,
                isImageLoading = false
            )
            localDataSource.updateNewsItem(entity)
        }
    }

    override fun getNewsFlow(): Flow<List<NewsItem>> = newsFlow.map { list ->
        list.map { it.mapToNewsItem() }
    }

}