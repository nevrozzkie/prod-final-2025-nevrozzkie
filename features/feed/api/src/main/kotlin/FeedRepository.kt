import utils.RFetchTickerRequest
import utils.Ticker

interface FeedRepository {

    suspend fun fetchTickers(rs: List<RFetchTickerRequest>) : List<Ticker>

}