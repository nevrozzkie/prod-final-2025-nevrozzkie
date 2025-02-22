import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun findTickers(query: String) : List<String>
    suspend fun findNews(query: String) : Flow<List<NewsItem>>
}