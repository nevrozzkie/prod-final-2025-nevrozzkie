package ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import org.koin.core.qualifier.named
import org.koin.dsl.module

data object HttpConstants {
    data object News {
        const val CLIENT_NAME = "HttpClientNews"
    }

    data object Stock {
        const val CLIENT_NAME = "HttpClientStock"
        const val HOST = "finnhub.io/api/v1"
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
            HttpClientFactory().createFactory {
                url {
                    host = "$HOST/$TOKEN/latest"
                }
            }
        }
    }

    with(HttpConstants.News) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory {

            }
        }
    }

    with(HttpConstants.Stock) {
        single<HttpClient>(named(CLIENT_NAME)) {
            HttpClientFactory().createFactory {
                header(TOKEN_HEADER, TOKEN)
                url {
                    host = HOST
                }
            }
        }
    }
}