package ktor

import HttpConstants
import android.graphics.Bitmap
import androidx.room.Query
import bitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

class KtorSearchRemoteDataSource(
    private val hcStock: HttpClient,
    private val hcNews: HttpClient,
) {
    internal suspend fun findTickers(query: String) : RFindTickersResponse {
        return with (HttpConstants.Paths.Stock) {
            hcStock.dGet(SEARCH, HttpConstants.CacheSeconds.Stock.SEARCH) {
                parameter("exchange", "US")
                parameter("q", query)
            }.dBody()
        }
    }

    internal suspend fun findNews(query: String): RFetchNewsResponse {
        return with(HttpConstants.Paths.News) {
            hcNews.dGet(SEARCH, HttpConstants.CacheSeconds.News.RECENT_NEWS) {
                parameter("q", query)
                parameter(HttpConstants.Core.News.TOKEN_HEADER, HttpConstants.Core.News.TOKEN)
            }.dBody()
        }
    }

    internal suspend fun fetchImageBitmap(url: String): Bitmap? =
        hcNews.fetchByteArray(url, HttpConstants.CacheSeconds.IMAGE)?.bitmap
}