import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun fetchTickers(ids: List<String>, canBeErrors: Boolean) : List<Ticker>


    suspend fun fetchRecentNews(mustBeFromInternet: Boolean)


    fun getNewsFlow() : Flow<List<NewsItem>>

}