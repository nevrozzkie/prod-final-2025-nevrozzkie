package ktor

import HttpConstants
import android.graphics.Bitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter




// what about creating 3 classes instead 1?
class KtorMainRemoteDataSource(
    private val hcStock: HttpClient,
    private val hcExchange: HttpClient,
    private val hcNews: HttpClient
) {

    internal suspend fun fetchRecentNewsData(): RFetchNewsResponse {
        return with(HttpConstants.Paths.News) {
            hcNews.dGet(FETCH_RECENT, HttpConstants.CacheSeconds.News.RECENT_NEWS) {
                parameter("limit", 5)
                parameter(HttpConstants.Core.News.TOKEN_HEADER, HttpConstants.Core.News.TOKEN)
            }.dBody()
        }
    }


    internal suspend fun fetchTickerData(id: String): Pair<RFetchCompanyInfoResponse, RFetchTickerPriceResponse> {
        return with(HttpConstants.Paths.Stock) {
            val symbolParameter: HttpRequestBuilder.() -> Unit = {
                parameter("symbol", id)
            }

            val info: RFetchCompanyInfoResponse =
                hcStock.dGet(FETCH_INFO, HttpConstants.CacheSeconds.Stock.COMPANY_INFO) {
                    symbolParameter()
                }.dBody()

            val price: RFetchTickerPriceResponse =
                hcStock.dGet(FETCH_PRICE, HttpConstants.CacheSeconds.Stock.TICKER_PRICE) {
                    symbolParameter()
                }.dBody()

            info to price
        }
    }

    internal suspend fun fetchExchangeRateToRuble(from: String): RFetchExchangeRateResponse {
        val path = "$from/RUB"
        return RFetchExchangeRateResponse(
            conversionRate = 93.4f
        ) //hcExchange.dGet(path, saveForSeconds = ExchangeRate).dBody()
    }

    internal suspend fun fetchTickerLogoBitmap(url: String): Bitmap? =
        hcStock.fetchByteArray(url, HttpConstants.CacheSeconds.IMAGE)?.imageBitmap

    internal suspend fun fetchNewsImageByteArray(url: String): ByteArray? =
        hcNews.fetchByteArray(url, HttpConstants.CacheSeconds.News.RECENT_NEWS_IMAGE)
}