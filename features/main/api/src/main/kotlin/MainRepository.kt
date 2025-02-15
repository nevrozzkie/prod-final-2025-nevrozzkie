import utils.NewsItem
import utils.Ticker

interface MainRepository {

    suspend fun fetchTickers(ids: List<String>) : List<Ticker>
    suspend fun fetchRecentNews() : List<NewsItem>

}