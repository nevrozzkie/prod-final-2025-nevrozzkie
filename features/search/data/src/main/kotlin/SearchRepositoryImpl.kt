import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ktor.KtorSearchRemoteDataSource
import utils.toLocalDate
import utils.toTimestamp

class SearchRepositoryImpl(
    private val remoteDataSource: KtorSearchRemoteDataSource,
) : SearchRepository {
    override suspend fun findTickers(query: String): List<String> {
        return remoteDataSource.findTickers(query).result.map { it.symbol }
    }

    override suspend fun findNews(query: String): Flow<List<NewsItem>> {
        return flow {

            val rNewsItem = remoteDataSource.findNews(query)


            val initialNewsList = rNewsItem.news.mapNotNull { rNews ->
                if (rNews.date == null) null
                else
                    rNews.media.maxByOrNull { it.height * it.width }?.url to NewsItem(
                        id = rNews.id,
                        url = rNews.url,
                        title = rNews.title,
                        desc = rNews.desc,
                        imageBitmap = null,
                        isImageLoading = true,
                        source = rNews.source,
                        geo = rNews.geo.minByOrNull { g -> g.length },
                        date = rNews.date!!.toTimestamp().toLocalDate()
                    )
            }


            emit(initialNewsList.map { it.second })


            val updatedNewsList = initialNewsList.map { (imageUrl, newsItem) ->

                val imageBitmap = imageUrl?.let { url ->
                    remoteDataSource.fetchImageBitmap(url)
                }

                newsItem.copy(
                    imageBitmap = imageBitmap,
                    isImageLoading = false
                )
            }

            emit(updatedNewsList)
        }
    }
}