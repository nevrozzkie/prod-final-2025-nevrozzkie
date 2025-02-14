import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.statement.readRawBytes
import io.ktor.http.Url
import ktor.dBody
import ktor.dGet
import utils.RFetchCompanyInfoResponse
import utils.RFetchExchangeRateResponse
import utils.RFetchTickerPriceResponse

private object Paths {
    object Stock {
        private const val PRE_PATH = "api/v1/"

        const val FETCH_PRICE = PRE_PATH + "quote"
        const val FETCH_INFO = PRE_PATH + "stock/profile2"
    }

}


class KtorMainRemoteDataSource(
    private val hcStock: HttpClient,
    private val hcExchange: HttpClient,
    private val hcNews: HttpClient,
    private val hcDefault: HttpClient,
) {
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
        return hcExchange.dGet(path, saveForSeconds = 60*60).dBody()
    }


    suspend fun fetchBitmap(url: String): Bitmap? {
        return try {
            val bytes =
                hcDefault.dGet(Url(url), 60*60).readRawBytes()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Throwable) {
            println("bitmapError $e")
            return null
        }
    }
}