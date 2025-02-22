package ktor

import consts.HttpConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import org.koin.core.qualifier.named
import org.koin.dsl.module


internal val ktorModule = module {

    with(HttpConstants.Core.Exchange) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory(get()) {
                host = HOST
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