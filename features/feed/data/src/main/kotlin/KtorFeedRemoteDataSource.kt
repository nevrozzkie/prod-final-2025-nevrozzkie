import io.ktor.client.HttpClient
import ktor.dBody
import ktor.dGet
import utils.RFetchCompanyInfoResponse
import utils.RFetchExchangeRateResponse
import utils.RFetchTickerPriceResponse
import utils.RFetchTickerRequest

private object Paths {
    object Stock {
        const val FETCH_PRICE = "quote"
        const val FETCH_INFO = "stock/profile2"
    }

}


class KtorFeedRemoteDataSource(
    private val hcStock: HttpClient,
    private val hcExchange: HttpClient,
    private val hcNews: HttpClient,
) {
    suspend fun fetchTickerData(r: RFetchTickerRequest): Pair<RFetchCompanyInfoResponse, RFetchTickerPriceResponse> {
        return with(Paths.Stock) {
            val info: RFetchCompanyInfoResponse = hcStock.dGet(FETCH_INFO, r).dBody()
            val price: RFetchTickerPriceResponse = hcStock.dGet(FETCH_PRICE, r).dBody()
            info to price
        }
    }

    suspend fun fetchExchangeRateToRuble(from: String) : RFetchExchangeRateResponse {
        val path = "$from/RUB"
        return hcExchange.dGet(path).dBody()
    }
}