import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import utils.RFetchTickerRequest
import utils.Ticker

class FeedRepositoryImpl(
    private val remoteDataSource: KtorFeedRemoteDataSource
) : FeedRepository {
    private val exchangeRates = mutableMapOf<String, Float>()
    override suspend fun fetchTickers(rs: List<RFetchTickerRequest>): List<Ticker> =
        coroutineScope {
            val tickerResponses = rs.map { r ->
                async {
                    remoteDataSource.fetchTickerData(r)
                }
            }.awaitAll()

            tickerResponses.map { (info, price) ->
                val exchangeRate = exchangeRates.getOrPut(info.currency) {
                    remoteDataSource.fetchExchangeRateToRuble(info.currency).conversionRate
                }
                Ticker(
                    title = info.title,
                    price = price.price,
                    currency = info.currency,
                    rubles = price.price * exchangeRate,
                    percentageDelta = price.percentageDelta,
                    logo = info.logo
                )
            }
        }

}