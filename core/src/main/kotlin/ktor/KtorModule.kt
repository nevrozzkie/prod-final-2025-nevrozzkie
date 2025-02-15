package ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

data object HttpConstants {
    data object News {
        const val CLIENT_NAME = "HttpClientNews"
        const val HOST = "api.nytimes.com"
        const val TOKEN_HEADER = "api-key"
        const val TOKEN = "SefpbOBF0C26dkV5vduYQWsebvOxNWoP"
    }

    data object Stock {
        const val CLIENT_NAME = "HttpClientStock"
        const val HOST = "finnhub.io"
        const val TOKEN_HEADER = "X-Finnhub-Token"
        const val TOKEN = "cum9nq9r01qsaphv45rgcum9nq9r01qsaphv45s0"
    }

    data object Exchange {
        const val CLIENT_NAME = "HttpClientExchange"
        const val HOST = "v6.exchangerate-api.com/v6"
        const val TOKEN = "bf0ad224573b24404c2e70a4"
    }
}


internal val ktorModule = module {

    with(HttpConstants.Exchange) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                host = "$HOST/$TOKEN/pair"
            }
        }
    }

    with(HttpConstants.News) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                host = HOST
            }
        }
    }

    with(HttpConstants.Stock) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                header(TOKEN_HEADER, TOKEN)
                host = HOST
            }
        }
    }
}