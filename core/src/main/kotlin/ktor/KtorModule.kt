package ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

data object HttpConstants {
    data object News {
        const val CLIENT_NAME = "HttpClientNews"
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

    data object Default {
        const val CLIENT_NAME = "HttpClientDefault"
    }
}


internal val ktorModule = module {
    single<HttpClient>(named(HttpConstants.Default.CLIENT_NAME)) {
        HttpClientFactory().createFactory(
            androidContext = get()
//            block = {
//                install(HttpCache) {
//
////                        val cacheFile = Files.createDirectories(Paths.get("build/cache")).toFile()
//                    val cacheFile = File(androidContext().cacheDir, "ktor-cache")
//                    publicStorage(FileStorage(cacheFile))
//                }
//            }
        ) {
        }
    }

    with(HttpConstants.Exchange) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                url {
                    host = "$HOST/$TOKEN/pair"
                }

            }
        }
    }

    with(HttpConstants.News) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()
            ) {

            }
        }
    }

    with(HttpConstants.Stock) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()
            ) {
                header(TOKEN_HEADER, TOKEN)
                url {
                    host = HOST
                }
            }
        }
    }
}