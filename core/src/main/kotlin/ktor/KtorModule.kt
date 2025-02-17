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




internal val ktorModule = module {

    with(HttpConstants.Core.Exchange) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                host = "$HOST/$TOKEN/pair"
            }
        }
    }

    with(HttpConstants.Core.News) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                host = HOST
            }
        }
    }

    with(HttpConstants.Core.Stock) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                header(TOKEN_HEADER, TOKEN)
                host = HOST
            }
        }
    }
}