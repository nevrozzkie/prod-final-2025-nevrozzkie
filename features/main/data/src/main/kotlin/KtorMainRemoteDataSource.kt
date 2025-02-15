import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.statement.readRawBytes
import io.ktor.http.Url
import ktor.HttpConstants.News.TOKEN
import ktor.HttpConstants.News.TOKEN_HEADER
import ktor.dBody
import ktor.dGet
import utils.RFetchCompanyInfoResponse
import utils.RFetchExchangeRateResponse
import utils.RFetchNewsResponse
import utils.RFetchTickerPriceResponse

private object Paths {
    object Stock {
        private const val PRE_PATH = "api/v1/"

        const val FETCH_PRICE = PRE_PATH + "quote"
        const val FETCH_INFO = PRE_PATH + "stock/profile2"
    }

    object News {
        private const val PRE_PATH = "svc/news/v3/"
        const val FETCH_RECENT = PRE_PATH + "content/all/all.json"
    }
}


class KtorMainRemoteDataSource(
    private val hcStock: HttpClient,
    private val hcExchange: HttpClient,
    private val hcNews: HttpClient,
    private val hcDefault: HttpClient,
) {

    suspend fun fetchRecentNewsData(): RFetchNewsResponse {
        return with(Paths.News) {
            hcNews.dGet(FETCH_RECENT, 0) {
                parameter("limit", 5)
                parameter(TOKEN_HEADER, TOKEN)
            }.dBody()
        }
    }


    suspend fun fetchTickerData(id: String): Pair<RFetchCompanyInfoResponse, RFetchTickerPriceResponse> {
        return with(Paths.Stock) {
            val symbolParameter: HttpRequestBuilder.() -> Unit = {
                parameter("symbol", id)
            }

            val info: RFetchCompanyInfoResponse =
                hcStock.dGet(FETCH_INFO, 60 * 60) {
                    symbolParameter()
                }.dBody()

            val price: RFetchTickerPriceResponse = hcStock.dGet(FETCH_PRICE, 0) {
                symbolParameter()
            }.dBody()

            info to price
        }
    }

    suspend fun fetchExchangeRateToRuble(from: String): RFetchExchangeRateResponse {
        val path = "$from/RUB"
        return RFetchExchangeRateResponse(
            conversionRate = 93.0f
        ) //hcExchange.dGet(path, saveForSeconds = 60 * 60).dBody()
    }


    suspend fun fetchBitmap(url: String): Bitmap? {
        return try {
            val bytes =
                hcDefault.dGet(Url(url), 60 * 60).readRawBytes()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Throwable) {
            println("fetch bitmapError $e")
            return null
        }
    }
}