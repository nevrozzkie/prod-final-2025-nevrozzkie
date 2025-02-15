import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import utils.NewsItem
import utils.Ticker

class MainRepositoryImpl(
    private val remoteDataSource: KtorMainRemoteDataSource
) : MainRepository {
    override suspend fun fetchTickers(ids: List<String>): List<Ticker> =
        coroutineScope {
            val tickerResponses = ids.map { id ->
                async {
                    remoteDataSource.fetchTickerData(id)
                }
            }.awaitAll()

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

    override suspend fun fetchRecentNews(): List<NewsItem> {
        return remoteDataSource.fetchRecentNewsData().news.map {
            val imageUrl = it.media.maxByOrNull { i -> i.height * i.width }?.url
            val image = imageUrl?.let { remoteDataSource.fetchNewsImageBitmap(imageUrl) }

            NewsItem(
                title = it.title,
                desc = it.desc,
                imageBitmap = image,
                source = it.source,
                geo = it.geo.minByOrNull { g -> g.length },
                date = it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
            )
        }
    }

}