import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import ktor.KtorMainRemoteDataSource
import ktor.RNewsItem
import room.NewsEntity
import room.RoomMainLocalDataSource
import room.mapToNewsItems

class MainRepositoryImpl(
    private val remoteDataSource: KtorMainRemoteDataSource,
    private val localDataSource: RoomMainLocalDataSource
) : MainRepository {

    private val newsFlow = localDataSource.getNewsEntities()

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

        if (mustBeFromInternet || isCacheExpired) {
            val responseNews = remoteDataSource.fetchRecentNewsData()
                .news
                // it has to be published
                .filter { it.date != null }
            refreshNews(responseNews, cachedNews)
        }
    }


    private suspend fun refreshNews(responseNews: List<RNewsItem>, cachedNews: List<NewsEntity>) {
        // without images or with cached images
        localDataSource.refreshWithoutImages(responseNews, cachedNews)

        // fetch actual images
        responseNews.forEach { rNewsItem ->
            val imageUrl = rNewsItem.media.maxByOrNull { it.height * it.width }?.url
            val imageByteArray = imageUrl?.let { remoteDataSource.fetchNewsImageByteArray(it) }
            localDataSource.updateForImage(rNewsItem, imageByteArray)
        }
    }

    override fun getNewsFlow(): Flow<List<NewsItem>> = newsFlow.mapToNewsItems()

}