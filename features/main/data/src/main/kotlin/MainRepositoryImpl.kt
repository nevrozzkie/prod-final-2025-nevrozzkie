import android.graphics.Bitmap
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import utils.Ticker

class MainRepositoryImpl(
    private val remoteDataSource: KtorMainRemoteDataSource
) : MainRepository {
    private val exchangeRates = mutableMapOf<String, Float>()
    private val logoBitmaps = mutableMapOf<String, Bitmap>()
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

                val logo = logoBitmaps.getOrElse(info.logo) {
                    val bitmap = remoteDataSource.fetchBitmap(info.logo)
                    if (bitmap != null) logoBitmaps[info.logo] = bitmap
                    bitmap
                }
                Ticker(
                    title = info.title,
                    price = price.price,
                    currency = info.currency,
                    rubles = price.price * exchangeRate,
                    percentageDelta = price.percentageDelta,
                    logo = logo
                )
            }
        }

}